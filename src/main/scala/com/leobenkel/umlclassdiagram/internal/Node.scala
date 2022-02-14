package com.leobenkel.umlclassdiagram.internal

import com.leobenkel.umlclassdiagram.internal.Node._
import java.lang.reflect._
import scala.reflect.ClassTag
import scala.util.Try

//noinspection scala2InSource3
class Node private (
  val current: Class[_],
  classLoader: ClassLoader
) {
  import Node.Utils._

  implicit private class FilteringClasses(input: Set[Class[_]]) {
    lazy val noCurrent: Set[Class[_]] = input.filterNot(_ === current)
    lazy val noStdLib: Set[Class[_]] = input.filterNot { c =>
      c.getName.startsWith("scala.") || c.getName.startsWith("java.")
    }
    lazy val filtered: Set[Class[_]] = noCurrent.noStdLib

    lazy val toNodes: Set[Node] = input.flatMap(_.toNode)
  }

  implicit private class ClassExtra(c: Class[_]) {
    lazy val toNode: Option[Node] = Try(Node(c, classLoader)).toOption
  }

  override def equals(obj: Any): Boolean =
    obj match {
      case Node(c) => c === this.current
      case _       => false
    }

  override def hashCode(): Int = this.current.getName.hashCode

  lazy private val currentStatic: Option[Class[_]] = classLoader
    .loadClassSafely(current.getName + "$")
    .toOption

  lazy val className: String = current.getName

  lazy private val directParentClass: Set[Class[_]] = current
    .getSuperclass
    .safe
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
    def loop(
      current: Class[_],
      acc:     Set[Class[_]] = Set.empty
    ): Set[Class[_]] = {
      val newAcc: Set[Class[_]] = acc + current
      current.getSuperclass.safe.map(loop(_, newAcc)).getOrElse(newAcc)
    }

    loop(this.current) |+| this.currentStatic.map(loop(_))
  }

  lazy private val allParentClassAndInterfaces: Set[Class[_]] = (allInterfaces ++ allParentClasses)
    .filtered
  lazy private[internal] val allInterfacesFiltered:    Set[Class[_]] = allInterfaces.filtered
  lazy private[internal] val allParentClassesFiltered: Set[Class[_]] = allParentClasses.filtered
  lazy private[internal] val allParentAndTraitFiltered: Set[Class[_]] = allParentClassAndInterfaces
    .filtered
  lazy private[internal] val allImmediateParents: Set[Class[_]] =
    (directInterfaces ++ directInterfacesForStatic ++ directParentClassForStatic ++
      directParentClass).filtered

  lazy val parents: Set[Node] = allImmediateParents.map(Node(_, classLoader))

  lazy override val toString: String = s"N(${current.getName})"

  lazy private val methods: Set[Method] =
    current.getDeclaredMethods.toSet |++| currentStatic.map(_.getDeclaredMethods)

  lazy private val inputs:             Set[Class[_]] = methods.flatMap(_.getParameterTypes)
  lazy private val returns:            Set[Class[_]] = methods.map(_.getReturnType)
  lazy private val possibleExceptions: Set[Class[_]] = methods.flatMap(_.getExceptionTypes)

  lazy private val constructors: Set[Constructor[_]] =
    current.getConstructors.toSet |++| currentStatic.map(_.getConstructors)
  lazy private val classForConstruction: Set[Class[_]] = constructors.flatMap(_.getParameterTypes)
  lazy private val exceptionForConstruction: Set[Class[_]] = constructors
    .flatMap(_.getExceptionTypes)

  lazy private val fields: Set[Field] =
    current.getDeclaredFields.toSet |++| currentStatic.map(_.getDeclaredFields)

  lazy private val classForFields: Set[Class[_]] = fields.map(_.getType)

  lazy val enclosingClass: Option[Node] =
    (current.getEnclosingClass.safe orElse
      currentStatic.flatMap[Class[_]](_.getEnclosingClass.safe)).flatMap(_.toNode)

  lazy val pack: Option[Package] = current.getPackage.safe orElse
    currentStatic.flatMap(_.getPackage.safe)

  // TODO: Will not work for nested type like arrays

  lazy val producedType:   Set[Node] = (classForFields ++ returns).toNodes
  lazy val inputTypes:     Set[Node] = (classForConstruction ++ inputs).toNodes
  lazy val exceptionTypes: Set[Node] = (possibleExceptions ++ exceptionForConstruction).toNodes

  lazy val classInvolvedByThisClass: Set[Node] = producedType ++ inputTypes ++ exceptionTypes ++
    parents ++ enclosingClass
}

//noinspection scala2InSource3
private[umlclassdiagram] object Node {
  implicit val ordering: Ordering[Node] = (x: Node, y: Node) =>
    x.current.getName.compare(y.current.getName)

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

  private[Node] object Utils {

    implicit class SetWithOpt[A: ClassTag](s: Set[A]) {
      def ++(opt: Option[A]): Set[A] = this |+| opt.map(Set(_))

      def |+|(opt: Option[Set[A]]): Set[A] = {
        val optSet: Set[A] = opt.getOrElse(Set.empty)
        s ++ optSet
      }

      def |++|(opt: Option[scala.Array[A]]): Set[A] = {
        val optSet: Set[A] = opt.getOrElse(scala.Array.empty[A]).toSet
        s ++ optSet
      }
    }

    implicit class CouldBeNull[A](s: A) {
      def safe: Option[A] = Option(s)
    }
  }
}
