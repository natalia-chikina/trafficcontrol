package ru.vsu.cs.boldinovanatalya.trafficcontrol

import cmeans.FuzzyCMeansAlgorithm

import scala.collection.JavaConversions._


package object utils {

  def fcm(data: Seq[Double], nbCluster: Int) = {
    val algorithm = new FuzzyCMeansAlgorithm(nbCluster, 3.0, data.map(x => List(x, 0.0).toArray), 0.0, null);
    algorithm.execute()
    algorithm.getCentroids.map(_(0))
  }

  def normalizeFuzzySet(n: Int): FuzzySet = {
    val step = 1.0/(n-1)
    val width = (step * 0.67)/3
    (for(i <-0 until n) yield (step*i + (if(math.random > 0.5) -1 else 1)* math.random * 0.05 , width + (if(math.random > 0.5) -1 else 1)* math.random * 0.05)).toIndexedSeq
  }

  def denormalizeFuzzySet(set: FuzzySet, max: Int) = {
    set.map(x => (x._1*max, x._2*max))
  }

  def randN(mathExp: Double, d: Double) = {
    ((for (i <- 0 until 12) yield Math.random()).sum - 6) * d + mathExp
  }


}
