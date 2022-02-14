package com.leobenkel.umlclassdiagram.internal

import com.leobenkel.umlclassdiagram.internal.ConnectionType._
import java.io.File

//noinspection scala2InSource3
object Diagram {
  case class DiagramContent(s: String) extends AnyVal

  def apply(
    input:       Set[ClassPath],
    classLoader: ClassLoader,
    settings:    DiagramSetting
  ): DiagramContent = {
    val nodes: Set[Node] = input.flatMap(ProcessClassPath(_)(classLoader))
    val expanded = {
      def loop(
        current: Node,
        acc:     Set[Node] = Set.empty
      ): Set[Node] =
        settings
          .enabledConnectionTypes
          .flatMap(_.getConnections(current))
          .filter(n => input.exists(_.isValid(n)))
          .flatMap {
            case p if acc.contains(p) => acc + p + current
            case p                    => loop(p, acc + p + current)
          } ++ acc + current

      nodes.flatMap(loop(_))
    }

    // TODO: Change style in settings for packages looked at from input
    //  and for specific classes looked at from input.
    produceDiagram(expanded, settings)
  }

  def readDotFileContent(
    dir:      String,
    settings: DiagramSetting
  ): DiagramContent = {
    val name = settings.name
    val dotFile = new File(dir, name + ".dot")
    DiagramContent(sbt.IO.read(dotFile))
  }

  /**
   * https://github.com/xuwei-k/sbt-class-diagram/blob/37c325d5b58f91a66b82b8bf5f538a6458570326/src/main/scala/Diagram.scala#L21-L29
   */
  def writeDotAndSvgFile(
    dir:      String,
    settings: DiagramSetting
  )(
    dotString: DiagramContent
  ): File = {
    import sys.process._
    val name = settings.name

    val dotFile = new File(dir, name + ".dot")
    sbt.IO.writeLines(dotFile, dotString.s :: Nil)

    if (settings.generateSvg) {
      val svgFile = new File(dir, name + ".svg")
      Seq("dot", "-o" + svgFile.getAbsolutePath, "-Tsvg", dotFile.getAbsolutePath).!
      svgFile
    } else
      dotFile

  }

  /**
   * https://github.com/xuwei-k/sbt-class-diagram/blob/37c325d5b58f91a66b82b8bf5f538a6458570326/src/main/scala/ClassNode.scala#L14-L49
   */
  private def produceDiagram(
    allClassNodes: Set[Node],
    setting:       DiagramSetting
  ): DiagramContent = {
    val quote = "\"" + (_: String) + "\""
    val map2string: (String, Map[String, String]) => String = {
      (title: String, map: Map[String, String]) =>
        if (map.isEmpty) ""
        else title + " " + map.map { case (k, v) => k + "=" + quote(v) }.mkString(" [", ", ", "]")
    }

    val nodes: List[String] = allClassNodes
      .filter(n => setting.filter(n.current))
      .map(n => quote(n.className) + map2string("", setting.nodeSetting(n.current)))
      .toList
      .sorted

    val edges: List[String] = setting
      .enabledConnectionTypes
      .toList
      .flatMap { ct =>
        (for {
          c <- allClassNodes
          p <- ct.getConnections(c)
          if setting.filter(p.current) && allClassNodes.contains(p)
        } yield quote(p.className) + " -> " + quote(c.className) +
          map2string("", ct.graphEdgeSettings ++ setting.edgeSetting(c.current, p.current)))
          .toList
          .reverse
          .distinct
      }

    DiagramContent(s"""digraph ${quote(setting.name)} {

${map2string("", setting.globalSettings)}

${map2string("node", setting.commonNodeSetting)}

${map2string("edge", setting.commonEdgeSetting)}

${nodes.mkString("\n")}

${edges.mkString("\n")}

}""")
  }

  case class DiagramSetting(
    name:                   String,
    globalSettings:         Map[String, String],
    commonNodeSetting:      Map[String, String],
    commonEdgeSetting:      Map[String, String],
    nodeSetting:            Class[_] => Map[String, String],
    edgeSetting:            (Class[_], Class[_]) => Map[String, String],
    filter:                 Class[_] => Boolean,
    enabledConnectionTypes: Set[ConnectionType],
    generateSvg:            Boolean,
    openFolder:             Boolean,
    openSvg:                Boolean
  )

  def defaultSettings(name: String): DiagramSetting =
    DiagramSetting(
      name = name,
      enabledConnectionTypes = Set(Inherit),
      globalSettings = Map.empty,
      commonNodeSetting = Map("shape" -> "box"),
      commonEdgeSetting = Map.empty,
      nodeSetting = _ => Map.empty,
      edgeSetting = (_, _) => Map.empty,
      filter = _ => true,
      generateSvg = true,
      openFolder = false,
      openSvg = false
    )
}
