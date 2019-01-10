package com.thoughtworks.sbt

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

/**
  * @author 杨博 (Yang Bo)
  */
object Delombok extends AutoPlugin {

  import autoImport._

  override def trigger = allRequirements

  override def requires = JvmPlugin

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(Compile, Test).flatMap(
      inConfig(_)(
        inTask(delombok)(
          Seq(
            Defaults.TaskZero / delombokDirectory := target.value / "delombok",
            Defaults.TaskZero / delombok := {
              val classPathArgument = s"--classpath=${Path.makeString(dependencyClasspath.value.map(_.data))}"
              val targetArgument = s"--target=${delombokDirectory.value.getPath}"
              val sourceDirectoryArguments = sourceDirectories.value.map(_.getPath)
              val sourceFileArguments = for {
                sourceFile <- sources.value
                if sourceFile.ext == "java" &&
                  sourceDirectories.value.forall(sourceFile.relativeTo(_).isEmpty)
              } yield sourceFile.getPath

              runner.value
                .run(
                  "lombok.launch.Main",
                  dependencyClasspath.value.map(_.data),
                  Seq("delombok", "--nocopy", "--onlyChanged", classPathArgument, targetArgument) ++
                    sourceDirectoryArguments ++ sourceFileArguments,
                  streams.value.log
                )
                .get

              sources.value.view.collect {
                case sourceFile if sourceFile.ext == "java" =>
                  val relativeJavaFile = sourceDirectories.value
                    .collectFirst(Function.unlift(IO.relativize(_, sourceFile)))
                    .getOrElse(sourceFile.getName)
                  sourceFile -> delombokDirectory.value / relativeJavaFile
              }.toMap
            }
          )
        )
      )
    )

  object autoImport {
    val delombokDirectory = settingKey[File]("Directory to save delomboked files to")
    val delombok = taskKey[Map[File, File]]("Run delombok and return translated file pairs")
  }

}
