package advanced


trait MySet[A] extends (A => Boolean) {

  // 自身のcontainsメソッドの呼び出し
  def apply(elem: A): Boolean =
    contains(elem)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A=> Unit): Unit

  /*
   * EXECISE #2
   * removing an element
   * intersection whit another set
   * difference with another set
   */
  def -(elem: A): MySet[A]
  def --(anoterSet: MySet[A]): MySet[A]
  def &(anotherSet: MySet[A]): MySet[A]

  // EXECISE #3
  // set[1, 2, 3] => implement a unary_! = NEGATION of a set
  def unary_1! : MySet[A]
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
  def --(anoterSet: MySet[A]): MySet[A] = this
  def &(anotherSet: MySet[A]): MySet[A] = this
  
  def unary_1! : MySet[A]  = new MySet[A] {

  }
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  def contains(elem: A): Boolean = 
    elem == head || tail.contains(elem)

  def +(elem: A): MySet[A] = 
    if (this contains elem) this
    else new NonEmptySet[A] (elem, this)

  /**
   * [1 2 3] ++ [4 5] =
   * [2 3] ++ [4 5] + 1 =
   * [3] ++ [4 5] + 1 + 2 =
   * [] ++ [4 5] + 1 + 2 + 3
   * [4 5] + 1 + 2 + 3 + = [4 5 1 2 3]
   */
  def ++(anotherSet: MySet[A]): MySet[A] = 
    tail ++ anotherSet + head

  def map[B](f: A => B): MySet[B] = (tail map f) + f(head)
  def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)
  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  def foreach(f: A=>Unit): Unit = {
    f(head)
    tail foreach f
  }

  // part 2
  def -(elem: A): MySet[A] =
    if ( head == elem) tail
    else tail - elem + head

  def --(anoterSet: MySet[A]): MySet[A] = filter(x => !anotherSet(x))
  def &(anoterSet: MySet[A]): MySet[A] = filter(x => anotherSet.contains(x)) //intersection = filtering!

  // new operator
  def unary_! : MySet[A]
}
object MySet {

  def apply[A](values: A*): MySet[A] = {

    //@trailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)
    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1, 2, 3, 4)
  s + 5 ++ MySet(-1, -2) + 3 flatMap(x =>MySet(x, 10 * x)) filter(_ % 2 == 0) foreach println
}
