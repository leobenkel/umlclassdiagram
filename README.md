# UML Class Diagram for Scala
Inspired by [xuwei-k/sbt-class-diagram](https://github.com/xuwei-k/sbt-class-diagram)

## Table of Contents

* [Setup](#setup)
   * [Requirements](#requirements)
   * [Enable plugin](#enable-plugin)
* [Use](#use)
* [Settings](#settings)
   * [To open the folder with output files and/or the SVG image](#to-open-the-folder-with-output-files-andor-the-svg-image)
   * [To change the name of the file](#to-change-the-name-of-the-file)

Created by [gh-md-toc](https://github.com/ekalinin/github-markdown-toc)

## Setup

### Requirements

You are required to install [Graphviz](https://www.graphviz.org/) to generate the SVG from the dot file.

If you do not need the generation of the SVG file, you can apply this setting:

```scala
classDiagramSettings := classDiagramSettings.value.copy(generateSvg = false)
```

in your `build.sbt`.

### Enable plugin

First add 

```scala
addSbtPlugin("com.leobenkel" % "umlclassdiagram" % "[VERSION]")
```

to `project/plugin.sbt`

and then 

```scala
enablePlugins(UmlClassDiagramPlugin)
```
to `build.sbt`

## Use

To analyze a specific class:

```bash
sbt classDiagram path.to.Class
```

To browse entire package:

```bash
sbt classDiagram path.to.package.*
```

## Settings

### To open the folder with output files and/or the SVG image

```scala
classDiagramSettings := classDiagramSettings.value.copy(openFolder = true, openSvg = true)
```

in your `build.sbt`.

### To change the name of the file

```scala
classDiagramFileName := "foo"
```

in your `build.sbt`.
