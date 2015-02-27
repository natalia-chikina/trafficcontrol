package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import java.util
import java.util.Random

import org.uncommons.maths.number.ConstantGenerator
import org.uncommons.maths.random.Probability
import org.uncommons.watchmaker.framework.EvolutionaryOperator
import collection.JavaConversions._

class GenotypeMutation(probability: Double) extends EvolutionaryOperator[Genotype] {
  val mutationCount = new ConstantGenerator(new Integer(1))
  val mutationProbability = new Probability(probability)
  override def apply(genotypes: util.List[Genotype], random: Random): util.List[Genotype] = {
    genotypes.foreach(x => {
      if (this.mutationProbability.nextEvent(random)) {
        for (i <- 0 until mutationCount.nextValue()) {
          //var index = (math.abs(random.nextInt() % x.genes.length)
          x.genes(math.abs(random.nextInt() % x.genes.length))(math.abs(random.nextInt() % x.genes(0).length)) = math.random + random.nextInt()
        }
      }
    })
    genotypes
  }
}
