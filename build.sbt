ThisBuild / scalaVersion := "2.12.8"

val project_name = "CloudCityExperiments"
name := project_name

val paradiseVersion = "2.1.0"
val breezeVersion = "0.13.2"
val scalaTestVersion = "3.1.2"
val squidVersion = "0.4.1-SNAPSHOT"

run / fork := true

lazy val commonSettings = Seq(
  libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  libraryDependencies += "org.scalanlp" %% "breeze" % breezeVersion,
  libraryDependencies += "org.scalanlp" %% "breeze-natives" % breezeVersion,
)

lazy val squidSettings = Seq(
  autoCompilerPlugins := true,
  addCompilerPlugin(
    "org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full
  ),
  unmanagedBase := (unmanagedBase in LocalRootProject).value
)

lazy val runAll = taskKey[Unit]("run-all, for compiling all meta examples")

def runAllIn(config: Configuration) = Def.task {
    val s = streams.value
    val cp = (config / fullClasspath).value
    val r = (run / runner).value
    (config / discoveredMainClasses).value.foreach(c =>
      r.run(c, cp.files, Seq(), s.log))
}

lazy val example = (project in file("example"))
  .settings(
    name := f"${project_name}-example",
    commonSettings, squidSettings,
    libraryDependencies += "ch.epfl.data" %% "cloudcity-core" % "2.0-SNAPSHOT",
    libraryDependencies += "ch.epfl.data" %% "cloudcity-library" % "2.0-SNAPSHOT",
    runAll := runAllIn(Compile).value,
    Test / parallelExecution := false,
  )

lazy val genExample = (project in file("generated"))
  .settings(
    name := f"${project_name}-genExample",
    libraryDependencies += "ch.epfl.data" %% "cloudcity-akka" % "2.0-SNAPSHOT",
    Test / parallelExecution := false,
    commonSettings
  ).dependsOn(example)
