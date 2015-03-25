package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import java.util.Random

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory
import ru.vsu.cs.boldinovanatalya.trafficcontrol.FuzzySet

class GenotypeFactory(length : Int, count: Int, inputSets: IndexedSeq[FuzzySet]) extends AbstractCandidateFactory[Genotype] {

  override def generateRandomCandidate(random: Random): Genotype = {
    new Genotype(length, count, random, inputSets)
  }
}
