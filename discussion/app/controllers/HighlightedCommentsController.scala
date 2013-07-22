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

  def render() = Action { implicit request =>
    val promiseOfHighlightedComments = DiscussionApi.highlightedComments()

    Async {
      promiseOfHighlightedComments.map{ highlightedComments =>
        Cached(60){
          Ok(views.html.highlightedComments(highlightedComments))
      }
    }
  }
}
}
