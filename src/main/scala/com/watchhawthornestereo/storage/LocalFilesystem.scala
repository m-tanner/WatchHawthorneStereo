package com.watchhawthornestereo.storage

import com.typesafe.scalalogging.LazyLogging
import com.watchhawthornestereo.Settings

import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.{FileAlreadyExistsException, Files, Paths}
import javax.inject.Inject
import scala.io.Source.fromFile
import scala.util.{Try, Using}

class LocalFilesystem @Inject()(settings: Settings) extends LazyLogging {
  private val path = Paths.get(settings.filepath)

  def read: Try[String] = Using(fromFile(settings.filepath)) { s => s.mkString }

  @throws(classOf[IOException])
  def save(contents: String): Unit = {
    createDir()
    createFile()
    Files.write(path, contents.getBytes(StandardCharsets.UTF_8))
  }

  private def createDir(): Unit = {
    try {
      Files.createDirectories(path.getParent)
    } catch {
      case _: FileAlreadyExistsException => // fine, it exists
      // other exceptions will explode
    }
  }

  private def createFile(): Unit = {
    try {
      Files.createFile(path)
    } catch {
      case _: FileAlreadyExistsException => // fine, it exists
      // all other exceptions will explode
    }
  }

}

object LocalFilesystem {
  def apply(settings: Settings): LocalFilesystem = {
    new LocalFilesystem(settings)
  }
}
