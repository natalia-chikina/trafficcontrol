package ru.vsu.cs.boldinovanatalya.trafficcontrol

import java.util.Random

import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.{TrainingElement, NetworkEvolver, Genotype}

object Main extends App {
  val inputSets = List(
    List((0.0,20.0), (30.0, 20.0), (60.0, 20.0),(90.0, 20.0)),
    List((0.0,20.0), (30.0, 20.0), (60.0, 20.0),(90.0, 20.0)),
    List((0.0, 4.0), (3.5, 4.0), (7.0, 4.0)))
  val outSet = List((0.0,10.0), (12.5, 10.0), (25.0, 10.0), (45.0, 10.0))
  val weights = new Genotype(48, outSet.length, new Random()).genes
  val trainingSet = List(
    new TrainingElement((2.0, 2.0, 3.0), 10.0),
    new TrainingElement((1.0, 50.0, 8.0), 20.0),
    /*new TrainingElement((1.0, 3.0, 4.0), 30.0),
    new TrainingElement((4.0, 6.0, 9.0), 0.0),*/
    new TrainingElement((12.0, 20.0, 38.0), 11.0),
    new TrainingElement((2.0, 4.0, 1.0), 14.0),
    new TrainingElement((8.0, 9.0, 10.0), 19.0),
  //  new TrainingElement((5.0, 6.0, 5.0), 28.0),
    new TrainingElement((1.0, 1.0, 1.0), 8.0),
    new TrainingElement((4.0, 9.0, 2.0), 1.0)
  )
  val evolver = new NetworkEvolver(trainingSet, inputSets, outSet, 100)
  val nw = evolver.evolve()
  println("SET INPUTS")
  trainingSet.foreach(x => {
    nw.setInput(x.input._1, x.input._2, x.input._3)
    nw.calculate()
    println(nw.getOutput()(0))

  })
}
