//ThisBuild / scalaVersion := "2.12.8"
//ThisBuild / organization := "friendbear.github.com"
scalaVersion := "2.12.8"
organization := "friendbear.github.com"

// https://mvnrepository.com/artifact/com.artima.supersafe/supersafe
libraryDependencies ++= Seq(
  "com.artima.supersafe" %% "supersafe" % "1.1.7"
)

lazy val commonSettings = Seq(
  version := "0.1.0"
)

lazy val root = (project in file(".")).
  settings(
    name := "scala-advanced",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.0.5",
      "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
    )


    //libraryDependencies ++= ...以下略
  )


resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.3")