package com.leobenkel.umlclassdiagram.internal

import com.leobenkel.umlclassdiagram.internal.ClassPath._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class InputParserTest extends AnyFreeSpec with Matchers {
  "InputParse" - {
    "fail with empty" in {
      val r = InputParser("")
      r.isFailure shouldBe true
      r.errors.nonEmpty shouldBe true
      r.errors shouldBe Seq("Expected whitespace character")
    }

    "fail with one space" in {
      val r = InputParser(" ")
      r.isFailure shouldBe true
      r.errors.nonEmpty shouldBe true
      r.errors shouldBe Seq("Expected non-double-quote-space character")
    }

    "fail with one package name" in {
      val r = InputParser(" com")
      r.isFailure shouldBe true
      r.errors.nonEmpty shouldBe true
      r.errors shouldBe Seq("Expected '.'")
    }

    "fail with one package name and separator" in {
      val r = InputParser(" com.")
      r.isFailure shouldBe true
      r.errors.nonEmpty shouldBe true
      r.errors shouldBe Seq("Expected non-double-quote-space character", "Expected '*'")
    }

    "valid with wildcard" in {
      val r = InputParser(" com.leobenkel.*")
      r.isFailure shouldBe false
      r.isValid shouldBe true
      r.errors.isEmpty shouldBe true
      r.toEither.right.get shouldBe
        ClassPath(Seq(PackageName("com"), PackageName("leobenkel")), Wildcard)
    }

    "valid with className" in {
      val r = InputParser(" com.leobenkel.foo.Bar")
      r.isFailure shouldBe false
      r.isValid shouldBe true
      r.errors.isEmpty shouldBe true
      r.toEither.right.get shouldBe
        ClassPath(
          Seq(PackageName("com"), PackageName("leobenkel"), PackageName("foo")),
          ClassName("Bar")
        )
    }
  }
}
