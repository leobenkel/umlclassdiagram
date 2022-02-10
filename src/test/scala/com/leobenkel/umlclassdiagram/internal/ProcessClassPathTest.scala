package com.leobenkel.umlclassdiagram.internal

import com.leobenkel.umlclassdiagram.internal.ClassPath.{ClassName, Wildcard}
import com.leobenkel.umlclassdiagram.internal.Node._
import com.leobenkel.umlclassdiagram.plugin._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

//noinspection scala2InSource3
class ProcessClassPathTest extends AnyFreeSpec with Matchers {
  private val classLoader: ClassLoader = getClass.getClassLoader

  "ProcessClassPath" - {
    "With simple specific class" in {
      val r = ProcessClassPath(
        ClassPath.make("com", "leobenkel", "umlclassdiagram", "internal")(ClassName("ClassPath"))
      )(classLoader)

      val expected = Node(classOf[ClassPath], classLoader)

      r.nonEmpty shouldBe true
      r shouldEqual Set(expected)

      val c = r.head

      c.className shouldEqual "com.leobenkel.umlclassdiagram.internal.ClassPath"
      c.current shouldEqual classOf[ClassPath]
      c.allInterfacesFiltered shouldBe Set.empty
      c.allParentClassesFiltered shouldBe Set.empty
    }

    "With extended class" in {
      val r = ProcessClassPath(
        ClassPath
          .make("com", "leobenkel", "umlclassdiagram", "internal")(ClassName("ClassPath$ClassName"))
      )(classLoader)

      val expected = Node(classOf[ClassPath.ClassName], classLoader)

      r.nonEmpty shouldBe true
      r shouldEqual Set(expected)

      val c = r.head

      c.className shouldEqual "com.leobenkel.umlclassdiagram.internal.ClassPath$ClassName"
      c.current shouldEqual classOf[ClassPath.ClassName]
      c.allInterfacesFiltered shouldBe Set(classOf[ClassPath.Leaf])
      c.allParentClassesFiltered shouldBe Set.empty
    }

    "With package name" in {
      val r = ProcessClassPath(
        ClassPath.make("com", "leobenkel", "umlclassdiagram", "plugin")(Wildcard)
      )(classLoader)

      r.nonEmpty shouldBe true
      r shouldEqual
        Set(
          Node(UmlClassDiagramPlugin.getClass, classLoader)
          // TODO: No clue why this one is not showing up
          // Node(UmlClassDiagramKeys.getClass)
        )

      val c = r.head

      c.className shouldEqual "com.leobenkel.umlclassdiagram.plugin.UmlClassDiagramPlugin"
      c.current === UmlClassDiagramPlugin.getClass shouldBe true
      c.allInterfacesFiltered shouldBe Set.empty
      c.allParentClassesFiltered shouldBe Set(classOf[sbt.Plugins.Basic], classOf[sbt.AutoPlugin])
    }

    "With unknown package name" in {
      val r = ProcessClassPath(
        ClassPath.make("com", "foo", "bar")(Wildcard)
      )(classLoader)

      r.isEmpty shouldBe true
    }

    "With unknown class name" in
      assertThrows[ClassNotFoundException] {
        ProcessClassPath(
          ClassPath.make("com", "foo", "bar")(ClassName("unknown"))
        )(classLoader)
      }

    "With test package name" in {
      import com.leobenkel.umlclassdiagram.testingBundle.pack

      val r = ProcessClassPath(
        ClassPath.make("com", "leobenkel", "umlclassdiagram", "testingBundle", "pack")(Wildcard)
      )(classLoader)

      r.nonEmpty shouldBe true
      r.toList.sorted shouldEqual
        List[Node](
          Node(classOf[pack.A], classLoader),
          Node(classOf[pack.Ab], classLoader),
          Node(classOf[pack.B], classLoader),
          Node(classOf[pack.Bar], classLoader),
          Node(classOf[pack.C], classLoader),
          Node(classOf[pack.Foo], classLoader),
          Node(pack.O.getClass, classLoader)
        ).sorted

      r.map {
        case n @ Node(c) if c === classOf[pack.Bar] =>
          n.className shouldEqual "com.leobenkel.umlclassdiagram.testingBundle.pack.Bar"
          n.allInterfacesFiltered shouldEqual Set(classOf[pack.Foo], classOf[pack.A])
          n.allImmediateParents shouldEqual Set(classOf[pack.Ab], classOf[pack.A])
          n.allParentClassesFiltered shouldEqual Set(classOf[pack.Ab])
          n.allParentAndTraitFiltered shouldEqual
            Set(classOf[pack.Foo], classOf[pack.A], classOf[pack.Ab])
        case n @ Node(c) if c === pack.O.getClass =>
          n.className shouldEqual "com.leobenkel.umlclassdiagram.testingBundle.pack.O"
          n.allInterfacesFiltered shouldEqual Set(classOf[pack.B])
          n.allImmediateParents shouldEqual Set(classOf[pack.B])
          n.allParentClassesFiltered shouldEqual Set.empty
          n.allParentAndTraitFiltered shouldEqual Set(classOf[pack.B])
        case n @ Node(c) if c === classOf[pack.A] =>
          n.className shouldEqual "com.leobenkel.umlclassdiagram.testingBundle.pack.A"
          n.allInterfacesFiltered shouldEqual Set(classOf[pack.Foo])
          n.allImmediateParents shouldEqual Set(classOf[pack.Foo])
          n.allParentClassesFiltered shouldEqual Set.empty
          n.allParentAndTraitFiltered shouldEqual Set(classOf[pack.Foo])
        case n @ Node(c) if c === classOf[pack.B] =>
          n.className shouldEqual "com.leobenkel.umlclassdiagram.testingBundle.pack.B"
          n.allInterfacesFiltered shouldEqual Set.empty
          n.allImmediateParents shouldEqual Set.empty
          n.allParentClassesFiltered shouldEqual Set.empty
          n.allParentAndTraitFiltered shouldEqual Set.empty
        case n @ Node(c) if c === classOf[pack.C] =>
          n.className shouldEqual "com.leobenkel.umlclassdiagram.testingBundle.pack.C"
          n.allInterfacesFiltered shouldEqual
            Set(classOf[pack.Foo], classOf[pack.A], classOf[pack.B])
          n.allImmediateParents shouldEqual Set(classOf[pack.A], classOf[pack.B])
          n.allParentClassesFiltered shouldEqual Set.empty
          n.allParentAndTraitFiltered shouldEqual
            Set(classOf[pack.Foo], classOf[pack.A], classOf[pack.B])
        case n @ Node(c) if c === classOf[pack.Foo] =>
          n.className shouldEqual "com.leobenkel.umlclassdiagram.testingBundle.pack.Foo"
          n.allInterfacesFiltered shouldEqual Set.empty
          n.allImmediateParents shouldEqual Set.empty
          n.allParentClassesFiltered shouldEqual Set.empty
          n.allParentAndTraitFiltered shouldEqual Set.empty
        case n @ Node(c) if c === classOf[pack.Ab] =>
          n.className shouldEqual "com.leobenkel.umlclassdiagram.testingBundle.pack.Ab"
          n.allInterfacesFiltered shouldEqual Set.empty
          n.allImmediateParents shouldEqual Set.empty
          n.allParentClassesFiltered shouldEqual Set.empty
          n.allParentAndTraitFiltered shouldEqual Set.empty
      }
    }

    "Hierarchy" in {
      import com.leobenkel.umlclassdiagram.testingBundle.pack

      val r = ProcessClassPath(
        ClassPath
          .make("com", "leobenkel", "umlclassdiagram", "testingBundle", "pack")(ClassName("C"))
      )(classLoader)

      r.nonEmpty shouldBe true
      r.toList.sorted shouldEqual List[Node](Node(classOf[pack.C], classLoader))

      val n = r.head

      n.className shouldEqual "com.leobenkel.umlclassdiagram.testingBundle.pack.C"
      n.allInterfacesFiltered shouldEqual Set(classOf[pack.Foo], classOf[pack.A], classOf[pack.B])
      n.allImmediateParents shouldEqual Set(classOf[pack.A], classOf[pack.B])
      n.allParentClassesFiltered shouldEqual Set.empty
      n.allParentAndTraitFiltered shouldEqual
        Set(classOf[pack.Foo], classOf[pack.A], classOf[pack.B])

      val parents = n.parents.toList.sorted
      parents shouldEqual
        List[Class[_]](classOf[pack.A], classOf[pack.B]).map(Node(_, classLoader)).sorted

      val a = parents(0)
      val b = parents(1)

      a shouldEqual Node(classOf[pack.A], classLoader)
      b shouldEqual Node(classOf[pack.B], classLoader)

      a.parents.toList.sorted shouldEqual List[Node](Node(classOf[pack.Foo], classLoader))
      b.parents.isEmpty shouldEqual true
    }
  }
}
