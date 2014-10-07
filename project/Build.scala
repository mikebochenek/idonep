import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "eventual"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "mysql" % "mysql-connector-java" % "5.1.21")

  val main = play.Project(appName, appVersion, appDependencies).settings()
}