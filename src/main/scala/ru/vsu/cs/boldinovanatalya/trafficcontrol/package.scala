package ru.vsu.cs.boldinovanatalya

import ru.vsu.cs.traffic.Color.{YELLOW, RED, GREEN}
import ru.vsu.cs.traffic.{Point, TrafficModel}

package object trafficcontrol {

  type FuzzySet = IndexedSeq[(Double, Double)]

  val bigProbability = 1.5
  val smallProbability = 0.8
  val mathExp = 450.0
  val width = 500.0


  def bigProb(time: Double) = {
    math.exp(-(math.pow(time % 900 - mathExp, 2) / math.pow(width, 2))) * bigProbability
  }

  def smallProb(time: Double) = {
    math.exp(-(math.pow(time % 900 - mathExp, 2) / math.pow(width, 2))) * smallProbability
  }


  def first(time: Double) = {
    val quarter = (time % 3600).toInt / 60 / 15
    if (quarter % 2 == 0) smallProb(time) else bigProb(time)

  }

  def second(time: Double) = {
        val half = (time % 3600).toInt/60/30
        if (half % 2 == 0) smallProb(time) else bigProb(time)
//    val quarter = (time % 3600).toInt / 60 / 15
//    if (quarter % 2 == 0) bigProb(time) else smallProb(time)
  }


  def createModel = {
    val tcTime = 30
    val model: TrafficModel = TrafficModel(0.5, false, 4)
    model.addFlow(Point(150, 0), Point(150, 300), 3, first)
    model.addFlow(Point(0, 150), Point(300, 150), 3, second)
    model.intersections(0)(model.trafficFlows(2)).durations = Map(GREEN -> tcTime, RED -> tcTime, YELLOW -> 0)
    model.intersections(0)(model.trafficFlows(3)).durations = Map(GREEN -> tcTime, RED -> tcTime, YELLOW -> 0)
    model.intersections(0)(model.trafficFlows(0)).durations = Map(GREEN -> tcTime, RED -> tcTime, YELLOW -> 0)
    model.intersections(0)(model.trafficFlows(1)).durations = Map(GREEN -> tcTime, RED -> tcTime, YELLOW -> 0)

    model
  }
}
