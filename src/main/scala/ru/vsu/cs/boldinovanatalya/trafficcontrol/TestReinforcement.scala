package ru.vsu.cs.boldinovanatalya.trafficcontrol

import java.util.Calendar

import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.{NetworkEvolver, TrainingElement}
import ru.vsu.cs.boldinovanatalya.trafficcontrol.utils._
import ru.vsu.cs.traffic.Color.{YELLOW, RED, GREEN}
import ru.vsu.cs.traffic.{Point, TrafficModel}

import scala.io.Source

object TestReinforcement extends App {

  def getModel = {
    val model: TrafficModel = TrafficModel(0.5, true)
    model.addFlow(Point(250, 0), Point(250, 500), 3, _ => 2.0, isOneWay = false, secondProbability = null)
    model.addFlow(Point(0, 250), Point(500, 250), 3, _ => 0.8, isOneWay = false, secondProbability = null)
    model.trafficLights.foreach(_.durations = Map(GREEN -> 30, RED -> 30, YELLOW -> 0))
    model
  }

  val evolver = new NetworkEvolver(null, List(normalizeFuzzySet(4).toIndexedSeq, normalizeFuzzySet(4).toIndexedSeq, normalizeFuzzySet(3).toIndexedSeq).toIndexedSeq, normalizeFuzzySet(4).toIndexedSeq, 100, false, getModel, new TrainingElement((24.0, 24.0, 12.0), 25) )
  println(Calendar.getInstance().getTime)
  val nw = evolver.evolve()
  nw.save("resultNNReinforcement.nnet")
  println("success")
}
