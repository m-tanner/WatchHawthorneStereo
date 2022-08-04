package com.watchhawthornestereo

import com.typesafe.config.{Config, ConfigFactory}

import javax.inject.Inject

class Settings @Inject() (config: Config) {
  val rootUrl: String = config.getString("app.config.rootUrl")
  val hawthorneRss: String = config.getString("app.config.hawthorneRss")
  val filepath: String = config.getString("app.config.filepath")
  val project: String = config.getString("app.config.project")
  val bucket: String = config.getString("app.config.bucket")
  val credentials: String = config.getString("app.config.credentials")
  require(credentials != "changme")
}

object Settings {
  def apply: Settings = {
    new Settings(ConfigFactory.load())
  }
}
