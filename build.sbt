import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "io.github.cdelmas",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "miniserver-scala",
    libraryDependencies ++= censorinus ++ opRabbit ++ Seq (scalaTest % Test),
    assemblyJarName in assembly := "miniserver-scala.jar",
    mainClass in assembly := Some("io.github.cdelmas.miniserver.Run")
  )


