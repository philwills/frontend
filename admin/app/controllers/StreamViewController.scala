package controllers

import play.api.mvc.Controller
import common.Logging
import tools.CloudWatch

object StreamViewController extends Controller with Logging with AuthLogging {
  def render() = AuthAction{ request =>
      Ok(views.html.streamview("PROD", CloudWatch.requestOkCount, CloudWatch.fastlyHitMissStatistics))
  }
}
