package ru.vsu.cs.boldinovanatalya.trafficcontrol

import java.util.Random

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.Genotype

import scala.collection.mutable.ArrayBuffer

@RunWith(classOf[JUnitRunner])
class FuzzyNetworkCreationTest extends FunSuite {

  val inputSets = ArrayBuffer(
    List((1.0,1.0), (1.0, 2.0), (1.0, 3.0),(1.0, 3.0)).toIndexedSeq,
    List((1.0,1.0), (1.0, 2.0), (1.0, 3.0),(1.0, 3.0)).toIndexedSeq,
    List((1.0,1.0), (1.0, 2.0), (1.0, 3.0)).toIndexedSeq)
  val outSet = ArrayBuffer((1.0,1.0), (1.0, 2.0), (1.0, 3.0), (1.0, 4.0) )
  val weights = new Genotype(48, outSet.length, new Random(), inputSets.toIndexedSeq).weights
  val network = new FuzzyNetwork(inputSets, outSet.toIndexedSeq, weights)

  test("count of input layer's neurons"){
    assert(network.getInputsCount == 3)
  }

  test("count of fuzzy layer's neurons"){
    assert(network.getLayerAt(1).getNeuronsCount == 11)
  }

  test("count of rule layer's neurons"){
    assert(network.getLayerAt(2).getNeuronsCount == 48)
  }

  test("count of rule consequence layer's neurons"){
    assert(network.getLayerAt(3).getNeuronsCount == 4)
  }

  test("count of output layer's neurons"){
    assert(network.getOutputsCount == 1)
  }

  test("count of layers"){
    assert(network.getLayersCount == 5)
  }

  test("input layer's first neuron connections"){
    val inputNeuron = network.getLayerAt(0).getNeuronAt(0)
    val inputConnections = inputNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(1).getNeurons.slice(0, 4)
    assert(outputNeurons.zip(inputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }
  test("input layer's second neuron connections"){
    val inputNeuron = network.getLayerAt(0).getNeuronAt(1)
    val inputConnections = inputNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(1).getNeurons.slice(4, 8)
    assert(outputNeurons.zip(inputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }

  test("input layer's third neuron connections"){
    val inputNeuron = network.getLayerAt(0).getNeuronAt(2)
    val outputConnections = inputNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(1).getNeurons.slice(8, 11)
    assert(outputNeurons.zip(outputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }

  test("fuzzy layer's first neuron of first group connections") {
    val fuzzyNeuron = network.getLayerAt(1).getNeuronAt(0)
    val outputConnections = fuzzyNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(2).getNeurons.slice(0, 13)
    assert(outputNeurons.zip(outputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }

  test("fuzzy layer's last neuron of first group connections") {
    val fuzzyNeuron = network.getLayerAt(1).getNeuronAt(3)
    val outputConnections = fuzzyNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(2).getNeurons.slice(36, 48)
    assert(outputNeurons.zip(outputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }

  test("fuzzy layer's first neuron of second group connections") {
    val fuzzyNeuron = network.getLayerAt(1).getNeuronAt(4)
    val outputConnections = fuzzyNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(2).getNeurons.zip(0 to network.getLayerAt(2).getNeuronsCount).filter(x => x._2 % 4 == 0).map(x => x._1)
    assert(outputNeurons.zip(outputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }

  test("fuzzy layer's last neuron of second group connections") {
    val fuzzyNeuron = network.getLayerAt(1).getNeuronAt(7)
    val outputConnections = fuzzyNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(2).getNeurons.zip(0 to network.getLayerAt(2).getNeuronsCount).filter(x => x._2 % 4 == 3).map(x => x._1)
    assert(outputNeurons.zip(outputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }


  test("fuzzy layer's first neuron of third group connections") {
    val fuzzyNeuron = network.getLayerAt(1).getNeuronAt(8)
    val outputConnections = fuzzyNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(2).getNeurons.zip(0 to network.getLayerAt(2).getNeuronsCount).filter(x => x._2 % 3 == 0).map(x => x._1)
    assert(outputNeurons.zip(outputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }

  test("fuzzy layer's last neuron of third group connections") {
    val fuzzyNeuron = network.getLayerAt(1).getNeuronAt(10)
    val outputConnections = fuzzyNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(2).getNeurons.zip(0 to network.getLayerAt(2).getNeuronsCount).filter(x => x._2 % 3 == 2).map(x => x._1)
    assert(outputNeurons.zip(outputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }


  test("rule layer's first neuron") {
    val ruleNeuron = network.getLayerAt(2).getNeuronAt(0)
    val outputConnections = ruleNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(3).getNeurons
    assert(outputNeurons.zip(outputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }

  test("rule layer's last neuron") {
    val ruleNeuron = network.getLayerAt(2).getNeuronAt(47)
    val outputConnections = ruleNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(3).getNeurons
    assert(outputNeurons.zip(outputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }

  test("rule conseq layer's first neuron") {
    val ruleConseqNeuron = network.getLayerAt(3).getNeuronAt(0)
    val outputConnections = ruleConseqNeuron.getOutConnections
    val outputNeurons = network.getLayerAt(4).getNeurons
    assert(outputNeurons.zip(outputConnections).map(x => x._2.getToNeuron == x._1).filter(!_).length == 0)
  }

  test("rule output layer's neuron") {
    val ruleConseqNeuron = network.getLayerAt(4).getNeuronAt(0)
    assert(ruleConseqNeuron.getInputConnections.length == 4)
  }



}
