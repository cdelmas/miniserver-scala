import sbt._

object Dependencies {

  val opRabbitVersion = "2.1.0"

  lazy val opRabbit = Seq(
    "com.spingo" %% "op-rabbit-core",
    "com.spingo" %% "op-rabbit-circe"
  ).map(_ % opRabbitVersion)
  lazy val censorinus = Seq(
    "com.github.gphat" %% "censorinus" % "2.1.13"
  )

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
}
