import sbt.Keys._
import sbt._

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / javacOptions := Seq(
  //format: off
  "-source", "11",
  "-target", "11",
  //format: on
)

Common.settings

maintainer := "tanner.mbt@gmail.com"

lazy val root = (project in file("."))
  .settings(
    name := "watch_hawthorne_stereo"
  )
  .enablePlugins(SbtTwirl)

val runtimeDependencies = Seq(
  Common.typesafeConfig,
  Common.scalaLogging,
  Common.logback,
  Common.guice, // eliminates the use of reflection
  play.sbt.PlayImport.guice, // needs both guice dependencies
  play.sbt.PlayImport.ws,
  Common.json4sNative,
  Common.jSoup,
  Common.gCloudStorage,
  Common.akkaPubSub,
  Common.akkaStream,
  Common.akkaHttp,
  Common.akkaSpray,
  Common.mailer,
  Common.mailerGuice,
)

libraryDependencies ++= runtimeDependencies ++ testDependencies
val testDependencies = Seq(
  Common.scalaTest,
  Common.scalaTestPlus,
  Common.wsStandalone,
  Common.wsStandaloneJson,
)

Compile / scalaSource       := baseDirectory.value / "src/main/scala"
Compile / resourceDirectory := baseDirectory.value / "src/main/resources"
Compile / TwirlKeys.compileTemplates / sourceDirectories := (Compile / unmanagedSourceDirectories).value

Test / scalaSource       := baseDirectory.value / "src/test/scala"
Test / resourceDirectory := baseDirectory.value / "src/test/resources"

Assets / resourceDirectory := baseDirectory.value / "src/main/public"

enablePlugins(PlayScala, PlayService, JavaAppPackaging)

Compile / publishArtifact := false

// Commands
addCommandAlias("build", "prepare; test")
addCommandAlias("fmt", "all scalafmtSbt scalafmtAll")
addCommandAlias("fmtCheck", "all scalafmtSbtCheck scalafmtCheckAll")
addCommandAlias("check", "fmtCheck")
