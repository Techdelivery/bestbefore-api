import sbt._
import Keys._
import xerial.sbt.Pack._

object Build extends Build {
  import BuildSettings._
  import Dependencies._

  // configure prompt to show current project
  override lazy val settings = super.settings :+ {
    shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Root Project
  // -------------------------------------------------------------------------------------------------------------------

  lazy val root = Project("root",file("."))
    .settings(basicSettings: _*)
    .aggregate(api)

  lazy val api_settings = basicSettings ++ packSettings ++
     Seq(
       // Specify mappings from program name -> Main class (full package path)
       packMain := Map("api" -> "org.techdelivery.property.spray.PropertyRegisterApp"),
       // Add custom settings here
       // [Optional] JVM options of scripts (program name -> Seq(JVM option, ...))
       packJvmOpts := Map("api" -> Seq("-Xmx512m")),
       // [Optional] Extra class paths to look when launching a program
       packExtraClasspath := Map("api" -> Seq("${PROG_HOME}/etc"))
     )

  lazy val api = Project("bbefore-api", file("bbefore-api"))
    .settings(api_settings: _*)
    .settings(libraryDependencies ++=
      compile(akkaActor, sprayCan, sprayRouting, sprayJson, logging, scalaMetrics, scaliak, time) ++
      test(specs2, scalatest) ++
      provided(akkaSlf4j, logback)
  )
}
