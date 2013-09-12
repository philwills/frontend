package controllers

import common._
import model._
import play.api.mvc.{ Controller, Action }
import feed._
import org.joda.time.format.DateTimeFormat
import conf.ContentApi
import scala.concurrent.Future

case class MatchPage(theMatch: FootballMatch, report: Option[Article], otherRes: List[FootballMatch]) extends MetaData with ExecutionContexts {

  override lazy val id = theMatch.id
  override lazy val section = "sport"
  override lazy val webTitle = s"${theMatch.homeTeam} - ${theMatch.awayTeam}"
  override lazy val analyticsName = "unknown"
}

object MatchController extends Controller with Logging with ExecutionContexts {

  private val dateFormat = DateTimeFormat.forPattern("yyyyMMMdd")

  def renderMatch(year: String, month: String, day: String, home: String, away: String) = Action { implicit request =>

    // These updates would be done on an agent.
    FootballClient.updateTeams()
    FootballClient.updateCompetitions()

    val date = dateFormat.parseDateTime(year + month + day).toDateMidnight

    val result = FootballClient.findResult(date, home, away)

    val otherRes = FootballClient.findOtherResults(date)

    //val report =
    val edition = Edition(request)

    val reportPath : Option[String] = result.flatMap{ _.contentId.lift(1)}

    val promiseOfArticle : Future[Option[Article]]=
      ContentApi.item(reportPath.getOrElse(""),edition)
        .showExpired(true)
        .showTags("all")
        .showFields("all")
        .response.map{ response =>
          response.content.filter {c => c.isArticle } map { new Article(_) }
        }

    result.map { matchFound =>
      Async {
        promiseOfArticle.map { article:Option[Article] =>
          Ok(views.html.oppMatch(MatchPage(matchFound, article, otherRes)))
        }
      }
    }.getOrElse(Ok("nothing found"))
  }
}
