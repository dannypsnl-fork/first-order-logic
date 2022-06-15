val scala3Version = "3.1.2"

enablePlugins(Antlr4Plugin)

lazy val root = project
  .in(file("."))
  .settings(
    name := "first-order-logic",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    Antlr4 / antlr4Version := "4.10.1",
    Antlr4 / antlr4PackageName := Some("org.kmu.fol.parser"),
    Antlr4 / antlr4GenListener := false,
    Antlr4 / antlr4GenVisitor := true,
  )
