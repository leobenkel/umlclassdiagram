enablePlugins(UmlClassDiagramPlugin)

lazy val root = (project in file(".")).settings()

classDiagramSettings := classDiagramSettings.value.copy(name = "testA", generateSvg = false)
