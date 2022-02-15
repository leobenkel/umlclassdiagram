

![umlclassdiagram example][umlclassdiagram-img]

# UML Class Diagram for Scala



[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![release-badge][]][release]
[![maven-central-badge][]][maven-search]
[![CI-CD](https://github.com/leobenkel/umlclassdiagram/workflows/CI-CD/badge.svg)](https://github.com/leobenkel/umlclassdiagram/actions/workflows/ci_cd.yml)
[![BCH compliance](https://bettercodehub.com/edge/badge/leobenkel/umlclassdiagram?branch=main)](https://bettercodehub.com/)
[![Coverage Status](https://coveralls.io/repos/github/leobenkel/umlclassdiagram/badge.svg?branch=main)](https://coveralls.io/github/leobenkel/umlclassdiagram?branch=main)


[release]:              https://github.com/leobenkel/umlclassdiagram/releases
[release-badge]:        https://img.shields.io/github/tag/leobenkel/umlclassdiagram.svg?label=version&color=blue
[maven-search]:         https://search.maven.org/search?q=g:com.leobenkel%20a:umlclassdiagram*
[maven-search-test]:         https://search.maven.org/search?q=g:com.leobenkel%20a:umlclassdiagram-test*
[leobenkel-github-badge]:     https://img.shields.io/badge/-Github-yellowgreen.svg?style=social&logo=GitHub&logoColor=black
[leobenkel-github-link]:      https://github.com/leobenkel
[leobenkel-linkedin-badge]:     https://img.shields.io/badge/-Linkedin-yellowgreen.svg?style=social&logo=LinkedIn&logoColor=black
[leobenkel-linkedin-link]:      https://linkedin.com/in/leobenkel
[leobenkel-personal-badge]:     https://img.shields.io/badge/-Website-yellowgreen.svg?style=social&logo=data:image/svg+xml;base64,PHN2ZyBoZWlnaHQ9JzMwMHB4JyB3aWR0aD0nMzAwcHgnICBmaWxsPSIjMDAwMDAwIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiB2ZXJzaW9uPSIxLjEiIHg9IjBweCIgeT0iMHB4IiB2aWV3Qm94PSIwIDAgNjQgNjQiIGVuYWJsZS1iYWNrZ3JvdW5kPSJuZXcgMCAwIDY0IDY0IiB4bWw6c3BhY2U9InByZXNlcnZlIj48Zz48Zz48cGF0aCBkPSJNNDEuNiwyNy4yYy04LjMsMC0xNSw2LjctMTUsMTVzNi43LDE1LDE1LDE1YzguMywwLDE1LTYuNywxNS0xNVM0OS45LDI3LjIsNDEuNiwyNy4yeiBNNTEuNSwzNmgtMy4zICAgIGMtMC42LTEuNy0xLjQtMy4zLTIuNC00LjZDNDguMiwzMi4yLDUwLjIsMzMuOSw1MS41LDM2eiBNNDEuNiwzMS41YzEuMywxLjIsMi4zLDIuNywzLDQuNGgtNkMzOS4zLDM0LjIsNDAuNCwzMi43LDQxLjYsMzEuNXogICAgIE0zNy40LDMxLjNjLTEsMS40LTEuOCwyLjktMi40LDQuNmgtMy4zQzMzLjEsMzMuOSwzNS4xLDMyLjIsMzcuNCwzMS4zeiBNMzAuMyw0NWMtMC4yLTAuOS0wLjQtMS44LTAuNC0yLjhjMC0xLDAuMS0yLDAuNC0yLjkgICAgaDMuOWMtMC4xLDEtMC4yLDEuOS0wLjIsMi45YzAsMC45LDAuMSwxLjksMC4yLDIuOEgzMC4zeiBNMzEuNyw0OC4zSDM1YzAuNiwxLjcsMS40LDMuNCwyLjQsNC44QzM1LDUyLjIsMzMsNTAuNSwzMS43LDQ4LjN6ICAgICBNNDEuNiw1Mi45Yy0xLjMtMS4yLTIuMy0yLjgtMy4xLTQuNWg2LjFDNDQsNTAuMSw0Mi45LDUxLjcsNDEuNiw1Mi45eiBNMzcuNiw0NWMtMC4yLTAuOS0wLjItMS44LTAuMi0yLjhjMC0xLDAuMS0yLDAuMy0yLjloOCAgICBjMC4yLDAuOSwwLjMsMS45LDAuMywyLjljMCwxLTAuMSwxLjktMC4yLDIuOEgzNy42eiBNNDUuOCw1My4xYzEtMS40LDEuOC0zLDIuNC00LjhoMy4zQzUwLjIsNTAuNSw0OC4yLDUyLjIsNDUuOCw1My4xeiBNNDksNDUgICAgYzAuMS0wLjksMC4yLTEuOCwwLjItMi44YzAtMS0wLjEtMi0wLjItMi45aDMuOWMwLjIsMC45LDAuNCwxLjksMC40LDIuOWMwLDEtMC4xLDEuOS0wLjQsMi44SDQ5eiI+PC9wYXRoPjxwYXRoIGQ9Ik0zNCwyNS45Yy0wLjktMC43LTEuOC0xLjMtMi45LTEuOGMyLTIuMSwzLjItNC45LDMuMi03LjljMC02LjMtNS4xLTExLjQtMTEuNC0xMS40UzExLjYsOS45LDExLjYsMTYuMiAgICBjMCwzLjEsMS4yLDUuOSwzLjIsNy45Yy00LjEsMi02LjgsNS40LTcuMSw5LjRsLTAuMywzLjhjMCwyLDcsMy42LDE1LjYsMy42YzAuMiwwLDAuNSwwLDAuNywwQzI0LjIsMzQuMywyOC4yLDI4LjYsMzQsMjUuOXogICAgIE0yMyw4LjhjNC4xLDAsNy40LDMuMyw3LjQsNy40cy0zLjMsNy40LTcuNCw3LjRzLTcuNC0zLjMtNy40LTcuNFMxOC45LDguOCwyMyw4Ljh6Ij48L3BhdGg+PC9nPjwvZz48L3N2Zz4=&logoColor=black
[leobenkel-personal-link]:      https://leobenkel.com
[leobenkel-patreon-link]:            https://www.patreon.com/leobenkel
[leobenkel-patreon-badge]: https://img.shields.io/badge/-Patreon-yellowgreen.svg?style=social&logo=Patreon&logoColor=black
[maven-central-link]:           https://maven-badges.herokuapp.com/maven-central/com.leobenkel/umlclassdiagram_2.11
[maven-central-badge]:          https://maven-badges.herokuapp.com/maven-central/com.leobenkel/umlclassdiagram_2.11/badge.svg
[maven-central-link-test]:           https://maven-badges.herokuapp.com/maven-central/com.leobenkel/umlclassdiagram-test_2.11
[maven-central-badge-test]:          https://maven-badges.herokuapp.com/maven-central/com.leobenkel/umlclassdiagram-test_2.11/badge.svg

[umlclassdiagram-img]: https://raw.githubusercontent.com/leobenkel/umlclassdiagram/main/assets/umlclassdiagram-classDiagram-internal.png


Inspired by [xuwei-k/sbt-class-diagram](https://github.com/xuwei-k/sbt-class-diagram)

## Table of Contents

* [Setup](#setup)
   * [Requirements](#requirements)
   * [Enable plugin](#enable-plugin)
   * [Which connections would you like to show?](#which-connections-would-you-like-to-show)
   * [Styling](#styling)
* [Use](#use)
* [Settings](#settings)
   * [To open the folder with output files and/or the SVG image](#to-open-the-folder-with-output-files-andor-the-svg-image)
   * [To change the name of the file](#to-change-the-name-of-the-file)
* [Authors](#authors)
    * [Leo Benkel](#leo-benkel)

Table of content generated by [gh-md-toc](https://github.com/ekalinin/github-markdown-toc)

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

### Which connections would you like to show?

To change which connection you would like to display, add this:
```scala

classDiagramSettings :=
  classDiagramSettings
    .value
    .copy(
      enabledConnectionTypes =
        Set(
          com.leobenkel.umlclassdiagram.internal.ConnectionType.Inherit,
          com.leobenkel.umlclassdiagram.internal.ConnectionType.Produce,
          com.leobenkel.umlclassdiagram.internal.ConnectionType.Consume,
          com.leobenkel.umlclassdiagram.internal.ConnectionType.Throw,
          com.leobenkel.umlclassdiagram.internal.ConnectionType.Enclosing,
        ),
    )
```

to your `build.sbt`.

The possibilities are:

* **Inherit**
  * will connect the classes with their parent trait and classes
  * This is the **default** behavior.
* **Produce**
  * Will connect the classes to the classes that can be returned
  * Will not find out about generic type like `Seq[A]` or `Foo[A]`
* **Consume**
  * Will connect the classes to the classes that are used as arguments
* **Throw**
  * Will connect the classes to the classes being thrown
* **Enclosing**
  * Will connect the classes to the parent class where the class is defined is it exist.

### Styling

You can read more about the key/values available for dot graph styling with this document: https://www.graphviz.org/pdf/dotguide.pdf .

## Use

To analyze a specific class:

```bash
sbt classDiagram path.to.Class
```

To browse entire package:

```bash
sbt classDiagram path.to.package.*
```

And those can be combined:

```bash
sbt classDiagram path.to.Class path.to.package.* pack.to.other.Class
```

## Settings

### To open the folder with output files and/or the SVG image

```scala
classDiagramSettings := classDiagramSettings.value.copy(openFolder = true, openSvg = true)
```

in your `build.sbt`.

### To change the name of the file

```scala
classDiagramSettings := classDiagramSettings.value.copy(name = "foo")
```

in your `build.sbt`.

## Authors

### Leo Benkel

* [![leobenkel-github-badge][]][leobenkel-github-link]
* [![leobenkel-linkedin-badge][]][leobenkel-linkedin-link]
* [![leobenkel-personal-badge][]][leobenkel-personal-link]
* [![leobenkel-patreon-badge][]][leobenkel-patreon-link]
