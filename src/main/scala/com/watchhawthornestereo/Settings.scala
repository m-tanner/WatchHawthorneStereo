package com.watchhawthornestereo

import com.typesafe.config.{Config, ConfigFactory}

import javax.inject.Inject

class Settings @Inject() (config: Config) {
  val rootUrl: String = config.getString("app.config.rootUrl")
  val hawthorneRss: String = config.getString("app.config.hawthorneRss")
  val filepath: String = config.getString("app.config.filepath")
}

object Settings {
  def apply: Settings = {
    new Settings(ConfigFactory.load())
  }
}
