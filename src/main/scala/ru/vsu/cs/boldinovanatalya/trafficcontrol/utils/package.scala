package ru.vsu.cs.boldinovanatalya.trafficcontrol

package object utils {
  def normalizeFuzzySet(n: Int): FuzzySet = {
    val step = 1.0/(n-1)
    val width = (step * 0.67)/3
    (for(i <-0 until n) yield (step*i + (if(math.random > 0.5) -1 else 1)* math.random * 0.05 , width + (if(math.random > 0.5) -1 else 1)* math.random * 0.05)).toIndexedSeq
  }

  def denormalizeFuzzySet(set: FuzzySet, max: Int) = {
    set.map(x => (x._1*max, x._2*max))
  }


}
