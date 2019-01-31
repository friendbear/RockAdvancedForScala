package typesystem

/**
  * A Taste of Advanced Scala
  * Mastering the Type System
  *
  * - Recursive Types and F-Bounded Polymorphism
  */
object FBoundedPolymorphism extends App {


  /*
    trait Animal {
      def breed: List[Animal]
    }
    class Cat extends Animal {
      override def breed: List[Animal] = ??? // List[Cat]
    }
    class Dog extends Animal {
      override def breed: List[Animal] = ??? // List[Cat]
    }

    // Solution 2 - FBP
    trait Animal[A <: Animal[A]] { // recursive type: F-Bounded Polymorphism
      def breed: List[Animal[A]]
    }

    class Cat extends Animal[Cat] {
      override def breed: List[Animal[Cat]] = ???
    }
    class Dog extends Animal[Dog] {
      override def breed: List[Animal[Dog]] = ???
    }

    trait Entity[E <: Entity[E]] // ORM
    class Person extends Comparable[Person] { // FBP
      override def compareTo(o: Person): Int = ???
    }
    class Crocodile extends Animal[Dog] {
      override def breed: List[Animal[Dog]] = ???
    }

    // Solution 3 - FBP + self-types
    trait Animal[A <: Animal[A]] { self: A =>
      def breed: List[Animal[A]]
    }

    class Cat extends Animal[Cat] {
      override def breed: List[Animal[Cat]] = ???
    }
    class Dog extends Animal[Dog] {
      override def breed: List[Animal[Dog]] = ???
    }

    trait Fish extends Animal[Fish]
    class Shark extends Fish {
      override def breed: List[Animal[Fish]] = List(new Cod) // wrond
    }
  */
  // Exercise

  // Solution 4 type classes!
  trait Animal
  trait CanBreed[A] {
    def breed(a: A): List[A]
  }

  class Dog extends Animal
  object Dog {
    implicit object DogsCanBreed extends CanBreed[Dog] {
      def breed(a: Dog): List[Dog] = List()
    }
  }
  implicit class CanBreedOps[A](animal: A) {
    def breed(implicit canBreed: CanBreed[A]): List[A] =
      canBreed.breed(animal)
  }

  /*
    new CanBreedOps[Dog](dog).breed(Dog.DogsCanBreed)
    implicit value to pass to bread: Dog.DogsCanBreed
   */
  val dog = new Dog
  dog.breed


}
