package ru.vsu.cs.boldinovanatalya.trafficcontrol

import org.scalatest.FunSuite
import ru.vsu.cs.boldinovanatalya.trafficcontrol.fuzzyutils.functions.GaussianFunction

class GaussianFunctionTest extends FunSuite{
  val mathExp = 1
  val sigma = 1
  val argument = 1
  val gaussian = new GaussianFunction(mathExp, sigma)

  test("gaussian result") {
    assert(gaussian.getOutput(1) == 1)
  }

}
