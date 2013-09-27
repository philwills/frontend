package discussion
package model

import _root_.model._
import org.joda.time.DateTime
import play.api.libs.json.{JsNumber, JsString, JsObject, JsValue}

case class CommentCount(id: String, count: Int) {
  lazy val toJson = JsObject(
    Seq(
      "id" -> JsString(id),
      "count" -> JsNumber(count)
    )
  )
}

case object Expertise extends Insight{
  def describe = "claims expertise"
  def id = "expertise"
}
case object Eyewitness extends Insight{
  def describe = "claims to be an eyewitness"
  def id = "eyewitness"
}
case object PersonalExperience extends Insight{
  def describe = "claims to have personal experience"
  def id = "experience"
}

trait Insight {
  def id: String
  def describe: String
}
object Insight {
  def apply(s: String) = s match {
    case "expertise" => Some(Expertise)
    case "experience" => Some(PersonalExperience)
    case "eyewitness" => Some(Eyewitness)
    case _ => None
  }
}

case class Comment(
  id: Int,
  body: String,
  responses: Seq[Comment],
  profile: Profile,
  date: DateTime,
  isHighlighted: Boolean,
  isBlocked: Boolean,
  responseTo: Option[ResponseTo] = None,
  numRecommends: Int,
  insight: Seq[Insight] = Seq()
)

object Comment extends {

  def apply(json: JsValue): Comment = Comment(json, Nil)

  def apply(json: JsValue, responses: Seq[Comment]): Comment = {
      Comment(
        id = (json \ "id").as[Int],
        body = (json \ "body").as[String],
        responses = responses,
        profile = Profile(json),
        date = (json \ "isoDateTime").as[String].parseISODateTimeNoMillis,
        isHighlighted = (json \ "isHighlighted").as[Boolean],
        isBlocked = (json \ "status").as[String].contains("blocked"),
        responseTo = (json \\ "responseTo").headOption.map(ResponseTo(_)),
        numRecommends = (json \ "numRecommends").as[Int]
    )
  }
}

case class ResponseTo(
  displayName: String,
  commentId: String
)

object ResponseTo {
  def apply(json: JsValue): ResponseTo = {
    ResponseTo(
      displayName = (json \ "displayName").as[String],
      commentId = (json \ "commentId").as[String]
    )
  }
}
