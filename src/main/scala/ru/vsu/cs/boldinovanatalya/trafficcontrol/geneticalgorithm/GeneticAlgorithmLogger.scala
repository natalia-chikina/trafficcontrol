package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import org.uncommons.watchmaker.framework.{PopulationData, EvolutionObserver}
import ru.vsu.cs.boldinovanatalya.trafficcontrol.{FuzzySet, FuzzyNetwork}

class GeneticAlgorithmLogger(outputSet: FuzzySet) extends EvolutionObserver[Genotype] {
  override def populationUpdate(populationData: PopulationData[_ <: Genotype]): Unit = {
    if(populationData.getGenerationNumber % 1000 == 0) {
      val network = FuzzyNetwork(populationData.getBestCandidate.inputSets, outputSet, populationData.getBestCandidate.weights)
      network.save("resultBackup.nnet")
      println(populationData.getGenerationNumber)
      println(populationData.getBestCandidate)
      println(populationData.getBestCandidateFitness)
      println("populationSize = " + populationData.getPopulationSize)
    }
  }
}
