package implicits

/**
  * A Taste of Advanced Scala
  * AdvancedFunctional Programing
  *
  * - Ordering Implicits
  */
object OrganizingImplicits extends App {

  implicit def reverseOrdering(): Ordering[Int] = Ordering.fromLessThan(_ > _)
  implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1, 4, 5, 3, 2).sorted(normalOrdering))

  // scala.Predef

  /*
    Implicits: (used as implicit parameters):
      - val/var
      - object
      - accessor methods = defs with no parentheses
   */

  // Exercise
  case class Person(name: String, age: Int)

  val person = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

  object AlphabeticNameOrdering {
    implicit val alphabeticOrdering: Ordering[Person] =
      Ordering.fromLessThan((x, y) => x.name.compareTo(y.name) < 0)
  }
  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((x, y) => x.age < y.age)
  }

  /*
    Implicit scope
    - normal scope = LOCAL SCOPE
    - imported scope
    - companions of all types involved in the method signature 🔴
      - List
      - Ordering
      - all the types involved = A or any supertype
   */
  // def sorted[B >: A](implicit ord: Ordering[B]): List[B]
  import AgeOrdering._
  println(person.sorted) // => List(Person(Amy,22), Person(Steve,30), Person(John,66))

  /*
    Exercise.

    - totalPrice = most used (%50)
    - by unit count = 25%
    - by unit price = 25%
   */
  case class Purchase(nUnits: Int, unitPrice: Double)
  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((x, y) => x.nUnits * x.unitPrice > y.nUnits * y.unitPrice)
  }

  object UnitCountOrdering {
    implicit val unitCostOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }
  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }
}
