package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import java.util.Random

import org.uncommons.maths.binary.BitString
import org.uncommons.maths.random.{Probability, MersenneTwisterRNG}
import org.uncommons.watchmaker.framework.selection.{TournamentSelection, RouletteWheelSelection}
import org.uncommons.watchmaker.framework.termination.{GenerationCount, TargetFitness}
import org.uncommons.watchmaker.framework.{GenerationalEvolutionEngine, EvolutionaryOperator}
import org.uncommons.watchmaker.framework.operators.{BitStringCrossover, BitStringMutation, EvolutionPipeline}
import ru.vsu.cs.boldinovanatalya.trafficcontrol.{FuzzySet, FuzzyNetwork}
import ru.vsu.cs.traffic.TrafficModel
import collection.JavaConversions._

class NetworkEvolver(trainingElements: Seq[TrainingElement] = null, inputSets: IndexedSeq[FuzzySet], outputSet: FuzzySet, populationSize: Int, isSupervised: Boolean, model: TrafficModel = null, maxParametersValues: TrainingElement = null) {

  def evolve(): FuzzyNetwork = {
    val operators = List(new GenotypeCrossover(4, 0.5), new GenotypeMutation(0.01))
    val engine = new GenerationalEvolutionEngine[Genotype](
      new GenotypeFactory(inputSets.map(_.length).reduceLeft(_ * _), outputSet.length, inputSets),
      new EvolutionPipeline[Genotype](operators),
      if (isSupervised) new GenotypeEvaluatorSupervised(trainingElements, outputSet) else new GenotypeEvaluatorReinforcement(model, outputSet, maxParametersValues ),
      new TournamentSelection(new Probability(0.8)),
      new MersenneTwisterRNG())
    engine.setSingleThreaded(false)
    engine.addEvolutionObserver(new GeneticAlgorithmLogger(outputSet))
    val winner = engine.evolve(populationSize, 5, new TargetFitness(1, false))
    new FuzzyNetwork(winner.inputSets.toIndexedSeq, outputSet, winner.weights)
  }
}
