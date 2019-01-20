package functionprograming

/**
  * A Taste of Advanced Scala
  *
  * Partial Functions : Advanced Functional Programming
  *
  * trait PartialFunction[-A, +B] extends (A => B) {
  *   def apply(x: A): B
  *   def isDefinedAt(x: A): Boolean
  * }
  * Utilities:
  * - isDefinedAt
  * - lift
  * - orElse
  */
object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) => {
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  }
  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  // ( 1, 2, 5) => Int

  // PartialFunction[Int, Int] = Function1(Int): Int
  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value
  println(aPartialFunction(2))
  // println(aPartialFunction(55842) => match error

  // PF utilities
  println(aPartialFunction.isDefinedAt(67))

  // lift => Options
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))
  println(lifted(98))

  // orElseを使った合成処理
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }
  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal functions
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }
  // HOFs accept partial functions as well
  val aMappedList = List(1,2,3).map {
    case 1 => 42
    case 2 => 76
    case 3 => 1000
  }
  println(aMappedList)

  /*
   * Note: PF can only have ONE Parameter type
   */
  /**
    * Exercises
    */
  // 1 - construct a PF instance yourself (anonymous class)
  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 65
      case 3 => 1000
    }
    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 3
  }

  // 2 - dumb chatBot as a PF
  val chatBot: PartialFunction[String, String] = {
    case "hello" => "Hi, my name is HAL9000"
    case "goodbye" => "Once you start toking to me, ther is no return, human!"
    case "call mom" => "Unable to find your phone without your credit card"
  }

  // PF をmapすることで、置換もできる
  //scala.io.Source.stdin.getLines().foreach(line => println("you said: " + line)) =>
  // scala.io.Source.stdin.getLines().foreach(line => println("chatBot says: " + chatBot(line)))
  //scala.io.Source.stdin.getLines().map(chatBot).foreach(println)
  val pf: PartialFunction[String, String] = {
    case "1" => "1 select"
    case "2" => "2 select"
    case "3" => "3 select"
  }
  scala.io.Source.stdin.getLines().map(pf).foreach(println)
}
