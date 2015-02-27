package ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm

import java.util.Random

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory

class GenotypeFactory(length : Int, count: Int) extends AbstractCandidateFactory[Genotype] {

  override def generateRandomCandidate(random: Random): Genotype = {
    new Genotype(length, count, random)
  }
}
