package playground

import scala.annotation.tailrec
import scala.util.Try

object Playground extends App{

  //VALUES AND VARIABLES

  val booleanVal: Boolean = false
  //you CAN't reassign booleanVal = true

  var booleanVar: Boolean = true
  // you CAN reassign variables
  booleanVar = false

  //no explicit is required, compiler "magically" infer it
  var  magicInt = 9
  magicInt += 1

  //expressions
  magicInt = if (booleanVal) 10 else 11

  //code block
  val codeBlockVal = {
    if (booleanVar) 89
    79 // previous line is ignored so 89 is unuseful
  }

  //TYPES

  //Unit -> Denote the type of expression that only have side effects (method in university), don't return anything.
  //        print, change mutable data return unit. Very used in akka
  val aUnit: Unit = println("Hello")

  //Function -> name(parameters):return (params and return could be infer by the compiler)
  def aFunction(x: Int):Int = x + 1

  //Recursion - TAIL recursion ( si la llamada a sí misma es la última acción de la función)
  // ej normal factorial n + factorial(n - 1) no porque lo último es la suma
  // sum(n - 1, total + n) con el uso de una variable extra sí lo cumple

  //el uso de la annotación @tailrec garantiza el uso de tail recursion
  //https://es.stackoverflow.com/questions/121965/que-es-tail-recursion

  @tailrec
  def factorial(valor:Int, acc:Int): Int =
    if (valor == 0) acc
    else factorial(valor - 1, acc)

  //OBJECT ORIENTED

  class Animal
  class Dog extends Animal

  //Polymorphism
  val aDog: Animal = new Dog

  //traits
  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Cocodrilo extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch")
  }

  //method notation
  val aCoroc = new Cocodrilo
  aCoroc.eat(aDog)

  //infix method notation
  aCoroc eat aDog

  //anonymous classes (instantiation a class  that extend from abstract types without declaring a type (class name) for them)
  val aCarnivor = new Carnivore {
    override def eat(a: Animal): Unit = println("gjhrhrhrhrhrhr")
  }

  aCarnivor eat aDog

  //generics covariant (ex. list of dogs is an extension of my list of animals)
  abstract class MyList[+A]

  //companion objects (singleton object of the abstract class)
  object MyList

  //case classes
  case class Persona(name: String, age: Int)

  //Exceptions
  val aPotentialFailure = try {
    throw new RuntimeException("I'm inocent")
  } catch {
    case e: Exception => "I caught"
  } finally {
    println("some finally")
  }

  // Functional programming (function object)

  val incrementer = new Function1[Int, Int] {
    //with syntax sugar Int => Int === Function1[Int,Int]
    override def apply(v1: Int): Int = v1 + 1
  }

  //is a trick by de compiler when sees an object is called like a function, callable wen you define an apply method
  val incremented = incrementer(42)
  //equivalent incrementer.apply(42)

  //anonymous increment function
  val anonymousIncrementer = (x: Int) => x + 1

  //Functional Programmin is all about working with functions as first-class
  //map is high order function (takes a function as a parameter or returns another function as a result)
  val a = List(1,2,3).map(incrementer)

  for (n <- a) {
    println(n)
  }

  //for comprenhensions
  val pairs = for {
    num <- List(1,2,3,4)
    char <- List('a','b','c','d')
  } yield num + "-" + char

  // List(1,2,3,4).flatMap(num => List('a','b','c').map(char => num + "-" + char))
  // List(1,2,3,4).num('a','b','c').map(char => num + "-" + char))
  // 1-a, 1-b, 1-c ...

  for (n <- pairs) {
    println(n)
  }

  // you can use with Seq, Array, List, Vector, Map, Tuples, Sets

  //abstractions som computational types
  // Option and Try
  val anOption = Some(2)
  val aTry = Try {
    throw new RuntimeException
  }

  // pattern matching
  val unknown = 2
  val order = unknown match {
    case 1 => "first"
    case 2 => "second"
    case _ => "unknown"
  }

  // more than just a switch, decompose and bind values to names
  val bob = Persona ("Bob", 22)
  val greeting = bob match {
    //_ is whatever
    case Persona(n,_) => s"Hi, im  $n"
  }

}