import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "io.github.cdelmas",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "miniserver-scala",
    scalacOptions ++= Seq(
      "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
      "-encoding", "utf-8",                // Specify character encoding used by source files.
      "-language:existentials",            // Existential types (besides wildcard types) can be written and inferred
      "-language:higherKinds"             // Allow higher-kinded types
    ),
    libraryDependencies ++= Seq(catsEffect, kamon, kamonStatsD, fs2Rabbit) ++ Seq (scalaTest % Test),
    assemblyJarName in assembly := "miniserver-scala.jar",
    mainClass in assembly := Some("io.github.cdelmas.miniserver.Run")
  )


