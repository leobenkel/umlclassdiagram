package com.leobenkel.umlclassdiagram.internal

import Node._
import scala.annotation.tailrec
import scala.util.Try

//noinspection scala2InSource3
class Node private (
  val current: Class[_],
  classLoader: ClassLoader
) {
  implicit private class FilteringClasses(input: Set[Class[_]]) {
    lazy val noCurrent: Set[Class[_]] = input.filterNot(_ === current)
    lazy val noStdLib: Set[Class[_]] = input.filterNot { c =>
      c.getName.startsWith("scala.") || c.getName.startsWith("java.")
    }
    lazy val filtered: Set[Class[_]] = noCurrent.noStdLib
  }

  override def equals(obj: Any): Boolean =
    obj match {
      case Node(c) => c === this.current
      case _       => false
    }

  lazy private val currentStatic: Option[Class[_]] = classLoader
    .loadClassSafely(current.getName + "$")
    .toOption

  lazy val className: String = current.getName

  lazy private val directParentClass: Set[Class[_]] = Option(current.getSuperclass)
    .map(Set[Class[_]](_))
    .getOrElse(Set.empty)
  lazy private val directParentClassForStatic: Set[Class[_]] = currentStatic
    .flatMap(c => Option[Class[_]](c.getSuperclass))
    .map(Set[Class[_]](_))
    .getOrElse(Set.empty)
    .filterNot(_ == null)
  lazy private val directInterfaces: Set[Class[_]] = current.getInterfaces.toSet
  lazy private val directInterfacesForStatic: Set[Class[_]] = this
    .currentStatic
    .map(_.getInterfaces.toSet)
    .getOrElse(Set.empty)
    .filterNot(_ == null)

  lazy private val allInterfaces: Set[Class[_]] = {
    def loop(
      current: Class[_],
      acc:     Set[Class[_]] = Set.empty
    ): Set[Class[_]] = {
      val interfaces: Seq[Class[_]] = current.getInterfaces
      if (interfaces.isEmpty) acc
      else interfaces.flatMap(i => loop(i, acc + i)).toSet
    }

    loop(this.current) ++ this.currentStatic.map(loop(_)).getOrElse(Set.empty)
  }

  lazy private val allParentClasses: Set[Class[_]] = {
    @tailrec
    def loop(
      current: Class[_],
      acc:     Set[Class[_]] = Set.empty
    ): Set[Class[_]] = {
      val newAcc: Set[Class[_]] = acc + current
      Option(current.getSuperclass) match {
        case Some(p) => loop(p, newAcc)
        case None    => newAcc
      }
    }

    loop(this.current) ++ this.currentStatic.map(loop(_)).getOrElse(Set.empty)
  }

  lazy val allParentClassAndInterfaces: Set[Class[_]] = (allInterfaces ++ allParentClasses).filtered
  lazy val allInterfacesFiltered:       Set[Class[_]] = allInterfaces.filtered
  lazy val allParentClassesFiltered:    Set[Class[_]] = allParentClasses.filtered
  lazy val allParentAndTraitFiltered:   Set[Class[_]] = allParentClassAndInterfaces.filtered
  lazy val allImmediateParents: Set[Class[_]] =
    (directInterfaces ++ directInterfacesForStatic ++ directParentClassForStatic ++
      directParentClass).filtered

  lazy val parents: Set[Node] = allImmediateParents.map(Node(_, classLoader))

  lazy override val toString: String = s"N(${current.getName})"
}

//noinspection scala2InSource3
private[umlclassdiagram] object Node {
  def unapply(n: Node): Option[Class[_]] = Some(n.current)

  def apply(
    s:           Class[_],
    classLoader: ClassLoader
  ): Node = {
    val sterile = classLoader.loadClass(s.noDollarTrim)
    new Node(sterile, classLoader)
  }

  implicit class ForClasses(c: Class[_]) {
    lazy private val name: String = c.getName

    def ===(o: Class[_]): Boolean = o.getName.filterNot(_ == '$') == name.filterNot(_ == '$')

    lazy val noDollarTrim: String =
      if (name.endsWith("$")) name.substring(0, name.length - 1) else name
  }

  implicit class SafeLoader(cl: ClassLoader) {
    def loadClassSafely(s: String): Try[Class[_]] = Try(cl.loadClass(s))
  }

  implicit val ordering: Ordering[Node] = (x: Node, y: Node) =>
    x.current.getName.compare(y.current.getName)
}
