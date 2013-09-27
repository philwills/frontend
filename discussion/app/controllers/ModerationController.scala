package controllers

import play.api.mvc.Action
import common.AkkaAgent
import discussion.model.{Insight, Profile, Comment}
import org.joda.time.DateTime

trait ModerationController extends DiscussionController {

   def moderation = Action { implicit r =>
     Ok(views.html.modqueue(discussionApi.queue()))
   }

  def moderate = Action { r =>
    val form = r.body.asFormUrlEncoded.get

    val insights = form.mapValues(_.head).flatMap {
      case (k, v) => if (v == "yes") Insight(k) else None
    }

    form("action").head match {
      case "approve" => discussionApi.approve(form("id").head.toInt, insights.toSeq)
      case "reject" => discussionApi.reject(form("id").head.toInt)
    }
    Redirect("/moderation")
  }
}
