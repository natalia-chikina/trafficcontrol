package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import java.util

import org.uncommons.watchmaker.framework.FitnessEvaluator
import ru.vsu.cs.boldinovanatalya.trafficcontrol.networkcontrol.NetworkModel
import ru.vsu.cs.boldinovanatalya.trafficcontrol.utils.Statistics
import ru.vsu.cs.boldinovanatalya.trafficcontrol.{FuzzyNetwork, FuzzySet}
import ru.vsu.cs.traffic.TrafficModel

class GenotypeEvaluatorReinforcement(model: TrafficModel, output: FuzzySet, maxParameterValues: TrainingElement ) extends FitnessEvaluator[Genotype] {
  override def getFitness(candidate: Genotype, population: util.List[_ <: Genotype]): Double = {
    val network = FuzzyNetwork(candidate.inputSets, output, candidate.weights)
    val nw = new NetworkModel(model, network, maxParameterValues)
    model.run(300)
    nw.statistics.averageQueuing
  }

  override def isNatural: Boolean = false
}
