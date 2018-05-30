import sbt._

object Dependencies {

  val opRabbitVersion = "2.1.0"
  val circeVersion = "0.9.3"

  lazy val opRabbit = Seq(
    "com.spingo" %% "op-rabbit-core",
    "com.spingo" %% "op-rabbit-circe"
  ).map(_ % opRabbitVersion)
  lazy val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
}
