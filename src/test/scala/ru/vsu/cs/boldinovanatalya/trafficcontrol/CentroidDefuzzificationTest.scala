package ru.vsu.cs.boldinovanatalya.trafficcontrol

import java.util.Random

import org.scalatest.FunSuite
import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.Genotype

class CentroidDefuzzificationTest extends FunSuite {

  val inputSets = List(
    List((0.0,10.0), (1.0, 2.0), (1.0, 3.0),(1.0, 3.0)),
    List((1.0,1.0), (1.0, 2.0), (1.0, 3.0),(1.0, 3.0)),
    List((1.0,1.0), (1.0, 2.0), (1.0, 3.0)))
  val outSet = List((0.0,10.0), (12.5, 10.0), (25.0, 10.0), (35.0, 10.0) )
  val weights = new Genotype(48, outSet.length, new Random()).genes
  val network = new FuzzyNetwork(inputSets, outSet, weights)
  test("centroid defuzzification result") {
    network.getLayerAt(3).getNeurons.foreach { x => x.setOutput(1)}
    assert(math.round(network.getLayerAt(4).getNeuronAt(0).getOutput) == 2)
  }

}
