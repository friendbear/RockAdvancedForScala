package typesystem

/**
  * A Taste of Advanced Scala
  * Mastering the Type System
  *
  * - Structural Types and Compile-Time Duck Typing
  */
object StructuralTypes extends App {

  // structural types
  type JavaCloseable = java.io.Closeable
  class HipsterCloseable {
    def close() = println("yeah yeah I'm closing")
    def closeSilently(): Unit = println("not make sounds")
  }

  // def closeQuietly(closeable: JavaCloseable OR HipsterCloseable) // ?!
  type UnifiedCloseable = {
    def close(): Unit
  } // STRUCTURAL TYPE
  def closeQuietly(unifiedCloseable: UnifiedCloseable): Unit = unifiedCloseable.close()

  closeQuietly(new JavaCloseable {
    override def close(): Unit = println("JavaCloseable close")
  })
  closeQuietly(new HipsterCloseable)


  // TYPE REFINEMENTS java Closeable + closeSilently
  type AdvancedCloseable = JavaCloseable {
    def closeSilently(): Unit
  }
  type AdvancedCloseable2 = java.io.Closeable {
    def closeSilently(): Unit
  }
  class AdvancedJavaCloseable extends JavaCloseable {
    override def close(): Unit = println("Java closes")
    def closeSilently(): Unit = println("Java closes silently") // 🔴
  }

  def closeShh(advCloseable: AdvancedCloseable): Unit = advCloseable.closeSilently()

  closeShh(new AdvancedJavaCloseable)
  // closeShh(new HipsterCloseable) => compile error

  // using structural types as standalone types
  // oun type 🔴 this is same UnifiedCloseable
  def altClose(closeable: {def close(): Unit}): Unit = closeable.close()
  def altCloseSilently(closeable: {def closeSilently(): Unit}): Unit = closeable.closeSilently()

  altClose(new HipsterCloseable)
  altCloseSilently(new HipsterCloseable)

  // type-checking => dock typing
  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark!")
  }
  class Car {
    def makeSound(): Unit = println("yroooom!")
  }

  // static duck typing 🔴
  /*
    Postfix operator notation: val s = 123 toString
    Reflective call: def foo(v: {def bar()}) = v.bar()
    Dynamic member selection: class Foo extends Dynamic
    Implicit conversion: implicit def toInt(s: String) = s.toInt
    Higher-kinded type: class Bar[M[A]]
    Existential type: def foo(v: Seq[T] forSome { type T })
    Macro definition: def assert(s: String) = macro Asserts.assertImpl
   */
  val doc: SoundMaker = new Dog // runtime structure type use reflection
  val car: SoundMaker = new Car

  // CAVEAT: based on reflection

  /*
    Exercises
   */
  // 1.
  trait CBL[+T] {
    def head: T
    def tail: CBL[T]
  }

  class Human {
    def head: Brain = new Brain
  }
  class Brain {
    override def toString: String = "BRAINZ!"
  }

  def f[T](somethingWithAHead: {def head: T}): Unit = println(somethingWithAHead.head)

  /*
    f is compatible with a CBL and with a Human? yes.
   */

  case object CBNil extends CBL[Nothing] {
    def head: Nothing = ???
    def tail: CBL[Nothing] = ???
  }
  case class CBCons[T](override val head: T, override val tail: CBL[T]) extends CBL[T]

  f(CBCons(2, CBNil))
  f(new Human) // ?! T = Brain !!

  // 2.
  object HeadEqualizer {
    type Headable[T] = { def head: T }
    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
  }

  /*
    is compatible with a CBL and with a Human? Yes.
   */
  val brainzList = CBCons(new Brain, CBNil)
  val stringsList = CBCons("Brainz", CBNil)

  HeadEqualizer.==(brainzList, new Human)

  HeadEqualizer.==(new Human, stringsList) // not type safe
}
