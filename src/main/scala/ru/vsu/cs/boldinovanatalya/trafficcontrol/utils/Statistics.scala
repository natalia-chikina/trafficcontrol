package ru.vsu.cs.boldinovanatalya.trafficcontrol.utils


import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.TrainingElement
import ru.vsu.cs.traffic.Color.{YELLOW, RED, GREEN}
import ru.vsu.cs.traffic.Direction.FORWARD
import ru.vsu.cs.traffic.event._
import ru.vsu.cs.traffic.{Vehicle, Color, Point, TrafficModel}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

class Statistics(model: TrafficModel, interval: Double = 1) {
  private val MinSpeed = 3

  private var approachingGreen_ = 0.0
  private var _maxQueuing = 0.0
  private var _maxApproaching = 0.0
  private var _currentQueuing = ListBuffer[(Double, Double)]()
  private var _lastTime = 0.0

  private var _spawnedTime = mutable.Map[Vehicle, Double]()
  private var _transitionTime = mutable.Map[Vehicle, Double]()

  def maxQueuing = _maxQueuing
  def maxApproaching = _maxApproaching
  def currentQueuing = _currentQueuing.toList
  def averageQueuing = _currentQueuing.map(_._2).sum / _currentQueuing.length
  def averageTransitionTime = _transitionTime.map(_._2).sum / _transitionTime.size


  model.vehicleEventHandlers += {
    case VehicleSpawned(vehicle) =>
      _spawnedTime += vehicle -> model.currentTime
      if (model.trafficLights.filter(_.color == GREEN).map(_(FORWARD)).contains(vehicle.trafficFlow)) {
        approachingGreen_ += 1
      }
    case VehicleRemoved(vehicle) =>
      _transitionTime += vehicle -> (model.currentTime - _spawnedTime(vehicle))

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
