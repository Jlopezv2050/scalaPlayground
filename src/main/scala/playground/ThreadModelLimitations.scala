package playground

import scala.concurrent.Future

object ThreadModelLimitations extends App {

  //1.-OPP encapsulation is only valid in the single threaded model

  class BankAccount(@volatile private var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int) = this.amount -= money
    def deposit(money: Int) = this.amount += money
    def getAmount = this.amount
  }

  //the principle says you call methods from a black box and inside of this box the internals are under control. So, If you
  //do 1000 threads withdrawing a dollar and 1000 depositing 1000 the result shall be the initial amount of the account

  val account = new BankAccount(2000)
  for(_ <- 1 to 1000) {
    new Thread(() => account.withdraw(1)).start()
  }

  for(_ <- 1 to 1000) {
    new Thread(() => account.deposit(1)).start()
  }

  println(account.getAmount)   // the result is 1999 so the internals haven't done its internals

  //to solve this you use synchronize stuff that drives your code into live, deadlocks problems and block the resources
  //increasing the time of computation

  //WE NEED non-blocking computation we will need a data structure full encapsulated and with no locks


  //2.-Inform a running thread becomes a big headache having to use wait() and notify. The solution is the model Consumer Producer
  // but involves many troubels

  //3.- other problems (signals like run this every x seconds, if there is multiple background tasks, which threads gets which task from
  // the running thread, how identify who gave you the signal, what if the background crash, how to manage it?)

  //WE NEED data structure which can safely receive messages, identify the sender, easily identifiable and can guard against errors

  //4.- Tracing and dealing errors in a multithreading is a pain
  import scala.concurrent.ExecutionContext.Implicits.global

  val futures = (0 to 9)
    .map(i=> 10000 * i until 10000 * (i + 1)) // 0 - 99999, 10000 - 19999, 20000 - 29999 etc
    .map(range => Future {
      if (range.contains(546735)) throw new RuntimeException("Invalid number")
      range.sum
    })

  val sumFuture = Future.reduceLeft(futures)(_ + _) //Future with the sum of all the numbers
  sumFuture.onComplete(println)

  //the console log is
  // ....
  //(background) waiting for a task (from the previous excercice)
  //Failure(java.lang.RuntimeException: invalid number)

}
