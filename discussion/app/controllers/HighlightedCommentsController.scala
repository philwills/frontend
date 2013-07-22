package controllers

import common._
import play.api.mvc.{ Controller, Action }
import discussion.DiscussionApi
import model._


object HighlightedCommentsController extends Controller with Logging with ExecutionContexts with implicits.Requests {

  def renderJson() = render()
  def render() = Action { implicit request =>
    val promiseOfHighlightedComments = DiscussionApi.highlightedComments()

    Async {
      promiseOfHighlightedComments.map{ highlightedComments =>

        if (request.isJson) {

          highlightedComments.comments.headOption.map{ comment =>
            Cached(60){
              JsonComponent(views.html.fragments.comment(comment))
            }
          }.getOrElse(NotFound)


        } else {

        Cached(600){
            Ok(views.html.highlightedComments(highlightedComments))
        }       }
      }
    }
  }
}
