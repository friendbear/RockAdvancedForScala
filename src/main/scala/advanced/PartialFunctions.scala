package advanced

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) => {
    if (x == 1) 42
    //else throw new FunctionNotApplicableException
  }

  // ( 1, 2, 5) => Int

  // PartialFunction[Int, Int] = Function1(Int): Int
  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  println(aPartialFunction(2))

  // PF utilities
  // 結果が戻るかどうかを事前に確認する
  println(aPartialFunction.isDefinedAt(67))

  // lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))
  println(lifted(98))

  // orElseを使った合成処理
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }
  println(pfChain(2))
  println(pfChain(45))


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
    * 1 - construct a PF instance yourself (annoymous class)
    * 2 - dumb chatbot as a PF
    */
  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 65
      case 5 => 999
    }
    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 5
  }

  val chatbot: PartialFunction[String, String] = {
    case "hello" => "Hi, my nameis HAL9000"
    case "goodbye" => "Once you start toking"
    case "call mom" => "good morning"
  }

  // PF をmapすることで、置換もできる
  scala.io.Source.stdin.getLines().map(chatbot).foreach(println)
}
