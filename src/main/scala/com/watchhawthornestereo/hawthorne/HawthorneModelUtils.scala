package com.watchhawthornestereo.hawthorne

import play.api.libs.json.{ Json, Reads }

trait HawthorneModelUtils {

  def asList(s: String): List[UnifiedListing] = {
    implicit val unifiedListingReads: Reads[UnifiedListing] = Json.reads[UnifiedListing]

    Json.parse(s).as[UnifiedListings](Json.reads[UnifiedListings]).listings
  }

}
