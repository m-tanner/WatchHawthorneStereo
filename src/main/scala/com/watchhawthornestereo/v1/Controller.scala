package com.watchhawthornestereo.v1

import com.typesafe.scalalogging.LazyLogging
import com.watchhawthornestereo.hawthorne.HawthorneClient
import play.api.mvc._

import javax.inject.Inject
import scala.language.postfixOps
import scala.util.{ Failure, Success, Try }

class Controller @Inject() (val hawthorneClient: HawthorneClient, val controllerComponents: ControllerComponents)
    extends BaseController
    with LazyLogging {

  /**
   * This API is used to trigger the fetching of new listings from Hawthorne.
   *
   * It fetches the listings and saves them to Google Cloud Storage.
   */
  def updateListings(): Action[AnyContent] = {
    val methodName = "updateListings"

    val response = hawthorneClient.updateListings()

    handleCommon(response, methodName)
  }

  /**
   * This API is used to get the most-recent-stored version of the listings from Google Cloud Storage.
   */
  def getListings(index: String): Action[AnyContent] = {
    val methodName = "getListings"

    val maybeInt = index.toIntOption

    val response = maybeInt match {
      case Some(i) => hawthorneClient.getListings(i)
      case _       => Failure(new Exception("not an int"))
    }

    handleCommon(response, methodName)
  }

  /**
   * This API is used to get the most-recent-stored version of the diff from Google Cloud Storage.
   */
  def getDiff(index: String): Action[AnyContent] = {
    val methodName = "getDiff"

    val maybeInt = index.toIntOption

    val response = maybeInt match {
      case Some(i) => hawthorneClient.getDiff(i)
      case _       => Failure(new Exception("not an int"))
    }

    handleCommon(response, methodName)
  }

  /**
   * This API is used to trigger the calculation of a new diff.
   *
   * It fetches the two most recent files from Google Cloud Storage and then saves the result to Google Cloud Storage.
   *
   * It also returns the diff, just like it would in a for a new call to `.getDiff`
   */
  def updateDiff(): Action[AnyContent] = {
    val methodName = "updateDiff"
    logger.info(methodName)

    val response = hawthorneClient.updateDiff()

    handleCommon(response, methodName)
  }

  /**
   * This API is used to fetch the RSS directly from Hawthorne's own site. This is mainly to facilitate finding issues with the Google Cloud
   * Storage-backed versions.
   */
  def getListingsDirect: Action[AnyContent] = {
    val methodName = "getListingsDirect"
    logger.info(methodName)

    val response = Try(hawthorneClient.getListingsDirect)

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
    val mb      = 1024 * 1024
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
