package ru.vsu.cs.boldinovanatalya.trafficcontrol.fuzzyutils.functions

import org.neuroph.core.transfer.TransferFunction

class GaussianFunction(mathExp: Double, sigma: Double) extends TransferFunction{
  override def getOutput(x: Double): Double = {
    math.exp(-(math.pow(x - mathExp, 2)/math.pow(sigma, 2)))
  }
}
