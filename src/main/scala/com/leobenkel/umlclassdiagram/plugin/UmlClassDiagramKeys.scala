package com.leobenkel.umlclassdiagram.plugin

import com.leobenkel.umlclassdiagram.internal.Diagram.DiagramSetting
import sbt.{inputKey, settingKey, File}

object UmlClassDiagramKeys {
  lazy val classDiagramFileName = settingKey[String]("Name of the output files")
  lazy val classDiagramDirectory = settingKey[File]("Directory where the output will be saved")
  lazy val classDiagramSettings = settingKey[DiagramSetting](
    "Setting for the diagram. https://www.graphviz.org/pdf/dotguide.pdf ."
  )
  lazy val classDiagram = inputKey[Unit]("Make the classDiagram")

  lazy val classDiagramFile = settingKey[File]("private - file to image")
}
