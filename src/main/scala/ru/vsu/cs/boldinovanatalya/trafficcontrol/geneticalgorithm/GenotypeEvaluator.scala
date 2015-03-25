package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import java.util

import org.uncommons.watchmaker.framework.FitnessEvaluator
import ru.vsu.cs.boldinovanatalya.trafficcontrol.{FuzzySet, FuzzyNetwork}

class GenotypeEvaluator(trainingElements: Seq[TrainingElement], outputSet: FuzzySet)  extends FitnessEvaluator[Genotype] {

  override def getFitness(genotype: Genotype, list: util.List[_ <: Genotype]): Double = {
     val network = FuzzyNetwork(genotype.inputSets, outputSet, genotype.weights)
    trainingElements.map(te => {
      network.setInput(te.input._1, te.input._2, te.input._3)
      network.calculate()
      math.pow(network.getOutput()(0) - te.output, 2)
    }).sum
  }

  override def isNatural: Boolean = false
}
