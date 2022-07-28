import sbt.Keys._
import sbt._

object Common {
  val scala213 = "2.13.8"

  val settings = Seq(
    organization         := "com.watchhawthornestereo",
    organizationHomepage := Some(url("https://watchhawthornestereo.com")),
    scalaVersion         := scala213,
    excludeDependencies ++= Seq("org.slf4j" %% "slf4j-jdk14", "org.slf4j" %% "slf4j-log4j12"),
    Test / fork := true,
  )

  val playWsStandaloneVersion = "2.1.10"

  // Shared Dependencies
  val guice          = "com.google.inject"           % "guice"           % "5.1.0"
  val typesafeConfig = "com.typesafe"                % "config"          % "1.4.2"
  val scalaLogging   = "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.5"
  val logback        = "ch.qos.logback"              % "logback-classic" % "1.2.11"
  val json4sNative   = "org.json4s"                 %% "json4s-native"   % "4.0.5"
  val jSoup          = "org.jsoup"                   % "jsoup"           % "1.14.3"

  // Testing
  val scalaTest        = "org.scalatest"            %% "scalatest"               % "3.2.12"                % Test
  val scalaTestPlus    = "org.scalatestplus.play"   %% "scalatestplus-play"      % "5.1.0"                 % Test
  val scalaMock        = "org.scalamock"            %% "scalamock"               % "5.2.0"                 % Test
  val scalaCheck       = "org.scalacheck"           %% "scalacheck"              % "1.16.0"                % Test
  val playMockWS       = "de.leanovate.play-mockws" %% "play-mockws"             % "2.8.1"                 % Test
  val wsStandalone     = "com.typesafe.play"        %% "play-ahc-ws-standalone"  % playWsStandaloneVersion % Test
  val wsStandaloneJson = "com.typesafe.play"        %% "play-ws-standalone-json" % playWsStandaloneVersion % Test

}
