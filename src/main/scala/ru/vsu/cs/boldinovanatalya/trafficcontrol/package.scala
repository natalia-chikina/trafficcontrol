package ru.vsu.cs.boldinovanatalya

import ru.vsu.cs.traffic.Color.{YELLOW, RED, GREEN}
import ru.vsu.cs.traffic.{Point, TrafficModel}

package object trafficcontrol {

  type   FuzzySet = IndexedSeq[(Double, Double)]

  val interval = 15
  val bigProbability = 5.0/6
  val smallProbability = 0.5
  val mathExp = (interval * 60) / 2
  val width = interval * 33.3 // weird constant



  def bigProb(time: Double) = {
    math.exp(-(math.pow(time % (interval * 60) - mathExp, 2) / math.pow(width, 2))) * bigProbability
  }

  def smallProb(time: Double) = {
    math.exp(-(math.pow(time % (interval * 60) - mathExp, 2) / math.pow(width, 2))) * smallProbability
  }


  def first(time: Double) = {
    val quarter = (time % (interval * 60 * 4)).toInt / 60 / interval
    if (quarter % 2 == 0) smallProb(time) else bigProb(time)

  }

  def second(time: Double) = {
        val half = (time % 3600).toInt/60/(interval * 2)
        if (half % 2 == 0) smallProb(time) else bigProb(time)
//    val quarter = (time % 3600).toInt / 60 / 15
//    if (quarter % 2 == 0) bigProb(time) else smallProb(time)
  }


  def createModel (tcTime: Int = 10) = {
    val model: TrafficModel = TrafficModel(0.5, false, 5)
    model.addFlow(Point(150, 0), Point(150, 300), 3, first)
    model.addFlow(Point(0, 150), Point(300, 150), 3, second)
    model.intersections(0)(model.trafficFlows(2)).durations = Map(GREEN -> tcTime, RED -> tcTime, YELLOW -> 0)
    model.intersections(0)(model.trafficFlows(3)).durations = Map(GREEN -> tcTime, RED -> tcTime, YELLOW -> 0)
    model.intersections(0)(model.trafficFlows(0)).durations = Map(GREEN -> tcTime, RED -> tcTime, YELLOW -> 0)
    model.intersections(0)(model.trafficFlows(1)).durations = Map(GREEN -> tcTime, RED -> tcTime, YELLOW -> 0)

    model
  }
}
