package ru.vsu.cs.boldinovanatalya.trafficcontrol

import java.io.FileWriter
import java.util.Arrays

import breeze.plot.Figure
import cmeans.FuzzyCMeansAlgorithm
import org.neuroph.core.NeuralNetwork
import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.TrainingElement
import ru.vsu.cs.boldinovanatalya.trafficcontrol.utils.{Adaptivealgorithm, JsonUtil}
import scala.collection.JavaConversions._

import scala.io.Source

object AdaptiveTest extends App{
 val trainingSet = JsonUtil.fromJson[List[TrainingElement]](Source.fromFile("trainingSetSim.json").mkString)
 //val trainingSet = JsonUtil.fromJson[List[TrainingElement]](Source.fromFile("trainingSetFuzzyRule.json").mkString)
// val adaptiveAlgorithm = new Adaptivealgorithm(trainingSet)
//  val algorithm = new FuzzyCMeansAlgorithm(4, 10, trainingSet.map(x => List(x.input._1, 0.0).toArray), 0.0, null);
//  println("begin1")
//  algorithm.execute();
//  println("end1")
//
//  println(trainingSet.map(x => x.input._1))
//
//  println(algorithm.toStringCentroids)
//  println("begin2")
//  println(utils.fcm(trainingSet.map(_.input._1), 4))
//  println("end2")
//val trainingSet = JsonUtil.fromJson[List[TrainingElement]](Source.fromFile("trainingSetSim.json").mkString)
//  val fileWriter = new FileWriter("trainingSetSim.json")
//  try {
//
//    val maxQR = trainingSet.map(_.input._1).max
//    val maxQG = trainingSet.map(_.input._2).max
//    val maxAG = trainingSet.map(_.input._3).max
//
//    fileWriter.write(JsonUtil.toJson(trainingSet.map(x => new TrainingElement((x.input._1/maxQR, x.input._2/maxQG, x.input._3/maxAG), x.output))))
//  }
//  finally {
//    fileWriter.close()
//  }


  val a = new Adaptivealgorithm(trainingSet)
  println(a.run())
  val nwDemo = NeuralNetwork.createFromFile("resultNN.nnet").asInstanceOf[FuzzyNetwork]

val weights =  nwDemo.getLayerAt(2).getNeurons().map(x => x.getOutConnections.map(y => y.getWeight.getValue)).flatten.toList
  println(weights)
  val f = Figure()
  val p = f.subplot(0)
  p += breeze.plot.hist(weights, 30)

//  val f = Figure()
//  val p = f.subplot(0)
//  val x = for (i <- 0 until 3601) yield i.toDouble
//  p += breeze.plot.plot(x, x.map(t => first(t)))

  val f1 = Figure()
  val p1 = f1.subplot(0)
  val x1 = for (i <- 0 until 3601) yield i.toDouble
  p1 += breeze.plot.plot(x1, x1.map(t => second(t)))
}
