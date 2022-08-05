package com.watchhawthornestereo.v1

import com.typesafe.scalalogging.LazyLogging
import com.watchhawthornestereo.hawthorne.HawthorneClient
import play.api.mvc._

import javax.inject.Inject
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

class Controller @Inject()(val hawthorneClient: HawthorneClient, val controllerComponents: ControllerComponents)
  extends BaseController
    with LazyLogging {

  def getNewest: Action[AnyContent] = {
    val methodName = "getNewest"
    logger.info(methodName)

    val response = hawthorneClient.getNewest

    handleCommon(response, methodName)
  }

  def getListings: Action[AnyContent] = {
    val methodName = "getListings"
    logger.info(methodName)

    val response = Try(hawthorneClient.getListings)

    handleCommon(response, methodName)
  }

  private def handleCommon(response: Try[String], methodName: String): Action[AnyContent] = Action {
    logMemory()

    response match {
      case Success(s) =>
        logger.info(s"$methodName succeeded")
        Ok(s)
      case Failure(_) =>
        logger.error(s"$methodName failed")
        InternalServerError("sorry, but we could not complete your request")
    }
  }
  private def logMemory(): Unit = {
    // memory info
    val mb = 1024 * 1024
    val runtime = Runtime.getRuntime
    logger.debug("** ALL RESULTS IN MB **")
    logger.debug("** Used Memory:  " + (runtime.totalMemory - runtime.freeMemory) / mb)
    logger.debug("** Free Memory:  " + runtime.freeMemory / mb)
    logger.debug("** Total Memory: " + runtime.totalMemory / mb)
    logger.debug("** Max Memory:   " + runtime.maxMemory / mb)
  }

}

object Controller {
  def apply(hawthorneClient: HawthorneClient, controllerComponents: ControllerComponents): Controller =
    new Controller(hawthorneClient, controllerComponents)
}
