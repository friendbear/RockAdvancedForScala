    package typesystem

/**
  * A Taste of Advanced Scala
  * Mastering the Type System
  *
  * - Type Members
  *   {{{
  *     class Animal
  *     class Dog extends Animal
  *     class Cat extends Animal
  *
  *     class AnimalCollection {
  *       type AnimalType // abstract type member
  *       type BoundedAnimal <: Animal
  *       type SuperBoundedAnimal >: Dog <: Animal
  *       type AnimalC = Cat
  *     }
  *
  *     trait MList {
  *       type A
  *       head: A
  *       def tail: MList
  *     }
  *
  *     trait ApplicableToNumbers {
  *       type A <: Number
  *     }
  *     // NOT-OK
  *     class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers   {
  *       type A = String
  *       def head = hd
  *       def tail = tl
  *     }
  *
  *     // OK
  *     class IntList(hd: Int, tl: IntList) extends MList {
  *       type A = Int
  *       def head = hd
  *       def tail = tl
  *     }
  *   }}}
  */
object TypeMembers extends App{

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
  val dog: ac.AnimalType = ???

  // val cat: ac.BoundedAnimal = new Cat //compile error

  val pup: ac.SuperBoundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  // alternative to generics
  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int // override
    def add(element: Int): MyList = ???
  }

  // .type
  type CatsType = cat.type
  val newCat: CatsType = cat
  // new CatsType // Abstract Members(Type member)

  /*
    Exercise - enforce a type to be applicable to SOME TYPES only
   */
  // LOCKED
  trait MList {
    type A
    def head: A
    def tail: MList
  }

  // Number
  // type members and type member constraints(bounds) 🔴
  trait ApplicableToNumbers {
    type A <: Number
  }
  // NOT-OK
  /*
  class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers {
    type A = String
    def head = hd
    def tail = tl
  }
  */

  // OK
  class IntList(hd: Int, tl: IntList) extends MList {
    type A = Int
    def head = hd
    def tail = tl
  }
}
