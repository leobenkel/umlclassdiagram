package com.leobenkel.umlclassdiagram.internal

import com.leobenkel.umlclassdiagram.internal.ClassPath._
import sbt.complete.DefaultParsers._
import sbt.complete.Parser
import sbt.internal.util.complete.{ParserMain, Parsers}
import scala.util.parsing.combinator.syntactical.StandardTokenParsers

object InputParser {
  private val forbiddenChar: Seq[Char] = Seq('.', '*')
  private def filterForbiddenChar(input: Parser[String]): Parser[String] =
    input.filter(
      s => forbiddenChar.forall(!s.contains(_)),
      s => s"The input '$s' should not contains any of: ${forbiddenChar.mkString("[", ", ", "]")}"
    )

  lazy private val packageName: Parser[PackageName] = token(
    filterForbiddenChar(NotQuoted),
    "<package>"
  ).map(PackageName)
  lazy private val className: Parser[Leaf] = token(filterForbiddenChar(NotQuoted), "<class>")
    .map(ClassName)
  lazy private val separator: Parser[Char] = literal('.')
  lazy private val wildCard:  Parser[Leaf] = literal('*').map(_ => Wildcard)

  lazy private val packageNames: Parser[Seq[PackageName]] = (packageName <~ separator).+
  lazy private val lastBlock:    Parser[Leaf] = className | wildCard
  lazy private val fullPackageName: Parser[ClassPath] = (packageNames ~ lastBlock).map {
    case (p, l) => ClassPath(p, l)
  }

  lazy val root: sbt.complete.Parser[Set[ClassPath]] = ((Space ~> fullPackageName).+).map(_.toSet)

  def apply(input: String): Parser.Result[Set[ClassPath]] =
    sbt.complete.Parser.apply(root)(input).resultEmpty
}
