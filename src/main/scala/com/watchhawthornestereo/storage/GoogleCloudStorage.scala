package com.watchhawthornestereo.storage

import com.google.cloud.storage.Storage.BlobListOption
import com.google.cloud.storage.{ BlobId, BlobInfo, StorageOptions }
import com.typesafe.scalalogging.LazyLogging
import com.watchhawthornestereo.Settings

import java.nio.charset.StandardCharsets
import javax.inject.Inject
import scala.collection.mutable.ListBuffer
import scala.util.{ Success, Try }

class GoogleCloudStorage @Inject() (settings: Settings) extends LazyLogging with StorageUtils {
  private val bucket  = settings.bucket
  private val project = settings.project

  private val storage = StorageOptions.newBuilder().setProjectId(project).build().getService

  def getMostRecentObjectInBucket(subfolder: String): List[String] = {
    val pagedBlobs   = storage.list(bucket, BlobListOption.prefix(subfolder))
    val blobIterator = pagedBlobs.iterateAll().iterator()
    val blobIds      = ListBuffer[String]()
    while (blobIterator.hasNext) {
      val blob = blobIterator.next()
      blobIds += blob.getName
    }
    blobIds.toList
  }

  def readMostRecentBy(index: Int, inSubfolder: String): Try[String] = {
    // TODO fix the `+ 1` hack, which is just used to avoid getting the folder name in the list
    val blob = getMostRecentObjectInBucket(inSubfolder)(index + 1) // will explode if accessing unavailable index
    read(blob)
  }

  def read(blobId: String): Try[String] = {
    val asGoogleBlogId = BlobId.of(bucket, blobId)
    Success(fromArrayBytesToString(storage.get(asGoogleBlogId).getContent()))
  }

  private def fromArrayBytesToString(arr: Array[Byte]): String = {
    arr.map(_.toChar).mkString
  }

  def save(contents: String, subfolder: String, path: String = bucket): Unit = {
    val name     = s"$subfolder/$now" // this enables sending test results to `test` folder
    val blobId   = BlobId.of(path, name)
    val blobInfo = BlobInfo.newBuilder(blobId).build()
    storage.create(blobInfo, contents.getBytes(StandardCharsets.UTF_8))
  }

}

object GoogleCloudStorage {

  def apply(settings: Settings): GoogleCloudStorage = {
    new GoogleCloudStorage(settings)
  }

}
