package ru.vsu.cs.boldinovanatalya.trafficcontrol

import org.neuroph.core.input.{Difference, Max, Min}
import org.neuroph.core.{Weight, Neuron, NeuralNetwork}
import org.neuroph.core.transfer.{Linear, Gaussian}
import org.neuroph.util._
import ru.vsu.cs.boldinovanatalya.trafficcontrol.fuzzyutils.functions.{GaussianFunction, CentroidDefuzzificationFunction}


class FuzzyNetwork(inputSets:  Seq[FuzzySet], outputSet: FuzzySet, ruleLayerWeights: Seq[Seq[Double]]) extends NeuralNetwork{

  // set network type
  this.setNetworkType(NeuralNetworkType.NEURO_FUZZY_REASONER)

  private def createInputLayer() = {
    val neuronProperties = new NeuronProperties
    val inLayer = LayerFactory.createLayer(inputSets.length, neuronProperties)
    this.addLayer(inLayer)
  }

  private def createFuzzyLayer() = {
    val neuronProperties = new NeuronProperties
    neuronProperties.setProperty("transferFunction", TransferFunctionType.GAUSSIAN)
    val fuzzySetsLayerNodes = inputSets.map(_.length).sum
    val setLayer = LayerFactory.createLayer(fuzzySetsLayerNodes, neuronProperties)
    addLayer(setLayer)

    //setting transfer functions
    val setNeurons = setLayer.getNeurons
    for {
      (n, p) <- setNeurons.zip(inputSets.flatten)
    } n.setTransferFunction(new GaussianFunction(p._1, p._2))

    //connecting inputLayer with setLayer

    val connectionGroups: Seq[Array[Neuron]] =  inputSets.map(_.length).scanLeft(0)(_ + _).slice(0, inputSets.length)
      .zip(inputSets.map(_.length)).map(x => setNeurons.slice(x._1 , x._1 + x._2))

    val inLayer = getLayerAt(0)
    for (i <- 0 until inLayer.getNeurons.length) {
      for (setNeuron <- connectionGroups(i)) {
        ConnectionFactory.createConnection(inLayer.getNeuronAt(i), setNeuron, 1)
      }
    }
  }

  private def createRuleLayer() = {
    val neuronProperties = new NeuronProperties
    neuronProperties.setProperty("inputFunction", classOf[Min])
    neuronProperties.setProperty("transferFunction", classOf[Linear])
    val ruleNodes = inputSets.map(_.length).reduceLeft(_ * _)
    val ruleLayer = LayerFactory.createLayer(ruleNodes, neuronProperties)
    addLayer(ruleLayer)

    //connecting setLayer with ruleLayer
    var setNodeIndex = 0
    var setNeuron = new Neuron()
    var ruleNeuron = new Neuron()

    for (i <- 0 until inputSets.length) {
      val setSize = inputSets(i).length
      val connectionPerNode = ruleNodes / setSize

      val setLayer = getLayerAt(1)
      for (si <- 0 until setSize) {
        if (i == 0) {
          setNeuron = setLayer.getNeuronAt(si)

          setNodeIndex = si

          for (c <- 0 until connectionPerNode) {
            ruleNeuron = ruleLayer.getNeuronAt(connectionPerNode * si + c)
            ConnectionFactory.createConnection(setNeuron, ruleNeuron, 1)
          }
        } else {
          setNodeIndex += 1
          setNeuron = setLayer.getNeuronAt(setNodeIndex)

          for (c <- 0 until connectionPerNode) {
            ruleNeuron = ruleLayer.getNeuronAt(si + setSize * c)
            ConnectionFactory.createConnection(setNeuron, ruleNeuron, 1)
          }
        }
      }
    }

  }

  private def createRuleConsequensLayer() = {
    val neuronProperties = new NeuronProperties()
    neuronProperties.setProperty("weightsFunction", classOf[Product]) //performs operation with input and weight vector
    neuronProperties.setProperty("summingFunction", classOf[Max]) // performs operation with the resulting vector from weightsFunction
    neuronProperties.setProperty("transferFunction", TransferFunctionType.LINEAR)
    val ruleConseqLayer = LayerFactory.createLayer(outputSet.length, neuronProperties)
    addLayer(ruleConseqLayer)

    //connecting rule consequents layer with rule layer
    val ruleLayer = getLayerAt(2)
    if (ruleLayer.getNeuronsCount * outputSet.length != ruleLayerWeights.flatten.length) {
      throw new IllegalArgumentException
    }
    ConnectionFactory.fullConnect(ruleLayer, ruleConseqLayer)
    //ruleLayer.getNeurons.zip(ruleLayerWeights).foreach(x => x._1.getOutConnections.foreach(_.setWeight(new Weight(x._2))))
    ruleConseqLayer.getNeurons.zip(ruleLayerWeights).foreach(x => x._1.getInputConnections.zip(x._2).foreach(y => y._1.setWeight(new Weight(y._2))))

  }

  private def createOutputLayer() = {
    val neuronProperties = new NeuronProperties()
    neuronProperties.setProperty("transferFunction", TransferFunctionType.LINEAR)
    val outputLayer = LayerFactory.createLayer(1, neuronProperties)
    outputLayer.getNeurons.foreach(_.setInputFunction(new CentroidDefuzzificationFunction(outputSet)))

    //connecting rule consequents layer with output layer
    val ruleConseqLayer = getLayerAt(3)
    ConnectionFactory.fullConnect(ruleConseqLayer, outputLayer)
    addLayer(outputLayer)
  }

  createInputLayer()
  createFuzzyLayer()
  createRuleLayer()
  createRuleConsequensLayer()
  createOutputLayer()

  NeuralNetworkFactory.setDefaultIO(this)

}

object FuzzyNetwork {
  def apply(inputSets:  Seq[Seq[(Double, Double)]], outputSet: Seq[(Double, Double)], ruleLayerWeights: Seq[Seq[Double]] ) : FuzzyNetwork = {
    new FuzzyNetwork(inputSets, outputSet, ruleLayerWeights)
  }
}