package com.thoughtworks.sbt

import com.thoughtworks.sbt.Delombok.autoImport._
import sbt.Keys._
import sbt._

/**
  * @author 杨博 (Yang Bo)
  */
object DelombokJavadoc extends AutoPlugin {
  override def trigger = allRequirements

  override def requires: Plugins = Delombok

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(Compile, Test).flatMap(
      inConfig(_)(
        inTask(doc)(
          Seq(
            sources := {
              val delombokMap = delombok.value
              sources.value.map { sourceFile: File =>
                delombokMap.getOrElse(sourceFile, sourceFile)
              }
            }
          )
        )
      )
    )
}
