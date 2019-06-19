package playground

import scala.concurrent.Future
import scala.util.Failure

object Multithreading extends App {

  //create thread
  val aThread = new Thread (() => println("Hi Thread"))
  aThread.start()
  aThread.join() //wait to finish

  // you can use multicore doing things at the same time but are unpredictable,
  // different runs produce different results

  class BankAccount(@volatile private var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int) = this.amount -= money

    def safeWithdraw(money: Int) = this.synchronized {
      this.amount -= money
    }
  }


  //the operation this.amount -= money is not atomic, and therefore is not threadsafe
  /*
   Bank account with 10000

   T1 wants withdraw 1000
   T2 wants withdraw 2000

   T1 starts doing this.amount
   suddenly T2 is scheduled
   T2.withdraw(2000)  = 8000
   T1 continue thinking this.amount of T1 is 10000

   result 9000
  */

  //usually is solved using synchronized as you can see in safeWithdraw
  //another solution is use @volatile (locks the amount member for read and write so no two threads can R/W simultaneously)
  //volatile only works for primitives


  //inter-thread communication
  //wait-notify mechanism

  //scala Futures
  import scala.concurrent.ExecutionContext.Implicits.global
  val future = Future {
    //long computation on a different thread
    42
  }

  //compute something when future finish

  future.onComplete {
    case util.Success(42) => println("I found a")
    case Failure(_) => println("Something happen")
    case util.Success(_) => println("nice shoot")
  }

  val aProcessedFuture = future.map(_ + 1)
  // concatenates the result of the future and it's used in another future
  val aFlatFuture = future.flatMap { value =>
    Future(value + 2)
  }

  val filteredFuture = future.filter(_ % 2 == 0) //NoSuchElementException

  val checkedFutureAfterFilter = future.map(_ + 1)

  aProcessedFuture.onComplete{
    case util.Success(43) => println("I found a")
  }

}