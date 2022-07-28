package com.watchhawthornestereo.hawthorne

import com.watchhawthornestereo.{PlaySpec, Settings}
import org.scalatest.PrivateMethodTester
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.ws.ahc._
import play.api.mvc._
import play.api.{Environment, Mode}

import java.io.File
import scala.concurrent.ExecutionContext.global
import scala.concurrent.ExecutionContextExecutor
import scala.language.postfixOps

class HawthorneClientSpec extends PlaySpec with Results with PrivateMethodTester with GuiceOneAppPerSuite {

  private val settings                      = Settings.apply
  implicit val ec: ExecutionContextExecutor = global

  def withRealClient(testCode: HawthorneClient => Any): Any = {
    val environment = Environment(new File("."), this.getClass.getClassLoader, Mode.Test)
    val wsConfig    = AhcWSClientConfigFactory.forConfig(classLoader = environment.classLoader)
    val mat         = app.materializer
    val ws          = AhcWSClient(wsConfig)(mat)
    try {
      testCode(HawthorneClient(ws, settings)(ec)) // loan the client
    } finally ws.close()

  }

  "HawthorneClient" must {
    "successfully connect to hawthorne rss and html sites, pull down data, and into case classes" in withRealClient {
      client =>
        val json                         = client.findNewListings()

        // for debugging
        // println(prettyPrint(Json.parse(json)))

        json.take(49) must include("{\"listings\":[{\"link\":\"https://www.hawthornestereo")
        json mustNot include("null")
        // this is obviously a fragile test, but it ensures happy path is working for now
    }
  }
}