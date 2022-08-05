package com.watchhawthornestereo.hawthorne

import com.watchhawthornestereo.storage.{GoogleCloudStorage, LocalFilesystem}
import com.watchhawthornestereo.{PlaySpec, Settings}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.ws.ahc._
import play.api.mvc._
import play.api.{Environment, Mode}

import java.io.File
import scala.concurrent.ExecutionContext.global
import scala.concurrent.ExecutionContextExecutor
import scala.language.postfixOps

class HawthorneClientSpec extends PlaySpec with Results with GuiceOneAppPerSuite {

  private val settings = Settings.apply
  private val fs = LocalFilesystem(settings)
  private val gs = GoogleCloudStorage(settings)
  implicit val ec: ExecutionContextExecutor = global

  def withRealClient(testCode: HawthorneClient => Any): Any = {
    val environment = Environment(new File("."), this.getClass.getClassLoader, Mode.Test)
    val wsConfig = AhcWSClientConfigFactory.forConfig(classLoader = environment.classLoader)
    val mat = app.materializer
    val ws = AhcWSClient(wsConfig)(mat)
    try {
      testCode(HawthorneClient(ws, fs, gs, settings)(ec)) // loan the client
    } finally ws.close()

  }

  "HawthorneClient" must {
    "successfully connect to hawthorne rss and html sites, pull down data, and into case classes" in withRealClient {
      client =>
        val json = client.getListings
        json.take(49) must include("{\"listings\":[{\"link\":\"https://www.hawthornestereo")
        json mustNot include("null")
    }
    "successfully find the difference" in withRealClient { client =>
      // initialize an empty file
      fs.save("{\"listings\":[]}")
      val firstRun = client.getNewest
      firstRun.get.length must be > 100 // random number
      // contents should now be saved in the file
      // run again and diff should be nothing
      val secondRun = client.getNewest
      secondRun.get mustBe "{\"listings\":[]}"
    }
  }
}
