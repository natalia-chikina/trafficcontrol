package ru.vsu.cs.boldinovanatalya.trafficcontrol

import java.io.FileWriter
import java.util.{Calendar, Random}
import org.neuroph.core.NeuralNetwork
import ru.vsu.cs.boldinovanatalya.trafficcontrol.utils.JsonUtil

import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.{TrainingElement, NetworkEvolver, Genotype}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import utils._

object Main extends App {

  val trainingSet = JsonUtil.fromJson[List[TrainingElement]](Source.fromFile("trainingSetFuzzyRule.json").mkString)

  val evolver = new NetworkEvolver(trainingSet, List(normalizeFuzzySet(4).toIndexedSeq, normalizeFuzzySet(4).toIndexedSeq, normalizeFuzzySet(3).toIndexedSeq).toIndexedSeq, normalizeFuzzySet(4).toIndexedSeq, 100)
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
