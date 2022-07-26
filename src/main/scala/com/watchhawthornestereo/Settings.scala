package com.streamingswap

import com.typesafe.config.{ Config, ConfigFactory }

import javax.inject.Inject

class Settings @Inject() (config: Config) {
  val rootUrl: String             = config.getString("app.config.rootUrl")
}

object Settings {
  def apply: Settings = {
    new Settings(ConfigFactory.load())
  }
}
