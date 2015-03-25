package ru.vsu.cs.boldinovanatalya.trafficcontrol.utils


import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.TrainingElement
import ru.vsu.cs.traffic.Color.{YELLOW, RED, GREEN}
import ru.vsu.cs.traffic.Direction.FORWARD
import ru.vsu.cs.traffic.event.{ModelActed, ColorChanged, BeforeColorChanged, VehicleSpawned}
import ru.vsu.cs.traffic.{Color, Point, TrafficModel}

import scala.collection.mutable.ListBuffer

class Statistics(model: TrafficModel, interval: Double = 10) {
  private val MinSpeed = 3

  private var approachingGreen_ = 0.0
  private var _maxQueuing = 0.0
  private var _maxApproaching = 0.0
  private var _currentQueuing = ListBuffer[(Double, Double)]()
  private var _lastTime = 0.0

  def maxQueuing = _maxQueuing

  def maxApproaching = _maxApproaching

  def currentQueuing = _currentQueuing.toList
  def averageQueuing = _currentQueuing.map(_._2).sum / _currentQueuing.length


  model.vehicleEventHandlers += {
    case VehicleSpawned(vehicle) =>
      if (model.trafficLights.filter(_.color == GREEN).map(_(FORWARD)).contains(vehicle.trafficFlow)) {
        approachingGreen_ += 1
      }
    case _ => Unit
  }

  model.trafficLightEventHandlers += {
    case ColorChanged(tl) =>
      approachingGreen_ = 0
    case _ => Unit
  }

  model.trafficModelEventHandlers += {
    case ModelActed(time) =>
      if(time - _lastTime > interval) {
        _currentQueuing += ((time, queuingGreen + queuingRed))
        _lastTime = time
      }
      if (approachingGreen > _maxApproaching) _maxApproaching = approachingGreen
      val currentQueuing = math.max(queuingGreen, queuingRed)
      if (currentQueuing > _maxQueuing) _maxQueuing = currentQueuing
    case _ => Unit
  }

  def approachingGreen = {
    approachingGreen_
  }

  private def queuing(color: Color): Double = {
    model.trafficLights.filter(_.color == color).map(_(FORWARD).vehicles.count(_.speed <= MinSpeed)).sum
  }

  def queuingGreen = queuing(GREEN)

  def queuingRed = queuing(RED)


}
