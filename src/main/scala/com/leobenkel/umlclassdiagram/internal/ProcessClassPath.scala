package com.leobenkel.umlclassdiagram.internal

//noinspection scala2InSource3
object ProcessClassPath {
  def apply(input: ClassPath)(classLoader: ClassLoader): Set[Node] =
    input.getClasses(classLoader).map(Node.apply(_, classLoader))
}
