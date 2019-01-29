package implicits

import java.{util => ju}

/**
  * A Taste of Advanced Scala
  * AdvancedFunctional Programing
  *
  * - Scala <> Java conversions
  *   - collection.JavaConverters._
  */
object ScalaJavaConversions extends App {

  import collection.JavaConverters._

  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()

  val test1 = {
    1 to 5 foreach javaSet.add
    println(javaSet)
  }

  val scalaSet = javaSet.asScala

  /*
  　Iterator
    Iterable
    ju.List - scala.mutable.Buffer
    ju.Set - scala.mutable.Set
    ju.Map - scala.mutable.Map
   */
  import collection.mutable._
  val numbersBuffer = ArrayBuffer[Int](1, 2, 3)
  val juNumbersBuffer = numbersBuffer.asJava

  val numbers = List(1, 2, 3)
  val juNumbers = numbers.asJava
  val backToScala = juNumbers.asScala

  val test2 = {
    println(juNumbersBuffer.asScala eq numbersBuffer)
    println(backToScala eq numbers) // false
    println(backToScala == numbers) // true
  }
  /*
    Exercise
    create a Scala-Java Optional-Option
        .asScala
   */
  class ToScala[T](value: => T) {
    def asScala: T = value
  }
  implicit def asScalaOptional[T](o: ju.Optional[T]): ToScala[Option[T]] = new ToScala[Option[T]](
    if (o.isPresent) Some(o.get) else None
  )

  val test3 ={
    val juOptional: ju.Optional[Int] = ju.Optional.of(2)
    val scalaOption = juOptional.asScala
    println(scalaOption)
  }
}
