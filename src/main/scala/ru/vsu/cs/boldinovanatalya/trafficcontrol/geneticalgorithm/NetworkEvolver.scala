package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import java.util.Random

import org.uncommons.maths.binary.BitString
import org.uncommons.maths.random.{Probability, MersenneTwisterRNG}
import org.uncommons.watchmaker.framework.selection.{TournamentSelection, RouletteWheelSelection}
import org.uncommons.watchmaker.framework.termination.{GenerationCount, TargetFitness}
import org.uncommons.watchmaker.framework.{GenerationalEvolutionEngine, EvolutionaryOperator}
import org.uncommons.watchmaker.framework.operators.{BitStringCrossover, BitStringMutation, EvolutionPipeline}
import ru.vsu.cs.boldinovanatalya.trafficcontrol.{EvolutionLogger, FuzzySet, FuzzyNetwork}
import collection.JavaConversions._

class NetworkEvolver(trainingElements: Seq[TrainingElement], inputSets: Seq[FuzzySet], outputSet: FuzzySet, populationSize: Int) {

  def evolve(): FuzzyNetwork = {
    val operators = List(new GenotypeCrossover(1, 0.5), new GenotypeMutation(0.01))
    val engine = new GenerationalEvolutionEngine[Genotype](
      new GenotypeFactory(inputSets.map(_.length).reduceLeft(_ * _), outputSet.length),
      new EvolutionPipeline[Genotype](operators),
      new GenotypeEvaluator(trainingElements, inputSets, outputSet),
      new TournamentSelection(new Probability(0.8)),
      new MersenneTwisterRNG()) //TODO
    engine.setSingleThreaded(false)
    engine.addEvolutionObserver(new GeneticAlgorithmLogger)
    val winner = engine.evolve(populationSize, 5, new TargetFitness(10, false))
    new FuzzyNetwork(inputSets, outputSet, winner.genes)
  }
}
