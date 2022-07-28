package com.watchhawthornestereo.v1

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

import javax.inject.Inject

class Router @Inject()(controller: Controller) extends SimpleRouter {

  override def routes: Routes = { case GET(p"/listings/new") =>
    controller.findNewListings()
  }

}
