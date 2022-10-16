package com.watchhawthornestereo.publish

import com.watchhawthornestereo.storage.LocalFilesystem
import com.watchhawthornestereo.{ PlaySpec, Settings }
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class PublisherSpec extends PlaySpec with GuiceOneAppPerSuite {

  private val settings  = Settings.apply
  private val fs        = LocalFilesystem(settings)
  private val publisher = Publisher(settings)

  "Publisher" must {
    "publish!" in {
      publisher.publish(fs.read("./src/test/resources/listings.json").get)
    }
  }

}
