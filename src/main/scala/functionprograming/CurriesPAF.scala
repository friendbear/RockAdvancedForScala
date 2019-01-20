package functionprograming

/**
  * A Taste of Advanced Scala
  * AdvancedFunctional Programing
  *
  * - Curring and Partially Applied Function
  */
object CurriesPAF extends App {

  { // Important!  🔴
    def add(x: Int, y: Int) =
      x + y

    def adder(x: Int)(y: Int) =
      x + y

    val addFunc = (x: Int, y: Int) =>
      x + y

    val superAdderOrg: Int => (Int => Int) = (x: Int) => (y: Int) => x + y
    // equal
    val superAdder: Int => Int => Int = // Higher order function 🔴
      x => y => x + y

    // curried functions
    val superAdder2: Int => Int => Int = new Function2[Int, Int, Int] {
      override def apply(x: Int, y: Int): Int = x + y
    } curried

    lazy val add3 = superAdder(3) // Int => Int => y => 3 + y
    println(add3(5)) // Int => 3 + 5
    println(superAdder(3)(5)) // curried function
  }
  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  val add4: Int => Int = curriedAdder(4) // (Int => Int) Noting Compile error
  // lifting = ETA-EXPANSION

  // functions != methods (JVM limitation)
  def inc(x: Int) = x + 1
  List(1,2,3).map(inc)
  List(1,2,3).map(x =>inc(x)) // ETA-expansion

  // PartialFunction function applications
  val add5 = curriedAdder(5) _ // Int => Int

  // EXERCISE
  {
    val simpleAddFunction = (x: Int, y: Int) => x + y

    def simpleAddMethod = (x: Int, y: Int) => x + y

    def curriedAddMethod(x: Int)(y: Int) = x + y

    // add7: Int => Int => y => 7 + y
    // as many different implementations of add7 using the above
    // be creative!
    lazy val add7 = (x: Int) => simpleAddFunction(7, x) // simplest
    lazy val add7_2 = simpleAddFunction.curried(7)
    lazy val add7_6 = simpleAddFunction(7, _: Int) // works as well
    lazy val add7_3 = curriedAddMethod(7) _ // PAF
    lazy val add7_4 = curriedAddMethod(7)(_) // PAF = alternative syntax
    lazy val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values
    // y => simpleAddMethod(7, y)

    println(add7(3))
  }

  // underscores are powerful
  {
    def concatenator(a: String, b: String, c: String): String = a + b + c

    // x: String => concatenator(hello, x, how are you?)
    val insertName = concatenator("Hello, Im ", _: String, ", how are you?")
    println(insertName("Tomi"))

    val fillInTheBlanks = concatenator("Hello, Im ", _: String, _: String)
    println(fillInTheBlanks("Tomi", "Scala is awesome!"))
  }
  // EXERCISES
  /*
    1. Process a list of numbers and return their string representations with different formats
      Use the %4.2f, %8.6f and %14.12f with a curried formatter function.
   */
  {
    def curriedFormatter(s: String)(number: Double): String = s.format(number)

    val number = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

    val simpleFormat = curriedFormatter("%4.2f") _
    val seriousFormat = curriedFormatter("%8.6f") _
    val preciseFormat = curriedFormatter("14.12f") _

    println(number.map(simpleFormat))
    println(number.map(seriousFormat))
    println(number.map(preciseFormat))
    println(number.map(curriedFormatter("%14.12f") _))
    println(number.map(curriedFormatter("%14.12f")))
  }
  /*
    2. difference between
        - function vs methods
        - parameters: by-name vs 0-lambda
   */
  /*
    calling byName and byFunction
    - int
    - method
    - parentMethod
    - lambda
    - PAF
   */
  {
    def byName(n: => Int) = n + 1
    def method: Int = 42
    def parenMethod(): Int = 42
    def byFunction(f: () => Int) = f() + 1
    byName(23)
    byName(method)
    byName(parenMethod())
    // Not work byName(() => 42)
    byName((() => 42)())
    // Not work byName(parenMethod _)

    // Not work byFunction(45)
    // byFunction(method) // not ok !!!!!!! compiler does not do ETA-expansion!
    byFunction(parenMethod)
    byFunction(() => 46)
    byFunction(parenMethod _) // also works, but warning
  }
}
