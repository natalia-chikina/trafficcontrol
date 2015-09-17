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

// val trainingSet1 = JsonUtil.fromJson[List[TrainingElement]](Source.fromFile("trainingSetFuzzyRule.json").mkString)
  val trainingSet = JsonUtil.fromJson[List[TrainingElement]](Source.fromFile("trainingSetFuzzyRule.json").mkString)
  //val trainingSet2 = JsonUtil.fromJson[List[TrainingElement]](Source.fromFile("trainingSetSim.json").mkString)
  //val trainingSet = trainingSet1 ++ trainingSet2

  def noise(trainingSet: List[TrainingElement], count: Int) = {
    val buffer = new ArrayBuffer[TrainingElement]()
    trainingSet.copyToBuffer(buffer)
    for (i <-0 until count) {
      trainingSet.map(x => {
        new TrainingElement(
          (x.input._1 + randN(0, 0.005),
            x.input._2 + randN(0, 0.005),
            x.input._3 + randN(0, 0.005)),
          x.output + randN(0, 0.005)
        )
      }).copyToBuffer(buffer)
    }
    buffer
  }

  val adaptiveAlgorithm = new Adaptivealgorithm(trainingSet)

  val evolver = new NetworkEvolver(noise(trainingSet, 2), List(normalizeFuzzySet(4).toIndexedSeq, normalizeFuzzySet(4).toIndexedSeq, normalizeFuzzySet(3).toIndexedSeq).toIndexedSeq, normalizeFuzzySet(4).toIndexedSeq, 100, true )
  //val evolver = new NetworkEvolver(noise(trainingSet, 2), List(adaptiveAlgorithm.run()(0).map(x => (x, 0.05)), adaptiveAlgorithm.run()(1).map(x => (x, 0.05)), adaptiveAlgorithm.run()(2).map(x => (x, 0.05))).toIndexedSeq, adaptiveAlgorithm.run()(3).map(x => (x, 0.05)), 100, true )
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
