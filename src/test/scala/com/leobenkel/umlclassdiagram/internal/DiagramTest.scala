package com.leobenkel.umlclassdiagram.internal

import com.leobenkel.umlclassdiagram.internal.ClassPath.ClassName
import com.leobenkel.umlclassdiagram.internal.Diagram.DiagramContent
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class DiagramTest extends AnyFreeSpec with Matchers {
  private val classLoader: ClassLoader = getClass.getClassLoader

  "Diagram" - {
    "produce diagram" in {
      val diagram: DiagramContent = Diagram(
        ClassPath
          .make("com", "leobenkel", "umlclassdiagram", "testingBundle", "pack")(ClassName("C")),
        classLoader,
        Diagram.defaultSettings("test")
      )
//      println(diagram)

      diagram.s should include("pack.C")
      diagram.s should include("pack.Foo")
      diagram.s should include("pack.A")
      diagram.s should include("pack.B")
    }
  }
}
