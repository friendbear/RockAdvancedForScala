package concurrency

import scala.collection.mutable
import scala.util.Random

/**
  * A Taste of Advanced Scala
  * Functional Concurrent Programming
  *
  * - Producer-Consumer, Level3
  */
object ProducerConsumerLevel3 extends App {

  /*
    Prod-cons, level 3

      producer1 -> [ ? ? ? ] =-> consumer1
      producer2 ----^     ^----- consumer2
   */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()

      while(true) {
        buffer.synchronized({
          /*
            producer produces value. two Cons are waiting
            notifies ONE consumer,notifies on buffer
            notifies the other consumer

           */
          /*
          [consumer 1] buffer empty, waiting ...
          [consumer 3] buffer empty, waiting ...
          [consumer 2] buffer empty, waiting ...
          [producer 1] producing 0
          [consumer 1] consumed 0
          [producer 3] producing 0
          Exception in thread "Thread-2" [producer 2] producing 0
          [consumer 2] consumed 0
          java.util.NoSuchElementException: queue empty
            at scala.collection.mutable.Queue.dequeue(Queue.scala:67)
            at concurrency.ProducerConsumerLevel3$Consumer.run(ProducerConsumerLevel3.scala:39)
          [producer 2] producing 1
          why ?
            🔴 This is Point not if use while 🔴
           */
          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting ...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue() // OOps.!
          println(s"[consumer $id] consumed " + x)

          // hey producer, there's empty space available, are you lazy?
          buffer.notify()
        })

        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0
      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer $id] buffer is full, waiting...")
            buffer.wait()
          }
          // there must be at least ONE EMPTY SPACE in the buffer
          println(s"[producer $id] producing " + i)
          buffer.enqueue(i)

          //todo hey consumer, new food for you!
          buffer.notify()
          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiProdCons(nConsumers: Int, nProducers: Int) = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())
  }

  multiProdCons(3, 3)
}
