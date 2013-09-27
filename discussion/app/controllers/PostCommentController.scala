package controllers

import play.api.mvc.Action
import discussion.model.Insight

trait PostCommentController extends DiscussionController {

  def postComment() =  Action { implicit request =>
    val form = request.body.asFormUrlEncoded.get
    discussionApi.postComment(form.apply("body").head, form.getOrElse("insight", Seq()) flatMap (Insight(_)) )
    Redirect("http://localhost:9000/lifeandstyle/the-womens-blog-with-jane-martinson/2013/sep/26/project-bush-pubic-region-photographed-ad-agency#comments")
  }
}
