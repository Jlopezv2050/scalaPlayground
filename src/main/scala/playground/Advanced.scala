package playground

import scala.concurrent.Future

object Advanced extends App {

  //partial functions (operate only on a subset of given input domain)
  //only operates in 1,2,5 otherwise throw an Exception
  val partialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 69
    case 5 => 999
  }

  //equivalent to a partial function because pf are based on pattern matching and extend Int error
  val pf = (x:Int) => x match {
    case 1 => 42
    case 2 => 69
    case 5 => 999
  }

  //so we can do this assignations
  val function: PartialFunction[Int, Int] = partialFunction
  val functionEquivalen: (Int => Int) = partialFunction

  //because pf are extensions of normal functions then may operate collections
  val modifiedList = List(1,2,3).map({
    case 1 => 42
    case _ => 0
  })

  //scala sweet syntax avoid the parenthesis
  val modifiedSweetList = List(1,2,3).map{
    case 1 => 42
    case _ => 0
  }

  //lifting (turns a PF into normal function)
  val lifter = partialFunction.lift
  lifter(2) // Some(69)
  lifter(78) // None (Option

  //orElse (add a case)
  val pfChain = partialFunction.orElse[Int, Int] {
    case 60 => 9000
  }

  pfChain(5) //999
  pfChain(60) //9000
  //pfChain(4570) //throw a matchError

  //remember you can define an anonymous method
  def recive(): Unit = println("hi")

  //type aliases
  type ReceiveFunction = PartialFunction[Any, Unit]

  def receive: ReceiveFunction = {
    case 1 => println("Hello")
    case _ => println("confused...")
  }

  //you can use aliases interchangeably
  def receiveEquivalen: PartialFunction[Any, Unit] = {
    case 1 => println("hello")
    case _ => println("confused")
  }

  // implicits (default values)
  implicit val timeout: Int = 3000
  def setTimeout(function: () => Unit)(implicit  timeout: Int) = function()

  setTimeout(() => println("timeout"+ timeout))

  //implicit conversions
  case class Persona(name: String) {
    def greet = s"Hi, my name is $name"
  }

  //implicit def (for the strings allow to use it as a Persona class)
  implicit def fromStringToPerson(string: String): Persona = Persona(string)

  "Juan".greet //equivalent fromStringToPerson("Juan").greet

  //implicit classes
  implicit class Dog(name: String){
    def bark = println("bark!")
  }

  "Lassie".bark

  //organize implicits

  //local scope
  implicit val inverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  List(1,2,3).sorted

  //imported scope
  import scala.concurrent.ExecutionContext.Implicits.global
  val future = Future {
    println("hello future")
  }

  //companion objects of the types included in the call
  object Persona {
    implicit val personOrdering: Ordering[Persona] = Ordering.fromLessThan((a,b) => a.name.compareTo(b.name) < 0)
  }

  List(Persona("Bob"), Persona("Alice")).sorted

  //the order compiler uses to fetch the implicit is:
  // 1. local
  // 2. imported
  // 3. companion objects


}