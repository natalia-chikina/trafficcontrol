package ru.vsu.cs.boldinovanatalya.trafficcontrol.utils

import ru.vsu.cs.boldinovanatalya.trafficcontrol.FuzzySet
import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.TrainingElement

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

class Adaptivealgorithm(val trainingSet: List[TrainingElement]) {

  val barrier = 0.2
  val inputs = ArrayBuffer[Seq[Double]]( trainingSet.map(x => x.input._1), trainingSet.map(x => x.input._2), trainingSet.map(x => x.input._3), trainingSet.map(x => x.output))

  def distance(x: Double, center: Double) = {
    math.sqrt(math.pow(x - center, 2))
  }

  def nextCenter(x: Double, c: ArrayBuffer[Double]) = {
    val distances = c.map(center => (distance(x, center), center)).toMap
    val minKey = distances.map(x => x._1).min
    (minKey, distances(minKey))
  }

  def makeClusters(inputs: Seq[Double]) = {
    var inputCentroids = ArrayBuffer[Double]()
    var inputClusterCount = ArrayBuffer[Double]()

    inputCentroids += inputs.head
    inputClusterCount += 1



    inputs.tail.foreach(x => {
      val nCenter = nextCenter(x, inputCentroids)
      val oldCenterIndex = inputCentroids.indexOf(nCenter._2)

      if(nCenter._1 > barrier) {
        println("new")
        inputCentroids += x
        inputClusterCount += 1
      } else {
        println("add")
        inputCentroids(oldCenterIndex) = (nCenter._2 * inputClusterCount(oldCenterIndex) + x)/(inputClusterCount(oldCenterIndex)+1)
        inputClusterCount(oldCenterIndex) = inputClusterCount(oldCenterIndex) + 1
      }
    })
    inputCentroids
  }


  def run() = {
   inputs.map(x => makeClusters(x))

  }


}
