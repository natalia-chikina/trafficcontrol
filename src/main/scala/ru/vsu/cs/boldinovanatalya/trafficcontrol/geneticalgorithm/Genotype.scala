package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import java.util.Random

import scala.collection.mutable.ListBuffer

class Genotype(size: Int, count: Int, r: Random) {
  val genes = new ListBuffer[ListBuffer[Double]]()
  for (i <- 0 until count) {
    genes += new ListBuffer[Double]
    for(j <- 0 until size) {
      genes(i) += r.nextDouble() + r.nextInt()
    }
  }


  override def toString: String = {
    s"Genotype = $genes"
  }

  override def clone(): AnyRef = {
    val cl = new Genotype(size, count, r)
    for (i <- 0 until count) {
      for(j <- 0 until size) {
        cl.genes(i)(j) = genes(i)(j)
      }
    }
    cl
  }
}
