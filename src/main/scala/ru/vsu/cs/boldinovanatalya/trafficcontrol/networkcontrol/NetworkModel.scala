package ru.vsu.cs.boldinovanatalya.trafficcontrol.networkcontrol

import breeze.plot.Figure
import ru.vsu.cs.boldinovanatalya.trafficcontrol.FuzzyNetwork
import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.TrainingElement
import ru.vsu.cs.boldinovanatalya.trafficcontrol.utils.Statistics
import ru.vsu.cs.traffic.TrafficModel
import ru.vsu.cs.traffic.event.{ModelStopped, ColorChanged, BeforeColorChanged}
import ru.vsu.cs.traffic.gui.SwingApp

import scala.collection.mutable.ListBuffer
import scala.swing.MainFrame

class NetworkModel(model: TrafficModel, network: FuzzyNetwork, maxParameterValues: TrainingElement, maxExtendCount: Int = 1) {
  var count = maxExtendCount
  val buffer = new ListBuffer[(Double, Double)]()

  val statistics = new Statistics(model)
  model.trafficLightEventHandlers += {
    case BeforeColorChanged(tl) =>
      // println("beforecolorchanged")
      if (tl == model.trafficLights(0)) {
        if (count > 0) {
          network.setInput(statistics.queuingRed / maxParameterValues.input._1, statistics.queuingGreen / maxParameterValues.input._2, statistics.approachingGreen / maxParameterValues.input._3)
          network.calculate()
          val extendedTime = network.getOutput()(0)
          buffer += ((model.currentTime, getExtendedTime(extendedTime) * maxParameterValues.output))
          tl.intersection.extendColor(getExtendedTime(extendedTime) * maxParameterValues.output)
         // println("{\n    \"input\" : [ %f, %f, %f ],\n    \"output\" : %f\n  }".format(statistics.queuingRed / maxParameterValues.input._1,statistics.queuingGreen / maxParameterValues.input._2,statistics.approachingGreen / maxParameterValues.input._3, getExtendedTime(extendedTime)))
          count -= 1
        }
      }
    case ColorChanged(tl) =>
      // println("color changed")
      count = maxExtendCount

    case _ => Unit
  }

  def getExtendedTime(time: Double) = {
    if (time > 0) {
      if (time > 1) 1.0 else time

    } else 0.0

  }
}
