package com.watchhawthornestereo

import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class SettingsSpec extends PlaySpec with GuiceOneAppPerSuite {

  private val settings = Settings.apply

  "Settings" must {
    "contain the proper settings" in {
      settings.rootUrl mustBe "https://watchhawthornestereo.com"
      settings.hawthorneRss mustBe "https://www.hawthornestereo.com/index.rss"
    }
    "be able to load from application configuration explicitly" in {
      val rootUrl = app.configuration.get[String]("app.config.rootUrl")
      rootUrl mustEqual "https://watchhawthornestereo.com"
    }
  }

}
