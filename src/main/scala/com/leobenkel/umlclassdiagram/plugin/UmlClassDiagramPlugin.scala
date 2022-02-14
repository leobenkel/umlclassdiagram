package com.leobenkel.umlclassdiagram.plugin

import com.leobenkel.umlclassdiagram.internal._
import com.leobenkel.umlclassdiagram.internal.Diagram.{DiagramContent, DiagramSetting}
import sbt.{AutoPlugin, ClassLoaderLayeringStrategy, Def, InputTask, Keys => SbtKeys, Task, Test, _}
import sbt.PluginTrigger.AllRequirements

//noinspection scala2InSource3
object UmlClassDiagramPlugin extends AutoPlugin {
  lazy final override val allRequirements = AllRequirements
  private val keys: UmlClassDiagramKeys.type = UmlClassDiagramKeys

  object autoImport {
    val classDiagram = keys.classDiagram
    val classDiagramProcessDotFileToSvg = keys.classDiagramProcessDotFileToSvg
    val classDiagramDirectory = keys.classDiagramDirectory
    val classDiagramSettings = keys.classDiagramSettings
  }
  import autoImport._

  private def classDiagramRun: Def.Initialize[InputTask[Unit]] =
    Def.inputTaskDyn {
      val classPath: Set[ClassPath] = InputParser.root.parsed

      makeDiagram(classPath)
    }

  private def makeDiagram(
    input: Set[ClassPath]
  ): Def.Initialize[Task[Unit]] =
    Def.taskDyn {
      val classLoader = (Test / SbtKeys.testLoader).value
      val dir = classDiagramDirectory.value
      val settings = classDiagramSettings.value

      Def.task {
        val diagram = Diagram(input, classLoader, settings)
        makeSvg(dir, settings, diagram)
      }
    }

  private def makeSvg(
    dir:      File,
    settings: DiagramSetting,
    diagram:  DiagramContent
  ): Unit = {
    if (!dir.exists()) dir.mkdirs()
    require(dir.isDirectory, s"The path '$dir' is not a directory.")
    val svgFile = Diagram.writeDotAndSvgFile(dir.getAbsolutePath, settings)(diagram)
    if (settings.openSvg && settings.generateSvg) java.awt.Desktop.getDesktop.open(svgFile)
    if (settings.openFolder) java.awt.Desktop.getDesktop.open(dir)
  }

  private def makeSvg: Def.Initialize[Task[Unit]] =
    Def.taskDyn {
      val dir = classDiagramDirectory.value
      val settings = classDiagramSettings.value

      Def.task {
        val diagram = Diagram.readDotFileContent(dir.getAbsolutePath, settings)
        makeSvg(dir, settings, diagram)
      }
    }

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      SbtKeys.classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat,
      classDiagram                        := classDiagramRun.evaluated,
      classDiagramProcessDotFileToSvg     := makeSvg.value,
      classDiagramDirectory               := SbtKeys.target.value / "uml-class-diagram",
      classDiagramSettings := Diagram.defaultSettings(SbtKeys.name.value + "-classDiagram")
    )
}
