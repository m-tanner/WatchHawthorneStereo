package com.watchhawthornestereo.storage

import com.google.cloud.storage.{BlobId, BlobInfo, StorageOptions}
import com.typesafe.scalalogging.LazyLogging
import com.watchhawthornestereo.Settings

import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import javax.inject.Inject
import scala.util.{Success, Try}

class GoogleCloudStorage @Inject()(settings: Settings) extends LazyLogging {
  private val bucket = settings.bucket
  private val filename = Paths.get(settings.filepath).getFileName.toString
  private val project = settings.project

  private val storage = StorageOptions.newBuilder().setProjectId(project).build().getService
  private val blobId = BlobId.of(bucket, filename)
  private val blobInfo = BlobInfo.newBuilder(blobId).build()

  def read: Try[String] = Success(fromArrayBytesToString(storage.get(blobId).getContent()))

  private def fromArrayBytesToString(arr: Array[Byte]): String = {
    arr.map(_.toChar).mkString
  }

  def save(contents: String): Unit = storage.create(blobInfo, contents.getBytes(StandardCharsets.UTF_8))
}

object GoogleCloudStorage {
  def apply(settings: Settings): GoogleCloudStorage = {
    new GoogleCloudStorage(settings)
  }
}
