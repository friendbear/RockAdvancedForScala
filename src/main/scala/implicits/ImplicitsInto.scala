package implicits

/**
  * A Taste of Advanced Scala
  * AdvancedFunctional Programing
  *
  * - Enter Implicits
  */
object ImplicitsInto extends App {

  // tuples
  /*
    implicit final class ArrowAssoc[A](private val self: A) extends AnyVal {
      @inline def -> [B](y: B): Tuple2[A, B] = Tuple2(self, y)
      def →[B](y: B): Tuple2[A, B] = ->(y)
    }
   */
  var pair = "Daniel" -> "555"
  val intPair = 1 -> 2

  case class Person(name: String) {
    def greet = s"Hi, my name is $name!"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Peter".greet)  // println(fromStringToPerson("Peter").greet)

  /* Compile Error
  class A {
    def greet: Int = 2
  }
  implicit def fromStringToA(str: String): A = new A
  */
  def increment(x: Int)(implicit amount: Int) = x + amount
  implicit val defaultAmount = 10

  increment(2)
  // NOT default argument

}
