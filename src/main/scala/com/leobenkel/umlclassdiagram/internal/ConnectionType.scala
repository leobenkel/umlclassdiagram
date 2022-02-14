package com.leobenkel.umlclassdiagram.internal

import ConnectionType._

sealed trait ConnectionType {
  def getConnections(n: Node): Set[Node]
  protected def direction:  Direction
  protected def style:      Style
  protected def arrowStyle: ArrowStyle

  lazy private val arrowHead: Map[String, String] = (direction match {
    case Directions.forward => Map("arrowhead" -> arrowStyle.toString)
    case Directions.back    => Map("arrowtail" -> arrowStyle.toString)
    case Directions.both =>
      Map(
        "arrowhead" -> arrowStyle.toString,
        "arrowtail" -> arrowStyle.toString
      )
  }) ++ Map("dir" -> direction.toString)

  lazy final val graphEdgeSettings: Map[String, String] = Map(
    "style" -> style.toString
  ) ++ arrowHead
}

object ConnectionType {

  // https://www.graphviz.org/pdf/dotguide.pdf

  sealed trait Direction
  private object Directions {
    case object forward extends Direction
    case object back extends Direction
    case object both extends Direction
  }

  sealed trait Style
  private object Styles {
    case object solid extends Style
    case object dashed extends Style
    case object dotted extends Style
    case object bold extends Style
  }

  sealed trait ArrowStyle
  private object ArrowStyles {
    case object normal extends ArrowStyle
    case object curve extends ArrowStyle
    case object box extends ArrowStyle
    case object crow extends ArrowStyle
    case object none extends ArrowStyle
    case object dot extends ArrowStyle
  }

  lazy val allConnectionTypes: Set[ConnectionType] =
    Set(Inherit, Produce, Consume, Throw, Enclosing)

  final case object Inherit extends ConnectionType {
    override def getConnections(n: Node): Set[Node] = n.parents

    lazy override protected val direction:  Direction = Directions.forward
    lazy override protected val style:      Style = Styles.bold
    lazy override protected val arrowStyle: ArrowStyle = ArrowStyles.normal
  }

  final case object Produce extends ConnectionType {
    override def getConnections(n: Node): Set[Node] = n.producedType

    lazy override protected val direction:  Direction = Directions.back
    lazy override protected val style:      Style = Styles.solid
    lazy override protected val arrowStyle: ArrowStyle = ArrowStyles.dot
  }

  final case object Consume extends ConnectionType {
    override def getConnections(n: Node): Set[Node] = n.inputTypes

    lazy override protected val direction:  Direction = Directions.forward
    lazy override protected val style:      Style = Styles.dotted
    lazy override protected val arrowStyle: ArrowStyle = ArrowStyles.curve
  }

  final case object Throw extends ConnectionType {
    override def getConnections(n: Node): Set[Node] = n.exceptionTypes

    lazy override protected val direction:  Direction = Directions.back
    lazy override protected val style:      Style = Styles.dashed
    lazy override protected val arrowStyle: ArrowStyle = ArrowStyles.crow
  }

  final case object Enclosing extends ConnectionType {
    override def getConnections(n: Node): Set[Node] =
      n.enclosingClass.map(Set(_)).getOrElse(Set.empty)

    lazy override protected val direction:  Direction = Directions.forward
    lazy override protected val style:      Style = Styles.bold
    lazy override protected val arrowStyle: ArrowStyle = ArrowStyles.box
  }
}
