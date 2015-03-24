package ru.vsu.cs.boldinovanatalya.trafficcontrol

import java.io.FileWriter
import java.util.{Calendar, Random}
import org.neuroph.core.NeuralNetwork
import ru.vsu.cs.boldinovanatalya.trafficcontrol.utils.JsonUtil

import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.{TrainingElement, NetworkEvolver, Genotype}

import scala.io.Source
import utils._

object Main extends App {

//  val trainingSetWithFuzzyRule = for {i <- normalizeFuzzySet(4)
//       j <- normalizeFuzzySet(4)
//       k <- normalizeFuzzySet(3)
//        } yield new TrainingElement((i._1, j._1, k._1), 0.0)
//
//  val fileWriter = new FileWriter("trainingSetFuzzyRule.json")
//  try {
//
//    fileWriter.write(JsonUtil.toJson(trainingSetWithFuzzyRule))
//  }
//  finally {
//    fileWriter.close()
//  }


  val inputSets = List(
    List((0.0,20.0), (30.0, 20.0), (60.0, 20.0),(90.0, 20.0)),
    List((0.0,20.0), (30.0, 20.0), (60.0, 20.0),(90.0, 20.0)),
    List((0.0, 4.0), (3.5, 4.0), (7.0, 4.0)))
  val outSet = List((0.0,10.0), (12.5, 10.0), (25.0, 10.0), (45.0, 10.0))
  val weights = new Genotype(48, outSet.length, new Random()).genes
  val trainingSet = JsonUtil.fromJson[List[TrainingElement]](Source.fromFile("trainingSetFuzzyRule.json").mkString)

  val evolver = new NetworkEvolver(trainingSet, List(normalizeFuzzySet(4), normalizeFuzzySet(4), normalizeFuzzySet(3)), normalizeFuzzySet(4), 100)
  println(Calendar.getInstance().getTime)
  val nw = evolver.evolve()
  nw.save("resultNN.nnet")

  val nwDemo = NeuralNetwork.createFromFile("resultNN.nnet")
  println(Calendar.getInstance().getTime)
  println("SET INPUTS")
  trainingSet.foreach(x => {
    nwDemo.setInput(x.input._1, x.input._2, x.input._3)
    nwDemo.calculate()
    println(nwDemo.getOutput()(0))

  })
}
