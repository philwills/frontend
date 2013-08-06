package model


class IdentityPage(id: String, webTitle: String, analyticsName: String, analiticsEvents: List[String])
  extends Page(id, "identity", webTitle, analyticsName) {
  override def metaData: Map[String, Any] = super.metaData + ("analyticsEvents" -> analiticsEvents)
}
