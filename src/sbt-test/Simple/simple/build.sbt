enablePlugins(UmlClassDiagramPlugin)

lazy val root = (project in file(".")).settings()

classDiagramFileName := "testA"
classDiagramSettings := classDiagramSettings.value.copy(generateSvg = false)
