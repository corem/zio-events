ThisBuild / scalaVersion := "3.3.0"
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalacOptions ++=
  Seq(
    "-source:future",
    "-deprecation"
  )

lazy val server = (project in file("server"))
  .configs(IntegrationTest)
  .settings(
    name := "tail",
    libraryDependencies ++= Dependencies.all,
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val producer = (project in file("producer"))
  .settings(
    name := "producer",
    libraryDependencies ++= Dependencies.all
  )