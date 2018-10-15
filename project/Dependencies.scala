import sbt._

object Dependencies {

  object Versions {
    val catsEffect = "1.0.0"
    val fs2Rabbit = "1.0-RC3"
    val kamon = "1.1.0"
    val kamonStatsD = "1.0.0"
    val censorinus = "2.1.13"
  }

  lazy val censorinus = "com.github.gphat" %% "censorinus" % Versions.censorinus
  lazy val fs2Rabbit = "com.github.gvolpe" %% "fs2-rabbit" % Versions.fs2Rabbit
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
}
