lazy val root = (project in file("."))
  .settings(
    name := "ktc",
    version := "0.0.1",
    scalaVersion := "3.7.1",
    javacOptions ++= Seq("-source", "17", "-target", "17"),
    Global / onChangedBuildSource := ReloadOnSourceChanges,
    Compile / javaSource := baseDirectory.value / "src" / "main" / "java",
    Test / javaSource := baseDirectory.value / "src" / "test" / "java",
    libraryDependencies ++= Seq(
      // Apache Pekko Actor system
      "org.apache.pekko" %% "pekko-actor-typed" % "1.1.4",

      // Stanford CoreNLP (OpenIE is part of the full CoreNLP bundle)
      "edu.stanford.nlp" % "stanford-corenlp" % "4.5.4",
      "edu.stanford.nlp" % "stanford-corenlp" % "4.5.4" classifier "models", // full models

      // Apache Jena
      "org.apache.jena" % "apache-jena-libs" % "4.5.0",
      "org.apache.jena" % "jena-fuseki-main" % "4.5.0",

      // SLF4J Simple Logger
      "org.slf4j" % "slf4j-simple" % "2.0.9",
      
      // Test dependencies
      "junit" % "junit" % "4.13.2" % Test,
      "com.github.sbt" % "junit-interface" % "0.13.3" % Test
    ),
    initialCommands := s"""
      import org.apache.jena.query._, org.apache.jena.rdfconnection._
    """,
    run / fork      := true,
    run / javaOptions ++= Seq(
      "-ea",
      "-Dlogback.configurationFile=logback.xml",
      "-Dedu.stanford.nlp.pipeline.useSingletonByDefault=true"
    ),
    Test / fork := true,
    Test / javaOptions ++= Seq(
      "-Xmx3g",
      "-ea"
    )
  )