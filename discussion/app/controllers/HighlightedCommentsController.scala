package controllers

import common._
import play.api.mvc.{ Controller, Action }
import discussion.DiscussionApi
import model._


object HighlightedCommentsController extends Controller with Logging with ExecutionContexts {

  def render() = Action { implicit request =>
    val promiseOfHighlightedComments = DiscussionApi.highlightedComments()

    Async {
      promiseOfHighlightedComments.map{ highlightedComments =>
        Cached(600){
            Ok(views.html.highlightedComments(highlightedComments))
        }
      }
    }
  }
}
