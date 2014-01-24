name := "sbf-medlem"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.28",
  "commons-io" % "commons-io" % "2.4"
)     

play.Project.playScalaSettings
