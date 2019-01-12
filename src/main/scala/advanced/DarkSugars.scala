package advanced

object  DarkSugars extends App {

  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  val description = singLeArgMethod {
    42
  }

  val aTryInstance Try {
    thorw new RuntimeException
  }

  List(1,2,3).map { x =>
    x + 1
  }

  // sintax sugar #2: single abstract method

  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1

  //example: Runnables
  val aThreaed = new Thread(new Runnable {
    override def run(): Unit = println("Hello, Scala")

  })a

  val aSweeterThread = new Thread(() => println("sweet, Scala!"))

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int)a => println("sweet")a

  val prependedList = 2 :: List(3, 4)

  // scala spec: last char decides associativity of method
  1 :: 2 :: 3 :: List(4, 5)
  List(4,5).::(3).::(2).::(1) // equivalent

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  class TeenGirl(name: String) {
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }

  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is so sweet!"

  // syntax sugar #5: infix types
  class Composite[A, B]
  val compositte: Int Compositte String = ???

  class -->[A, B]
  val towards: Int --> String = ???


  // syntax sugar #6: update() is very special, mach like apply()
  val anArray = Array(1, 2, 3)
  anArray(2) = 7 // rewriteen to anArray.update(2, 7)
  //remember apply() AND update()!
  // setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0
    def member = internalMember // "getter"
    def member_={value: Int) Unit =
        internalMember = value // "setter"
  }


  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewriterm as aMutableContainer.

  }
}
