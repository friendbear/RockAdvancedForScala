package basics

/**
  * A Taste of Advanced Scala
  *
  * - Advanced Pattern Matching,Part1 : The Scala Basics
  */
object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head.")
    case _ =>
  }

  /*
   * - constants
   * - wildcards
   * - case classes
   * - tuples
   * - some special magic like above
   */
  class Person(val name: String, val age: Int)
  object Person{
    def unapply(person: Person): Option[(String, Int)] =
      Some((person.name, person.age))
  }
  object PersonPattern {
    // under age 21
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)
  val greeting = bob match {
    case PersonPattern(n, a) => s"Hi, my name is $n and I am $a yo."
  }
  println(greeting)

  val legalStatus = bob.age match {
    case PersonPattern(status) => s"My legal status is $status"
  }
  println(legalStatus)


  /*
   * Exercise.
   */
  val n: Int = 45
  val mathProperty = n match {
    case x if x < 10 => "single digit"
    case x if x % 2 == 0 => "an even numbers"
    case _ => "no property"
  }

  // even
  object even {
    def unapply(arg: Int): Option[Boolean] =
      if (arg % 2 == 0) Some(true)
      else None
  }
  object singleDigit {
    def unapply(arg: Int): Option[Boolean] =
      if (arg > -10 && arg < 10) Some(true)
      else None
  }
  val n2: Int = 8
  val mathProperty2 = n2 match {
    case even(_) => "single digit"
    case singleDigit(_) => "an even numbers"
    case _ => "no property"
  }
  println(mathProperty2)


}
