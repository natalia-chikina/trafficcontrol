package ru.vsu.cs.boldinovanatalya.trafficcontrol

import java.util.Random
import ru.vsu.cs.boldinovanatalya.trafficcontrol.utils.JsonUtil

import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.{TrainingElement, NetworkEvolver, Genotype}

import scala.io.Source

object Main extends App {
  val inputSets = List(
    List((0.0,20.0), (30.0, 20.0), (60.0, 20.0),(90.0, 20.0)),
    List((0.0,20.0), (30.0, 20.0), (60.0, 20.0),(90.0, 20.0)),
    List((0.0, 4.0), (3.5, 4.0), (7.0, 4.0)))
  val outSet = List((0.0,10.0), (12.5, 10.0), (25.0, 10.0), (45.0, 10.0))
  val weights = new Genotype(48, outSet.length, new Random()).genes

  val trainingSet = JsonUtil.fromJson[List[TrainingElement]](Source.fromFile("trainingSet.json").mkString)
  val evolver = new NetworkEvolver(trainingSet, inputSets, outSet, 100)
  val nw = evolver.evolve()
  println("SET INPUTS")
  trainingSet.foreach(x => {
    nw.setInput(x.input._1, x.input._2, x.input._3)
    nw.calculate()
    println(nw.getOutput()(0))

  })
}
