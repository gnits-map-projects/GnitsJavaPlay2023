lazy val root = (project in file(".")).enablePlugins(PlayJava).settings(
  name := """GnitsJavaPlay2023""",
  organization := "com.example",
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.13.11",
  libraryDependencies ++= Seq(
    guice, javaForms, caffeine
  ),
  (Test / testOptions) := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
)