package ru.vsu.cs.boldinovanatalya.trafficcontrol

import org.neuroph.core.NeuralNetwork
import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.TrainingElement
import ru.vsu.cs.boldinovanatalya.trafficcontrol.networkcontrol.NetworkModel
import ru.vsu.cs.boldinovanatalya.trafficcontrol.utils.Statistics
import ru.vsu.cs.traffic.Color.{YELLOW, RED, GREEN}
import ru.vsu.cs.traffic.{Point, TrafficModel}
import ru.vsu.cs.traffic.gui.{TrafficModelPanel, SwingApp}

object NetworkModelTest extends SwingApp {
  override val model: TrafficModel = TrafficModel(0.5, false)

  model.addFlow(Point(250, 0), Point(250, 500), 3, _ => 0.8)
  model.addFlow(Point(0, 250), Point(500, 250), 3, _ => 1.3)
  model.intersections(0)(model.trafficFlows(2)).durations = Map(GREEN -> 30, RED -> 15, YELLOW -> 0)
  model.intersections(0)(model.trafficFlows(3)).durations = Map(GREEN -> 30, RED -> 15, YELLOW -> 0)
  model.intersections(0)(model.trafficFlows(0)).durations = Map(GREEN -> 15, RED -> 30, YELLOW -> 0)
  model.intersections(0)(model.trafficFlows(1)).durations = Map(GREEN -> 15, RED -> 30, YELLOW -> 0)

  val nwDemo = NeuralNetwork.createFromFile("resultNN.nnet").asInstanceOf[FuzzyNetwork]

  //val networkModel = new NetworkModel(model, nwDemo, new TrainingElement((24.0, 24.0, 12.0), 40))

  val statistics = new Statistics(model)
  override val panel: TrafficModelPanel = new TrafficModelPanel(model)
  model.run()
}
