package com.leobenkel.umlclassdiagram.internal

import com.leobenkel.umlclassdiagram.internal.ClassPath._
import com.leobenkel.umlclassdiagram.internal.Node._
import org.reflections.Reflections
import org.reflections.scanners.Scanners.SubTypes
import org.reflections.util.ConfigurationBuilder

//noinspection scala2InSource3
case class ClassPath(
  private val path: Seq[PackageName],
  private val leaf: Leaf
) {
  override def equals(obj: Any): Boolean =
    obj match {
      case c: ClassPath => this.toString equals c.toString
      case _ => false
    }

  override def hashCode(): Int = this.toString.hashCode

  lazy final override val toString: String = s"${path.toPath}.$leaf"

  def getClasses(classLoader: ClassLoader): Set[Class[_]] = leaf.getClassesWith(classLoader, path)
  lazy private val packageName:             String = path.toPath
  def isValid(n: Node):                     Boolean = n.className.startsWith(packageName)
}

//noinspection scala2InSource3
object ClassPath {
  def make(packageNames: String*)(leaf: Leaf): ClassPath =
    new ClassPath(packageNames.map(PackageName), leaf)

  case class PackageName(name: String) extends AnyVal

  implicit class PackagePath(packageNames: Seq[PackageName]) {
    lazy val toPath: String = packageNames.map(_.name).mkString(".")
  }

  sealed trait Leaf {
    final private[ClassPath] def getClassesWith(
      classLoader: ClassLoader,
      path:        Seq[PackageName]
    ): Set[Class[_]] =
      getClasses(classLoader, path)
        .filter(_.startsWith(path.toPath))
        .map {
          case c if c.endsWith("$") => c.substring(0, c.length - 1)
          case c                    => c
        }
        .flatMap(classLoader.loadClassSafely(_).toOption)

    protected def getClasses(
      classLoader: ClassLoader,
      path:        Seq[PackageName]
    ): Set[String]
  }

  case class ClassName(name: String) extends Leaf {
    require(name != "")
    require(name != null)
    require(!name.contains('*'))
    require(!name.contains('.'))

    lazy final override val toString: String = name
    override protected def getClasses(
      classLoader: ClassLoader,
      path:        Seq[PackageName]
    ): Set[String] = Set(classLoader.loadClass(s"${path.toPath}.$name").getName)
  }

  case object Wildcard extends Leaf {
    lazy final override val toString: String = "*"
    override protected def getClasses(
      classLoader: ClassLoader,
      path:        Seq[PackageName]
    ): Set[String] = {
      import scala.collection.JavaConverters._

      val r = new Reflections(
        new ConfigurationBuilder().forPackage(
          path.toPath,
          classLoader,
          ClassLoader.getSystemClassLoader
        )
      )

      r.getAll(SubTypes).iterator().asScala.toSet
    }
  }
}
