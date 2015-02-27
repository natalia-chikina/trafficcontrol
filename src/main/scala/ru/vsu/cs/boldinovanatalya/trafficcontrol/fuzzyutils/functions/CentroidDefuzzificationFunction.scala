package ru.vsu.cs.boldinovanatalya.trafficcontrol.fuzzyutils.functions

import org.neuroph.core.Connection
import org.neuroph.core.input.InputFunction

class CentroidDefuzzificationFunction(parameters :Seq[(Double, Double)]) extends InputFunction{

  override def getOutput(connections: Array[Connection]): Double = {
    val num = (parameters.map(x => x._1 * x._2) zip connections.map(_.getInput)).map(x => x._1 * x._2).sum
    val denom = (parameters.map(_._2) zip connections.map(_.getInput)).map(x => x._1 * x._2).sum
    num/denom
  }
}

