package advanced

object LazyEvaluation extends App {

  lazy val x: Int = {
    println("hello")
    42
  }
  println(x)
  println(x)

  // examples of implications:
  // side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no")

  // in conjuction with call by name
  def byNameMethod(n: => Int): Int = {
    // CALL BY NEED
    lazy val t = n // only evaluated once
    t + t + t + t + 1

  }

  def retrieveMagicValue = {
    // side effect or a long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue))

  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is grater than 30?")
    i < 30
  }
  // filtering with lazy vals
  def graterThan20(i: Int): Boolean = {
    println(s"$i is grater than 20?")
    i > 20
  }


  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) // List(1, 25, 5, 23)
  val gt20 = lt30.filter(graterThan20)  // List(25, 23)

  println(gt20)

  val it30Lazy = numbers.withFilter(lessThan30) // lazy vals under the head
  val gt20Lazy = it30Lazy.withFilter(graterThan20)
  gt20Lazy.foreach(println)

  // for=comprehensions use withFilter whit guards
  //
  for {
    a <- List(1, 2, 3) if a %2 == 0 // use lazy vals!
  } yield a + 1

  List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

  /**
   * Exerclise: implement a lazlly evaluated, singly linked STREAM o elements.
   *
   * naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!
   * naturals.take(100).foreach(println) // lazily evaluate stream of the first 100 naturals
   * naturals.take(100) // lazily evaluated stream of the first 100 naturals (finite stream)
   * naturals.map(_ * 2) // stream of all even numbers (potentally infinite)
   */
  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] // prepend operator
    def ++[B >: A](anoterStream: MyStream[B]): MyStream[B] // concatenate two streams
    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elements out of this stream
    def takeAsList(n: Int): List[A]

  }
  object MyStream {


  }
}
