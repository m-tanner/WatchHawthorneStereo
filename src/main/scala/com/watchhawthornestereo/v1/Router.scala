package com.watchhawthornestereo.v1

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

import javax.inject.Inject

class Router @Inject() (controller: Controller) extends SimpleRouter {

  override def routes: Routes = {
    case POST(p"/listings")       => controller.updateListings()
    case GET(p"/listings")        => controller.getListings("0")
    case GET(p"/listings/direct") => controller.getListingsDirect
    case GET(p"/listings/$index") => controller.getListings(index)
    case POST(p"/diff")           => controller.updateDiff()
    case GET(p"/diff")            => controller.getDiff("0")
    case GET(p"/diff/$index")     => controller.getDiff(index)
  }

}
