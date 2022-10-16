package com.watchhawthornestereo.storage

import com.watchhawthornestereo.{ PlaySpec, Settings }
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

import scala.language.postfixOps
import scala.util.{ Failure, Success }

class GoogleCloudStorageSpec extends PlaySpec with GuiceOneAppPerSuite {

  private val settings           = Settings.apply
  private val googleCloudStorage = GoogleCloudStorage(settings)

  "GoogleCloudStorage" must {
    "Successfully save a blob, then open it" in {
      val toSave = "{\"listings\":[]}"
      googleCloudStorage.save(toSave, "test/listings")
      val fromSave = googleCloudStorage.readMostRecentBy(0, "test/listings") match {
        case Success(value) => value
        case Failure(_)     => fail()
      }
      fromSave mustEqual toSave
    }
  }

}
