package controllers

import play.api.mvc.Controller
import common.{JsonComponent, ExecutionContexts, Logging}
import discussion.DiscussionApi
import model.Cached
import common._
import play.api.mvc.{ Controller, Action }
import discussion.DiscussionApi
import model.Cached
import play.libs.Json._
import play.api.libs.json.{JsString, JsArray, JsNumber, JsObject}



object HighlightedCommentsController extends Controller with Logging with ExecutionContexts {

  def render(shortUrl: String) = Action { implicit request =>
    val promiseOfHighlightedComments = DiscussionApi.highlightedComments()

    Async {
      promiseOfHighlightedComments.map{ comment =>
        Cached(60){
          JsonComponent(
            JsObject(Seq("comments" -> JsArray(comment.map(_.toJson)))
      }
    }
  }
}
}
