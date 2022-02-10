package com.leobenkel.umlclassdiagram.plugin

import com.leobenkel.umlclassdiagram.internal._
import sbt.{AutoPlugin, ClassLoaderLayeringStrategy, Def, InputTask, Keys => SbtKeys, Task, Test, _}
import sbt.PluginTrigger.AllRequirements

//noinspection scala2InSource3
object UmlClassDiagramPlugin extends AutoPlugin {
  lazy final override val allRequirements = AllRequirements
  private val keys: UmlClassDiagramKeys.type = UmlClassDiagramKeys

  object autoImport {
    val classDiagram = keys.classDiagram
    val classDiagramFileName = keys.classDiagramFileName
    val classDiagramDirectory = keys.classDiagramDirectory
    val classDiagramSettings = keys.classDiagramSettings
  }
  import autoImport._

  private def classDiagramRun: Def.Initialize[InputTask[Unit]] =
    Def.inputTaskDyn {
      val classPath = InputParser.root.parsed

      makeDiagram(classPath)
    }

  private def makeDiagram(
    input: ClassPath
  ): Def.Initialize[Task[Unit]] =
    Def.taskDyn {
      val classLoader = (Test / SbtKeys.testLoader).value
      val dir = classDiagramDirectory.value
      val name = classDiagramFileName.value
      val settings = classDiagramSettings.value

      Def.task {
        if (!dir.exists()) dir.mkdirs()
        require(dir.isDirectory, s"The path '$dir' is not a directory.")

        val diagram = Diagram(input, classLoader, settings)
        val svgFile = Diagram.writeDotAndSvgFile(dir.getAbsolutePath, settings)(diagram)
        if (settings.openSvg && settings.generateSvg) java.awt.Desktop.getDesktop.open(svgFile)
        if (settings.openFolder) java.awt.Desktop.getDesktop.open(dir)
      }
    }

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      SbtKeys.classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat,
      classDiagram                        := classDiagramRun.evaluated,
      classDiagramFileName                := SbtKeys.name.value + "-classDiagram",
      classDiagramDirectory               := SbtKeys.target.value / "uml-class-diagram",
      classDiagramSettings                := Diagram.defaultSettings(classDiagramFileName.value)
    )
}
