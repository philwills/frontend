package discussion

import _root_.model.Page
import common.{AkkaAgent, ExecutionContexts, Logging}
import common.DiscussionMetrics.DiscussionHttpTimingMetric
import conf.Switches.ShortDiscussionSwitch
import play.api.libs.json._
import System.currentTimeMillis
import scala.concurrent.Future
import org.joda.time.DateTime
import play.api.libs.json.JsArray
import play.api.libs.ws.Response
import play.api.libs.json.JsObject
import play.api.libs.json.JsNumber
import discussion.model.{Profile, Comment, CommentCount}

trait DiscussionApi extends ExecutionContexts with Logging {

  protected def GET(url: String): Future[Response]
  protected val apiRoot: String

  val profile = Profile("http://static.guim.co.uk/sys-images/Guardian/Pix/site_furniture/2010/09/01/no-user-image.gif", "Faker", false, false)

  lazy val comments = AkkaAgent(Seq(
    Comment(0, "Fake comment body", Seq(),
      profile, DateTime.now, false, false, None, 10)
  ))
//  def comments() = Seq[Comment]()

  def postComment(body: String) = {
    comments.send(cs => cs :+ Comment(cs.length, body, Seq(), profile, DateTime.now, false, false, None, 10))
  }


  def commentCounts(ids: String): Future[Seq[CommentCount]] = {
    def onError(response: Response) =
      s"Error loading comment count ids: $ids status: ${response.status} message: ${response.statusText}"
    val apiUrl = s"$apiRoot/getCommentCounts?short-urls=$ids"

//<<<<<<< HEAD
    val start = currentTimeMillis
    Future.successful(Seq(CommentCount("123", 10)))
  }

  def commentsFor(key: String, page: String) = {
    val size = if (ShortDiscussionSwitch.isSwitchedOn) 10 else 50

    Future.successful(CommentPage(
      id= s"disussion/$key",
      title= "title",
      contentUrl = "http://url.com",
      comments = comments(),
      currentPage = 1,
      pages = 1,
      isClosedForRecommendation = false
    ))
//=======
//    val apiUrl = s"$apiRoot/discussion/$key?pageSize=$size&page=$page&orderBy=oldest&showSwitches=true"
//
//    def onError(r: Response) =
//      s"Error loading comments id: $key status: ${r.status} message: ${r.statusText}"
//
//    getJsonOrError(apiUrl, onError) map {
//      json =>
//        val comments = (json \\ "comments")(0).asInstanceOf[JsArray].value map {
//          commentJson =>
//            val responses = (commentJson \\ "responses").headOption map {
//              responsesJson =>
//                responsesJson.asInstanceOf[JsArray].value map {
//                  responseJson =>
//                    Comment(responseJson)
//                }
//            } getOrElse Nil
//            Comment(commentJson, responses)
//        }
//
//        CommentPage(
//          id = s"discussion/$key",
//          title = (json \ "discussion" \ "title").as[String],
//          contentUrl = (json \ "discussion" \ "webUrl").as[String],
//          comments = comments,
//          currentPage = (json \ "currentPage").as[Int],
//          pages = (json \ "pages").as[Int],
//          isClosedForRecommendation = (json \ "discussion" \ "isClosedForRecommendation").as[Boolean]
//        )
//    }
  }

  protected def getJsonOrError(url: String, onError: (Response) => String):Future[JsValue] = {
    val start = currentTimeMillis()
    GET(url) map {
      response =>
        DiscussionHttpTimingMetric.recordTimeSpent(currentTimeMillis - start)

        response.status match {
          case 200 =>
            Json.parse(response.body)

          case _ =>
            log.error(onError(response))
            throw new RuntimeException("Error from Discussion API")
        }
    }
  }

}


