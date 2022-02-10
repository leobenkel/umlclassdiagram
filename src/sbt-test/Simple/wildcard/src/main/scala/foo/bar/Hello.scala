package foo.bar

import foo.aaa.Bbb

object Main extends App {
  println(s"hello ${World("World")} and ${World.Thing(Bbb.ccc)}")
}
