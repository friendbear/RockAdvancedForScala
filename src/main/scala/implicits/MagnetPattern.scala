package implicits

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * A Taste of Advanced Scala
  * AdvancedFunctional Programing
  *
  * - A Type Class Use Case / The Magnet Pattern
  *   {{{
  *     trait HandleMagnet {
  *       def apply(): Unit
  *     }
  *     def handle(magnet: HandleMagnet) = magnet()
  *
  *     implicit class StringHandle(s: => String) extends HandleMagnet {
  *     override def apply(): Unit = {
  *       println(s)
  *       println(s)
  *     }
  *   }
  *
  *   def sideEffectMethod(): String = {
  *     println("Hello Scala")
  *     "hahaha"
  *   }
  *   handle(sideEffectMethod()) // =>
  *   handle {
  *     println("Hello, Scala")
  *     "magnet"
  *   }
  *   }}}
  */
object MagnetPattern extends App {

  // MagnetPattern is method overloading
  class P2PRequest
  class P2PResponse
  class Serializer[T]
  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T : Serializer](message: T): Int
    def receive[T : Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest])
    // def receive(future: Future[P2PResponse]): Int => Generics type compile error
    // lots of overloads
  }

  /* Troubles
    1 - type erasure
    2 - lifting doesn't work for all overloads

      val receiveFV = receive _ // ?!

    3 - code duplication
    4 - type interrence and default args

      actor.receive(?!)
   */

  // Magnet Pattern (Type Parameter) 🔴
  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet.apply()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling a P2P request
      println("Handling P2P request")
      42
    }
  }
  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling a P2P response
      println("Handling P2P response")
      24
    }
  }
  // call a Magnet Pattern
  val test1 = {
    receive(new P2PRequest)
    receive(new P2PResponse)
  }

  // 1 - no more type erasure problems!
  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    def apply(): Int = 2
  }
  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    def apply(): Int = 3
  }

  val test2 = {
    println(receive(Future(new P2PRequest)))
    println(receive(Future(new P2PResponse)))
  }

  // 2 - lifting works
  trait MathLib {
    def add1(x: Int) = x + 1
    def add1(x: String) = x.toInt + 1
  }
  // "magnetize"
  trait AddMagnet {
    def apply(): Int // concrete not Type Parameter 🔴
  }
  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }
  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  val test3 = {
    val addFV = add1 _
    println(addFV(1))
    println(addFV("3"))
    val receiveFV = receive _ // => MessageMagnet[Noting]
  }

  /*
    Drawbacks
    1 - verbose
    2 - harder to read
    3 - you can't name or place default arguments
    4 - call by name  doesn't work correctly
    (exercise: prove it!) (hint; side effects
   */
  class Hander {
    def handle(s: => String): Unit = {
      println(s)
      println(s)
    }
  }
  trait HandleMagnet {
    def apply(): Unit
  }
  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello Scala")
    "hahaha"
  }
  handle(sideEffectMethod()) // =>
  handle {
    println("Hello, Scala")
    "magnet"
  }


}
