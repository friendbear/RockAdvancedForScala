package advanced

object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3)
  println(add3(5))
  println(superAdder(3)(5)) // curried function

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  val add4: Int => Int = curriedAdder(4)
  // lifting = ETA-EXPANSION

  // functions != methods (JVM limitation)
  def inc(x: Int) = x + 1
  List(1,2,3).map(x =>inc(x))

  // Parthial function applications
  val add5 = curriedAdder(5) _ // Int => Int

  // EXERCISE
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simleAddMethod = (x: Int, y: Int) => x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7:
  val add7 = (x: Int) => simpleAddFunction(7, x)
  val add7_2 = simpleAddFunction.curried(7)
  val add7_6 = simpleAddFunction(7, _: Int) // works as well
  val add7_3 = curriedAddMethod(7) _  // PAF
  val add7_4 = curriedAddMethod(7)(_) // PAF

  //val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values

  // underscores are powerful
  //
  def concatenator(a: String, b: String, c: String): String = a + b + c

  val insertName = concatenator("Hello, Im", _: String, ", how are you?")
  println(insertName("Tomi"))

  val fillInThe = concatenator("Hello, Im", _: String, _: String)
  println(fillInThe("Tomi", "yei!"))

  // EXERCISES
  // 1.
  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleformat = curriedFormatter("%4.2f") _ // lift
  val seriusFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(preciseFormat))
  //
  println("%4.2f".format(Math.PI))

  /*
   * 2. difference between
   * - functions vs methods
   *- parameters: By name vs 0.lambda
   */
  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def paranMethod(): Int = 42

  /*
   * calling byName and
   */

  byName(23) // ok
  byName(method) // ok
  byName(paranMethod())
  byName(paranMethod)
  // byName(() => 42) // not ok
  // byName(() => 42()) // ok TODO:Dose not work
  //byName(paranMethod _) // not ok
  //
  // byFunction(45) // not ok
  // byFunction(method) // not ok!!!!!! does not do ETA-expansion!!
  // byFunction(paranMethod) // compiler does ETA-expansion deprecated.
  byFunction(() => 46) // works
  byFunction(paranMethod _ ) // also works
}
