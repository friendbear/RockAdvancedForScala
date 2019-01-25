package concurrency

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference

import scala.collection.parallel.{ForkJoinTaskSupport, Task, TaskSupport}
import scala.collection.parallel.immutable.ParVector

/**
  * A Taste of Advanced Scala
  * Functional Concurrent Programming
  *
  * - Scala & JVM Standard Parallel Libraries
  *   - parallel collections (par, Par)
  *     - Map-reduce model
  *     - synchronization
  *     - alternatives
  *   - atomic ops and references
  */
object ParallelUtils extends App {

  // 1 - parallel collections
  /*
    Seq
    Vector
    Array
    Map - Hash, Trie
    Set - Hash, Trie
   */
  val parList = List(1,2,3).par

  val aParVector = ParVector[Int](1, 2, 3)

  // benchmark
  def measure[T](operation: => T): Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }

  // serial time: 42
  val list = (1 to 1000000).toList
  val serialTime = measure {
    list.map(_ + 1)
  }
  println("serial time: " + serialTime)

  // parallel time: 206
  val parallelTime = measure {
    list.par.map(_ + 1)
  }
  println("parallel time: " + parallelTime)

  /*
    Map-reduce model
    - split the elements into chunks - Splitter
    - operation
    - recombine - Combiner
   */
  // map, flatMap, filter, foreach, reduce, fold

  // fold, reduce with non-associative operators
  println(List(1, 2, 3).reduce(_ + _))
  println(List(1, 2, 3).par.reduce(_ + _))

  // synchronization
  var sum = 0
  List(1, 2, 3).par.foreach(sum += _)
  println(sum) // 6 // race conditions!

  // configuring.
  aParVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))

  /*
    alternatives
    - ThreadPoolTaskSupport - deprecated
    - ExecutionContextTaskSupport(EC)
   */
  aParVector.tasksupport = new TaskSupport {
    override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???

    override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???

    override def parallelismLevel: Int = ???

    override val environment: AnyRef = None
  }

  // 2 - atomic ops and references

  val atomic = new AtomicReference[Int](2)
  val atomicList = List(
    atomic.get(), // thread-safe read
    atomic.set(4), // thread-safe write
    atomic.getAndSet(5), // thread safe combo
    atomic.compareAndSet(38, 56), // if the value is 38, then set to 56
    atomic.updateAndGet(_ + 1),
    atomic.getAndUpdate(_ + 1),
    atomic.accumulateAndGet(12, _ + _),
    atomic.getAndAccumulate(12, _ + _),
    atomic.get()
  ).filter(_ != ())
  atomicList.foreach(println)
}
