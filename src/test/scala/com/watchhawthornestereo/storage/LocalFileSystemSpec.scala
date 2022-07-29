package com.watchhawthornestereo.storage

import com.watchhawthornestereo.{PlaySpec, Settings}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

import scala.language.postfixOps
import scala.util.{Failure, Success}

class LocalFileSystemSpec extends PlaySpec with GuiceOneAppPerSuite {

  private val settings = Settings.apply
  private val localFilesystem = LocalFilesystem(settings)

  "LocalFileSystem" must {
    "Successfully save a file, then open it" in {
      val toSave = "{\"listings\":[]}"
      localFilesystem.save(toSave)
      val fromSave = localFilesystem.read match {
        case Success(value) => value
        case Failure(_) => fail()
      }
      fromSave mustEqual toSave
    }
  }
}
