package concurrency

import scala.collection.mutable
import scala.util.Random

/**
  * A Taste of Advanced Scala
  * Functional Concurrent Programming
  *
  * - Producer-Consumer, Level2
  */
/*
hey producer, there's empty space available, are you lazy?
hey consumer, new food for you!

[consumer] buffer empty, waiting ...
[producer] producing 0
[consumer] consumed 0
[producer] producing 1
[consumer] consumed 1
[producer] producing 2
[producer] producing 3
[producer] producing 4
[consumer] consumed 2
[producer] producing 5
[producer] buffer is full, waiting...
[consumer] consumed 3
[producer] producing 6
[consumer] consumed 4
[consumer] consumed 5
[consumer] consumed 6
[producer] producing 7
[consumer] consumed 7
[producer] producing 8
[consumer] consumed 8
[consumer] buffer empty, waiting ...
[producer] producing 9
 */
object ProducerConsumerLevel2 extends App {

  /*
    producer -> [ ? ? ? ] - consumer
   */
  def prodConsLargeBuffer(): Unit ={
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while(true) {
        buffer.synchronized({
          if(buffer.isEmpty) {
            println("[consumer] buffer empty, waiting ...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println("[consumer] consumed " + x)

          // todo hey producer, there's empty space available, are you lazy?
          buffer.notify()
        })

        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0
      while(true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }
          // there must be at least ONE EMPTY SPACE in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)

          //todo hey consumer, new food for you!
          buffer.notify()
          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start
    producer.start
  }
  prodConsLargeBuffer()

}
