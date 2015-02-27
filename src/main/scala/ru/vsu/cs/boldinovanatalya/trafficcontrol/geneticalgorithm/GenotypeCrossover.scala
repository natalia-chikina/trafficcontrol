package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import java.util
import java.util.Random
import collection.JavaConversions._

import org.uncommons.maths.random.Probability
import org.uncommons.watchmaker.framework.operators.AbstractCrossover

class GenotypeCrossover(crossoverPoints: Int, probability: Double) extends AbstractCrossover[Genotype](crossoverPoints, new Probability(probability)) {

  override def mate(parent1: Genotype, parent2: Genotype, crossoverPoints: Int, random: Random): util.List[Genotype] = {
    val offspring1 = parent1.clone().asInstanceOf[Genotype]
    val offspring2 = parent2.clone().asInstanceOf[Genotype]
    val parentGenes = offspring1.genes
    val offspringGenes = offspring2.genes
    val length = parentGenes.length * parentGenes(0).length
    for (i <- 0 until crossoverPoints) {
      val fromIndex = math.abs(random.nextInt() % length)
      val tmpIndex = math.abs(fromIndex + random.nextInt())
      val toIndex = if (tmpIndex > length) length else tmpIndex
      for (j <- fromIndex until toIndex) {
        val x = j / parentGenes(0).length
        val y = j % parentGenes(0).length
        val tmp = parentGenes(x)(y)
        parentGenes(x)(y) = offspringGenes(x)(y)
        offspringGenes(x)(y) = tmp
      }
    }
    List(offspring1, offspring2)
  }
}
