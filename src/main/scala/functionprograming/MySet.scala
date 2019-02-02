package functionprograming

/**
  * A Taste of Advanced Scala
  * AdvancedFunctional Programing
  *
  * - Functional Collections : A functional Set
  * - Enhancing A Functional Set
  * - A Functional Set, level 9000: A Potentially Infinite Set
  */
trait MySet[A] extends (A => Boolean) {

  def apply(elem: A): Boolean =  // (A => Boolean) implement. 🔴
    contains(elem)

  /*
   * EXERCISE #1
   */
  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A=> Unit): Unit

  /*
   * EXERCISE #2
   * removing an element
   * intersection whit another set
   * difference with another set
   */
  def -(elem: A): MySet[A]
  def --(anotherSet: MySet[A]): MySet[A]
  def &(anotherSet: MySet[A]): MySet[A]

  // EXERCISE #3
  // set[1, 2, 3] => implement a unary_! = NEGATION of a set
//todo  def unary_! :MySet[A]
}

class EmptySet[A] extends MySet[A] {

  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet
  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = this
  def foreach(f: A=>Unit): Unit = ()

  def -(elem: A): MySet[A] = this
  def --(anotherSet: MySet[A]): MySet[A] = this
  def &(anotherSet: MySet[A]): MySet[A] = this
  
//todo  def unary_! :MySet[A]  = new PropertyBasedSet[A](_ => true)
}

// all elements of type a which satisfy a property
// { x in A | property(x) }
/*todo
class PropertyBasedSet[A](property: => Boolean) extends MySet[A] {
  def contains(elem: A): Boolean = property(elem)

  // { x in A | property(x) } + element = { x in A | property(x) || x == element }
  def +(elem: A): MySet[A] = ???
//todo    new PropertyBasedSet[A](x => property(x) || x == elem)

  def ++(anotherSet: MySet[A]): MySet[A] = ???
//todo    new PropertyBasedSet[A](x => property(x) || anotherSet(x))

  // all integers => (_ % 3) => [0 1 2]
  def map[B](f: A => B): MySet[B] = politelyFail
  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
  def filter(predicate: A => Boolean): MySet[A] = ???
//todo    new PropertyBasedSet[A](x => property(x) && predicate(x))
  def foreach(f: A=> Unit): Unit = politelyFail

  def -(elem: A): MySet[A] = filter(x => x != elem)
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

//todo def unary_! :MySet[A] = new PropertyBasedSet[A](_ => true)

  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole!")
}
*/
class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {

  def contains(elem: A): Boolean =   // 🔴
    elem == head || tail.contains(elem)

  def +(elem: A): MySet[A] =  // 🔴
    if (this contains elem) this
    else new NonEmptySet[A] (elem, this)

  /*
   * [1 2 3] ++ [4 5] =
   * [2 3] ++ [4 5] + 1 =
   * [3] ++ [4 5] + 1 + 2 =
   * [] ++ [4 5] + 1 + 2 + 3
   * [4 5] + 1 + 2 + 3 + = [4 5 1 2 3]
   */
  def ++(anotherSet: MySet[A]): MySet[A] =  // 🔴
    tail ++ anotherSet + head

  def map[B](f: A => B): MySet[B] = (tail map f) + f(head) // 🔴
  def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head) // 🔴
  def filter(predicate: A => Boolean): MySet[A] = { // 🔴
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  // part #2
  def -(elem: A): MySet[A] =
    if (head == elem) tail
    else tail - elem + head

  def --(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet(x))
  def &(anotherSet: MySet[A]): MySet[A] = filter(x => anotherSet.contains(x)) //intersection = filtering!

  // new operator
//todo  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
}
object MySet {
  /*
    val s = MySet(1,2,3) = buildSet(seq(1,2,3), [])
    = buildSet(seq(2,3, [] + 1)
    = buildSet(seq(3), [1] + 2)
    = buildSet(seq(), [1, 2] + 3)
    = [1, 2, 3]
   */
  def apply[A](values: A*): MySet[A] = {
    @annotation.tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)
    buildSet(values.toSeq, new EmptySet[A]) // Point
  }
}

object MySetPlayground extends App {
  val s = MySet(1, 2, 3, 4)

  // EXERCISE #1
  // test 1
  s foreach println

  // test 2
  s + 5 ++ MySet(-1, -2) + 3 flatMap(x => MySet(x, 10 * x)) foreach println

  // test 3
  s + 5 ++ MySet(-1, -2) + 3 flatMap(x => MySet(x, 10 * x)) filter(_ % 2 == 0) foreach println

/*todo
  val negative = !s
  println(negative(2))
  println(negative(5))

  val negativeEven = negative.filter(_ % 2 == 0)
  println(negativeEven(5))

  val negativeEven5 = negativeEven + 5
  println(negativeEven5)
*/
}
