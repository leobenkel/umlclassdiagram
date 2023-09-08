sbtPlugin := true

organization := "com.leobenkel"
homepage     := Some(url("https://github.com/leobenkel/umlclassdiagram"))
licenses     := List("MIT" -> url("https://opensource.org/licenses/MIT"))
developers :=
  List(
    Developer(
      "leobenkel",
      "Leo Benkel",
      "",
      url("https://leobenkel.com")
    )
  )
sonatypeCredentialHost := "oss.sonatype.org"
sonatypeRepository     := "https://oss.sonatype.org/service/local"
val projectName = IO.readLines(new File("PROJECT_NAME")).head
name := projectName

publishMavenStyle := true

// fail to publish without that
updateOptions := updateOptions.value.withGigahorse(false)

Test / publishArtifact := false

pomIncludeRepository := (_ => false)

resolvers += Resolver.bintrayRepo("scalameta", "maven")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")

libraryDependencies += "commons-logging" % "commons-logging" % "1.2"
libraryDependencies += "org.reflections" % "reflections"     % "0.10.2"

// Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % Test

import org.scoverage.coveralls.Imports.CoverallsKeys.{coverallsFailBuildOnError, coverallsFile}

// https://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html

enablePlugins(SbtPlugin)

scriptedLaunchOpts ++= Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
scriptedBufferLog         := false
scriptedParallelInstances := 1
scriptedBatchExecution    := false

coverageOutputDebug := true

coverallsFailBuildOnError := true

coverallsFile := baseDirectory.value / "coveralls/coveralls.json"

classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.ScalaLibrary
allowZombieClassLoaders     := false
closeClassLoaders           := true
