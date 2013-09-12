package feed

import common.{Edition, ExecutionContexts, Logging}
import scala.concurrent.{Await, Future, ExecutionContext}
import play.api.libs.ws.{Response, WS}
import pa._

import org.joda.time.{DateTimeConstants, DateMidnight, DateTime}
import scala.xml.{XML, NodeSeq}

import org.neo4j.graphdb.{GraphDatabaseService, Node, RelationshipType, Transaction}
import org.neo4j.graphdb.index.{UniqueFactory, Index}

import org.neo4j.rest.graphdb.RestGraphDatabase
import conf.{ContentApi, Configuration}
import common.PaMetrics.{PaApiHttpErrorMetric, PaApiHttpOkMetric, PaApiHttpTimingMetric}
import model.Tag
import com.gu.openplatform.contentapi.model.{SearchResponse, TagsResponse}
import scala.concurrent.duration._
import common.editions.Uk
import org.joda.time.format.DateTimeFormat

// remove
import language.reflectiveCalls
import language.postfixOps

trait Feed extends PaClient with Http with Logging with ExecutionContexts {

  // Can be overridden for test.
  def get(url: String): Future[Response] = WS.url(url).withTimeout(2000).get()

  private val baseUrl: String = "http://pads6.pa-sport.com/api/"
  private val key: String = "/dxj451p9/"
  protected var teamMap = Map.empty[String, Tag]
}

trait SportsMatch {
  def id: String
  def date: DateTime
}

case class League (
  name: String,
  start: DateMidnight,
  end: DateMidnight,
  leagueId: String,
  seasonId: String
)

case class FootballMatch(
  id: String,
  date:DateTime,
  homeTeam: String,
  awayTeam: String,
  homeTeamId: String,
  awayTeamId: String,
  homeScore: Int,
  awayScore: Int,
  homeScorers: String,
  awayScorers: String,
  isResult: Boolean,
  contentId: List[String]) extends SportsMatch
{
  def homeTeamHasScored = homeScore > 0
  def awayTeamHasScored = awayScore > 0
  def url():String = {
    s"/sport/oppm/${date.toString("yyyy")}/${date.toString("MMM")}/${date.toString("dd")}/${FootballClient.findUrlNameFor(homeTeamId).getOrElse("")}-v-${FootballClient.findUrlNameFor(awayTeamId).getOrElse("")}"
  }
}

object FootballClient extends Feed{

  private val dateFormat = DateTimeFormat.forPattern("yyyyMMdd")

  override lazy val base = Configuration.pa.host

  val league = // League("Barclays Premier League 13/14", new DateMidnight(2013,6,1), new DateMidnight(2014, 5, 31), "100", "785")
                  League("Barclays Premier League 13/14", new DateMidnight(2013,8,30), new DateMidnight(2014, 5, 31), "100", "785")


  private var _http: Http = new Http {
    override def GET(urlString: String): Future[pa.Response] = {
      val start = System.currentTimeMillis()
      val promiseOfResponse = WS.url(urlString).withTimeout(2000).get()
      promiseOfResponse.onComplete( r => PaApiHttpTimingMetric.recordTimeSpent(System.currentTimeMillis() - start))

      promiseOfResponse.map{ r =>

        r.status match {
          case 200 => PaApiHttpOkMetric.recordCount(1)
          case _ => PaApiHttpErrorMetric.recordCount(1)
        }

        //this feed has a funny character at the start of it http://en.wikipedia.org/wiki/Zero-width_non-breaking_space
        //I have reported to PA, but just trimming here so we can carry on development
        pa.Response(r.status, r.body.dropWhile(_ != '<'), r.statusText)
      }
    }
  }

  def http = _http
  def http_=(delegateHttp: Http) = _http = delegateHttp

  lazy val apiKey = Configuration.pa.apiKey

  override def GET(urlString: String): Future[pa.Response] = {
    _http.GET(urlString)
  }

  def addLeague(graphDb: GraphDatabaseService)
  {
    // add an index and make a unique node factory
    val leagueIndex:Index[Node]  = graphDb.index.forNodes("league")

    val hits = leagueIndex.get("league", "Premier League").getSingle();
    if (hits == null)
    {
      val newNode = graphDb.createNode()
      val result = leagueIndex.putIfAbsent(newNode, "league", league.name)
      newNode.setProperty("league", league.name)
    }
  }

  def addFixture(graphDb: GraphDatabaseService, fixture:Fixture)
  {
    val leagueIndex:Index[Node]  = graphDb.index.forNodes("league")

    val leagueNode:Node = leagueIndex.get("league", league.name).getSingle()

    val fixturesIndex:Index[Node]  = graphDb.index.forNodes("fixture")

    val matchName: String = s"${fixture.homeTeam.id}-${fixture.awayTeam.id}"
    val matchDate: Int = fixture.date.toString("yyyyMMdd").toInt

    // If the index doesn't have an entry for this fixture, populate a new node and then
    // add it to the index.
    val hits = fixturesIndex.get(matchName, matchDate).getSingle()
    if (hits == null)
    {
      val fixtureNode = graphDb.createNode()
      val result = fixturesIndex.putIfAbsent(fixtureNode, matchName, matchDate)
      fixtureNode.setProperty( "homeTeam", fixture.homeTeam.id)
      fixtureNode.setProperty( "awayTeam", fixture.awayTeam.id)
      fixtureNode.setProperty( "homeTeamName", fixture.homeTeam.name)
      fixtureNode.setProperty( "awayTeamName", fixture.awayTeam.name)
      fixtureNode.setProperty( "date", matchDate)

      val relationship = leagueNode.createRelationshipTo(fixtureNode, new RelationshipType {
        def name = "FIXTURE"})

      fixturesIndex.putIfAbsent(fixtureNode, "homeTeam", fixture.homeTeam.name)
      fixturesIndex.putIfAbsent(fixtureNode, "awayTeam", fixture.awayTeam.name)
      fixturesIndex.putIfAbsent(fixtureNode, "matchName", matchName)
      fixturesIndex.putIfAbsent(fixtureNode, "date", matchDate)
    }
  }

  def addResult(graphDb:GraphDatabaseService, result:Result)
  {
    val leagueIndex:Index[Node]  = graphDb.index.forNodes("league")

    val leagueNode:Node = leagueIndex.get("league", league.name).getSingle()

    val resultsIndex:Index[Node]  = graphDb.index.forNodes("result")

    val matchName: String = s"${result.homeTeam.id}-${result.awayTeam.id}"
    val matchDate: Int = result.date.toString("yyyyMMdd").toInt

    val hits = resultsIndex.get(matchName, matchDate).getSingle()
    if (hits == null)
    {
      val resultNode = graphDb.createNode()


      resultNode.setProperty( "homeTeamName", result.homeTeam.name)
      resultNode.setProperty( "awayTeamName", result.awayTeam.name)
      resultNode.setProperty( "homeTeam", result.homeTeam.id)
      resultNode.setProperty( "awayTeam", result.awayTeam.id)
      resultNode.setProperty( "homeScore", result.homeTeam.score.getOrElse(-1))
      resultNode.setProperty( "awayScore", result.awayTeam.score.getOrElse(-1))
      resultNode.setProperty( "homeScorers", result.homeTeam.scorers.getOrElse(""))
      resultNode.setProperty( "awayScorers", result.awayTeam.scorers.getOrElse(""))
      //result.venue.map {f:Venue => resultNode.setProperty( "venue", f.name)}
      resultNode.setProperty( "date", matchDate)
      resultNode.setProperty( "matchName", matchName)

      val relationship = leagueNode.createRelationshipTo(resultNode, new RelationshipType {
        def name = "RESULT"})

      // a unique field.
      resultsIndex.add(resultNode, matchName, matchDate)

      resultsIndex.add(resultNode, "homeTeam", result.homeTeam.name)
      resultsIndex.add(resultNode, "awayTeam", result.awayTeam.name)
      resultsIndex.add(resultNode, "matchName", matchName)
      resultsIndex.add(resultNode, "date", matchDate)

      val gameweekDate = findGameweek(result.date)
      resultsIndex.add(resultNode, "gameweek", gameweekDate.toString("yyyyMMdd").toInt)

      // A content node should be added to the graph that links this fixture to the content api report.
      // Add a filter temporarily, to avoid spamming the content api.
      val response: Future[SearchResponse] =
        ContentApi.search(Uk)
          .section("football")
          .tag("tone/matchreports|football/series/squad-sheets|football/series/saturday-clockwatch")
          .fromDate(result.date.minusDays(2))
          .toDate(result.date.plusDays(2))
          .reference(s"pa-football-team/${result.homeTeam.id},pa-football-team/${result.awayTeam.id}")
          .response

      val contentIds: List[String] = Await.result(response, 5.seconds).results.map(_.id)

      if (!contentIds.isEmpty)
      {
        val contentIndex:Index[Node]  = graphDb.index.forNodes("content")

        val hits = contentIndex.get(matchName, matchDate).getSingle()

        if (hits == null)
        {
          val contentNode = graphDb.createNode()
          contentIndex.putIfAbsent(contentNode, matchName, matchDate)

          contentNode.setProperty("contentId", contentIds.toArray)

          resultNode.createRelationshipTo(contentNode, new RelationshipType {
            def name = "CONTENT"})
        }
      }
    }
  }

  def updateCompetitions()
  {
    val graphDb: GraphDatabaseService = new RestGraphDatabase("http://localhost:7474/db/data")
    val tx: Transaction = graphDb.beginTx()

    try
    {
      // Write to graph db, the league node (and should eventually add the table)
      addLeague(graphDb)

      // use less fixtures. dev hack
      val fixtureList: List[Fixture] =  Await.result(fixtures(league.leagueId), 5.seconds)
      fixtureList.take(10).map{ addFixture(graphDb,_) }

      val resultsList: List[Result] = Await.result(results(league.leagueId, league.start), 5.seconds)
      resultsList.map{ addResult(graphDb,_) }

      tx.success()
    }
    finally
    {
      tx.finish()
      graphDb.shutdown()
    }
  }

  def updateTeams(page: Int = 1)
  {
    val response: Future[TagsResponse] = ContentApi.tags
      .page(page)
      .pageSize(50)
      .referenceType("pa-football-team")
      .showReferences("pa-football-team")
      .response

    val result: TagsResponse = Await.result(response, 5.seconds)

    if (result.pages > page) {
      updateTeams(page + 1 )
    }

    val tagReferences = result.results.map { tag => (tag.references.head.id.split("/")(1), Tag(tag)) }.toMap
    teamMap = teamMap ++ tagReferences
  }

  def findTeamIdByUrlName(name: String): Option[String] = teamMap.find(_._2.id == s"football/$name").map(_._1)
  def findUrlNameFor(teamId: String): Option[String] = teamMap.get(teamId).map(_.url.replace("/football/", ""))

  def findFixture( date:DateMidnight, homeTeam: String, awayTeam: String ) : Option[SportsMatch] =
  {
    val graphDb: GraphDatabaseService = new RestGraphDatabase("http://localhost:7474/db/data")
    val tx: Transaction = graphDb.beginTx()

    try
    {
      val fixturesIndex:Index[Node]  = graphDb.index.forNodes("fixture")

      val homeTeamId = findTeamIdByUrlName(homeTeam).getOrElse("")
      val awayTeamId = findTeamIdByUrlName(awayTeam).getOrElse("")

      val matchName: String = s"${homeTeamId}-${awayTeamId}"
      val matchDate: Int = date.toString("yyyyMMdd").toInt

      val hits = fixturesIndex.get(matchName, matchDate).getSingle()

      if (hits != null)
      {
        return Some(FootballMatch(matchName, date.toDateTime, homeTeam, awayTeam, homeTeamId, awayTeamId, -1, -1, "", "", false, List.empty))
      }
    }
    finally
    {
      tx.finish()
      graphDb.shutdown()
    }

    None
  }

  def findResult( date:DateMidnight, homeTeam: String, awayTeam: String ) : Option[FootballMatch] =  {
    val graphDb: GraphDatabaseService = new RestGraphDatabase("http://localhost:7474/db/data")
    val tx: Transaction = graphDb.beginTx()

    try
    {
      val homeTeamId = findTeamIdByUrlName(homeTeam).getOrElse("")
      val awayTeamId = findTeamIdByUrlName(awayTeam).getOrElse("")

      val matchName: String = s"${homeTeamId}-${awayTeamId}"
      val matchDate: Int = date.toString("yyyyMMdd").toInt

      val resultsIndex:Index[Node] = graphDb.index.forNodes("result")

      val resultsQuery = resultsIndex.get(matchName, matchDate).getSingle()

      if (resultsQuery!= null)
      {
        val homeScore = resultsQuery.getProperty("homeScore").asInstanceOf[Int]
        val awayScore = resultsQuery.getProperty("awayScore").asInstanceOf[Int]
        val homeScorers = resultsQuery.getProperty("homeScorers").asInstanceOf[String]
        val awayScorers = resultsQuery.getProperty("awayScorers").asInstanceOf[String]
        val homeTeamName = resultsQuery.getProperty("homeTeamName").asInstanceOf[String]
        val awayTeamName = resultsQuery.getProperty("awayTeamName").asInstanceOf[String]

        // Find any related content too.
        val contentIndex:Index[Node]  = graphDb.index.forNodes("content")
        val hits = contentIndex.get(matchName, matchDate).getSingle()

        var content: List[String] = List.empty

        if (hits != null)
        {
          content = hits.getProperty("contentId").asInstanceOf[Array[String]].toList
        }

        return Some(FootballMatch( matchName, date.toDateTime, homeTeamName, awayTeamName, homeTeamId, awayTeamId, homeScore, awayScore, homeScorers, awayScorers, true, content))
      }
    }
    finally
    {
      tx.finish()
      graphDb.shutdown()
    }
    None
  }

  def findGameweek(date:DateMidnight): DateMidnight =
  {
    // Find the previous wednesday
    if (date.getDayOfWeek() >= DateTimeConstants.WEDNESDAY)
    {
      date.withDayOfWeek(DateTimeConstants.WEDNESDAY)
    }
    else
    {
      date.minusWeeks(1).withDayOfWeek(DateTimeConstants.WEDNESDAY)
    }
  }

  def findOtherResults(date:DateMidnight) :List[FootballMatch] = {
    import scala.collection.JavaConverters._

    // Given a league and a date, find the other results that exist in the same weds-thurs window.
    val gameweekDate = findGameweek(date)

    val graphDb: GraphDatabaseService = new RestGraphDatabase("http://localhost:7474/db/data")
    val resultsIndex:Index[Node] = graphDb.index.forNodes("result")

    val resultsQuery: java.util.Iterator[Node] = resultsIndex.get("gameweek", gameweekDate.toString("yyyyMMdd").toInt)

    resultsQuery.asScala.map{
      node =>
        val homeScore = node.getProperty("homeScore").asInstanceOf[Int]
        val awayScore = node.getProperty("awayScore").asInstanceOf[Int]
        val homeScorers = node.getProperty("homeScorers").asInstanceOf[String]
        val awayScorers = node.getProperty("awayScorers").asInstanceOf[String]
        val homeTeamName = node.getProperty("homeTeamName").asInstanceOf[String]
        val awayTeamName = node.getProperty("awayTeamName").asInstanceOf[String]
        val matchName = node.getProperty("matchName").asInstanceOf[String]
        val date = node.getProperty( "date" ).asInstanceOf[Int]
        val homeTeamId = matchName.split("-")(0)
        val awayTeamId = matchName.split("-")(1)
        FootballMatch( matchName, dateFormat.parseDateTime(date.toString), homeTeamName, awayTeamName, homeTeamId, awayTeamId,homeScore, awayScore, homeScorers, awayScorers, false, List.empty)
    }.toList
  }

}