package controllers

import play.api.mvc.Controller
import common.Logging
import tools.CloudWatch
import play.api.libs.json._
import tools.Chart

object StreamViewController extends Controller with Logging with AuthLogging {
  def render() = AuthAction{ request =>
      Ok(views.html.streamview("PROD",
                               CloudWatch.requestOkCount,
                               CloudWatch.fastlyHitMissStatistics,
                               CloudWatch.healthyHostEuWest1aCount,
                               CloudWatch.healthyHostEuWest1bCount
                               ))
  }

  def renderJson() = AuthAction { request =>

    val response = Map(
      "requestOkCount" -> CloudWatch.requestOkCount.toString,
      "fastlyStats"    -> CloudWatch.fastlyHitMissStatistics.toString,
      "instances"      -> CloudWatch.healthyHostEuWest1aCount.toString
    )

    Ok(Json.toJson(response)).as("application/javascript")

  }
}
