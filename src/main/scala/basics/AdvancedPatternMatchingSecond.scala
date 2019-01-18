package basics

import basics.AdvancedPatternMatching.Person

/**
  * A Taste of Advanced Scala
  *
  * - Advanced Pattern Matching,Part1 : The Scala Basics
  */
object AdvancedPatternMatchingSecond extends App {
  val numbers = List(1)

  // infix patterns
  case class Or[A, B](a: A, b: B)
  val either = Or(2, "two")
  val humanDescription = either match {
    case Or(number, string) => s"$number is writtern as $string"
  }
  println(humanDescription)

  // decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }
  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty[A])
      else unapplySeq(list.tail).map(list.head +: _) // 🔵
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1, 2"
    case _ => "something else"
  }
  println(decomposed)

  // custom return types for unapply
  // isEmpty: Boolean, get: something.
  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  val bob = new Person("Bob", 25)
  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get = person.name
    }
  }

  println(bob match {
    case PersonWrapper(n) => s"This persons name is $n"
    case _ => "An alien"
  })

}
