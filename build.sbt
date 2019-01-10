organization in ThisBuild := "com.thoughtworks.sbt"

val Delombok = project

val DelombokJavadoc = project.dependsOn(Delombok)
