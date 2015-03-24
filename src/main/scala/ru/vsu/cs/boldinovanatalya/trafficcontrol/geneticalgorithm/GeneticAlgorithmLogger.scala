package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import org.uncommons.watchmaker.framework.{PopulationData, EvolutionObserver}

class GeneticAlgorithmLogger extends EvolutionObserver[Genotype] {
  override def populationUpdate(populationData: PopulationData[_ <: Genotype]): Unit = {
    if(populationData.getGenerationNumber % 1000 == 0) {
      println(populationData.getGenerationNumber)
      //println(populationData.getBestCandidate)
      println(populationData.getBestCandidateFitness)
      println("populationSize = " + populationData.getPopulationSize)
    }
  }
}
