package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import java.util.Random

import ru.vsu.cs.boldinovanatalya.trafficcontrol.FuzzySet

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

class Genotype(size: Int, count: Int, r: Random, inSets: IndexedSeq[FuzzySet]) {
  val inputSets = new ArrayBuffer[ArrayBuffer[(Double, Double)]]
  for (is <- inSets) {
    inputSets += new ArrayBuffer[(Double, Double)]()
  }
  for (i <- 0 until inSets.length) {
    for (j <- 0 until inSets(i).length) {
      val p: (Double, Double) = inSets(i)(j)
      val p1: (Double, Double)= (p._1, p._2)
      inputSets(i) += p1
    }
  }
  val weights = new ListBuffer[ListBuffer[Double]]()
  for (i <- 0 until count) {
    weights += new ListBuffer[Double]
    for(j <- 0 until size) {
      weights(i) += r.nextDouble() + r.nextInt()
    }
  }


  override def toString: String = {
    s"InputSetsParameters = $inputSets"
  }

  override def clone(): AnyRef = {
    val cl = new Genotype(size, count, r, inputSets)
    for (i <- 0 until count) {
      for(j <- 0 until size) {
        cl.weights(i)(j) = weights(i)(j)
      }
    }
    cl
  }
}
