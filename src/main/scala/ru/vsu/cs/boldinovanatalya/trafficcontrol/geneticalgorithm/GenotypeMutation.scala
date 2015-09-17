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
          val index1 = math.abs(random.nextInt() % x.inputSets.length)
          val index2 = math.abs(random.nextInt() % x.inputSets(index1).length)
          val pair =  x.inputSets(index1)(index2)
          val sign1 = if (random.nextDouble > 0.5) 1 else -1
          val sign2 = if (random.nextDouble > 0.5) 1 else -1

          x.inputSets(index1)(index2) = (pair._1 + sign1* random.nextDouble * 0.02, pair._2 + sign2 * random.nextDouble * 0.005)
          x.weights(math.abs(random.nextInt() % x.weights.length))(math.abs(random.nextInt() % x.weights(0).length)) += 0.5 * sign1 * random.nextDouble()//math.random //+ random.nextInt() % 10
        }
      }
    })
    genotypes
  }
}
