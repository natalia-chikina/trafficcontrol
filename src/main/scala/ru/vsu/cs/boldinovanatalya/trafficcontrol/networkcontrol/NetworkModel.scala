package ru.vsu.cs.boldinovanatalya.trafficcontrol.networkcontrol

import ru.vsu.cs.boldinovanatalya.trafficcontrol.FuzzyNetwork
import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.TrainingElement
import ru.vsu.cs.boldinovanatalya.trafficcontrol.utils.Statistics
import ru.vsu.cs.traffic.TrafficModel
import ru.vsu.cs.traffic.event.{ColorChanged, BeforeColorChanged}
import ru.vsu.cs.traffic.gui.SwingApp

import scala.swing.MainFrame

class NetworkModel(model: TrafficModel, network: FuzzyNetwork, maxParameterValues: TrainingElement, maxExtendCount: Int = 1) {
  var count = maxExtendCount * model.trafficLights.length
  val statistics = new Statistics(model)
 model.trafficLightEventHandlers += {
   case BeforeColorChanged(tl) =>
     println("beforecolorchanged")
     if(count > 0 ) {
       network.setInput(statistics.queuingRed/maxParameterValues.input._1,statistics.queuingGreen/maxParameterValues.input._2, statistics.approachingGreen/maxParameterValues.input._3)
       network.calculate()
       val extendedTime = network.getOutput()(0)
       tl.extendColor(getExtendedTime(extendedTime) * maxParameterValues.output)
       println(extendedTime)
       count -= 1
     }
   case ColorChanged(tl) =>
     println("color changed")
     count = maxExtendCount * model.trafficLights.length

   case _ => Unit
 }

  def getExtendedTime(time: Double) = {
    if(time > 0) {
      if(time > 1) 1.0 else time

    } else 0.0

  }
}
