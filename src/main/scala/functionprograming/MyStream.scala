package functionprograming


/**
  * Lazy Evaluation Exercise: A Potentially Infinite Stream
  *
  *
  * Exercise: implement a lazily evaluated, singly linked STREAM of elements.
  *
  * naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!
  * naturals.take(100).foreach(println) // lazily evaluate stream of the first 100 naturals
  * naturals.foreach(println) // will crash - infinite!
  * naturals.map(_ * 2) // stream of all even numbers (potentally infinite)
  */
abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B] // prepend operator
  def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams
  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // takes the first n elements out of this stream
  def takeAsList(n: Int): List[A]

}
object MyStream {
  def from[A](start: A)(generator: A => A) = ???

}
