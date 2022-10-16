package com.watchhawthornestereo.storage

import com.typesafe.scalalogging.LazyLogging
import com.watchhawthornestereo.Settings

import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.{ FileAlreadyExistsException, Files, Path, Paths }
import javax.inject.Inject
import scala.io.Source.fromFile
import scala.util.{ Try, Using }

class LocalFilesystem @Inject() (settings: Settings) extends LazyLogging with StorageUtils {
  private val folder             = settings.filepath
  private val defaultFilepath    = s"$folder/$now" // TODO dafuq was i doing with these defaults
  private var mostRecentFilepath = defaultFilepath

  def readMostRecent: Try[String] = read(mostRecentFilepath)

  def read(path: String = defaultFilepath): Try[String] = Using(fromFile(path))(s => s.mkString)

  @throws(classOf[IOException])
  def save(contents: String, path: String = defaultFilepath): Unit = { // TODO update with filename
    val asPath = Paths.get(path)
    createDir(asPath)
    createFile(asPath)
    Files.write(asPath, contents.getBytes(StandardCharsets.UTF_8))
    mostRecentFilepath = path
  }

  private def createDir(pathToFile: Path): Unit = {
    try {
      Files.createDirectories(pathToFile.getParent)
    } catch {
      case _: FileAlreadyExistsException => // fine, it exists
      // other exceptions will explode
    }
  }

  private def createFile(pathToFile: Path): Unit = {
    try {
      Files.createFile(pathToFile)
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
