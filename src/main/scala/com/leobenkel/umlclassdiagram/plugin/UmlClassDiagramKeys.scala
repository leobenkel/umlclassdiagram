package com.leobenkel.umlclassdiagram.plugin

import com.leobenkel.umlclassdiagram.internal.Diagram.DiagramSetting
import sbt.{inputKey, settingKey, taskKey, File}

object UmlClassDiagramKeys {
  lazy val classDiagramDirectory = settingKey[File]("Directory where the output will be saved")
  lazy val classDiagramSettings = settingKey[DiagramSetting](
    "Setting for the diagram. https://www.graphviz.org/pdf/dotguide.pdf ."
  )
  lazy val classDiagram = inputKey[Unit]("Make the classDiagram")
  lazy val classDiagramProcessDotFileToSvg =
    taskKey[Unit]("Make svg from an already produced dot file.")

  lazy val classDiagramFile = settingKey[File]("private - file to image")
}
