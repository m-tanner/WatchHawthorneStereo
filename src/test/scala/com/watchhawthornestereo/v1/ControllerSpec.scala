package com.watchhawthornestereo.v1

import com.watchhawthornestereo.hawthorne.HawthorneClient
import com.watchhawthornestereo.storage.{GoogleCloudStorage, LocalFilesystem}
import com.watchhawthornestereo.{PlaySpec, Settings}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.ws.ahc.{AhcWSClient, AhcWSClientConfigFactory}
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
import play.api.{Environment, Mode}

import java.io.File
import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContextExecutor, Future}

class ControllerSpec extends PlaySpec with Results with GuiceOneAppPerSuite {

  private val settings = Settings.apply
  private val fs = LocalFilesystem(settings)
  private val gs = GoogleCloudStorage(settings)
  implicit val ec: ExecutionContextExecutor = global

  def withRealController(testCode: Controller => Any): Any = {
    val environment = Environment(new File("."), this.getClass.getClassLoader, Mode.Test)
    val wsConfig = AhcWSClientConfigFactory.forConfig(classLoader = environment.classLoader)
    val mat = app.materializer
    val ws = AhcWSClient(wsConfig)(mat)
    val spotifyClient = HawthorneClient(ws, fs, gs, settings)(ec)
    try {
      testCode(Controller(spotifyClient, Helpers.stubControllerComponents())) // loan the controller
    } finally ws.close()
  }

  "Controller" must {
    "return valid 200 response" in withRealController { controller =>
      val result: Future[Result] = controller.getListings().apply(FakeRequest())
      val json: String = contentAsString(result)
      json.take(49) must include("{\"listings\":[{\"link\":\"https://www.hawthornestereo")
      json mustNot include("null")
    }
  }
}
