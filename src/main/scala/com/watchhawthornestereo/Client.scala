package com.watchhawthornestereo

import com.typesafe.scalalogging.LazyLogging
import com.watchhawthornestereo.storage.{ GoogleCloudStorage, LocalFilesystem }
import play.api.libs.ws.WSClient

import javax.inject.Inject
import scala.concurrent.ExecutionContext

abstract class Client @Inject() (ws: WSClient, fs: LocalFilesystem, gs: GoogleCloudStorage, settings: Settings)(
  implicit ec:                       ExecutionContext) extends LazyLogging {}
