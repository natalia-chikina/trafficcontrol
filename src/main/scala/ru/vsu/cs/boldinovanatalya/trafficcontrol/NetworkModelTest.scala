package ru.vsu.cs.boldinovanatalya.trafficcontrol

import java.awt.Graphics2D

import breeze._
import breeze.plot.Figure
import org.neuroph.core.NeuralNetwork
import ru.vsu.cs.boldinovanatalya.trafficcontrol.geneticalgorithm.TrainingElement
import ru.vsu.cs.boldinovanatalya.trafficcontrol.networkcontrol.NetworkModel
import ru.vsu.cs.boldinovanatalya.trafficcontrol.utils.Statistics
import ru.vsu.cs.traffic.Color.{YELLOW, RED, GREEN}
import ru.vsu.cs.traffic.{Point, TrafficModel}
import ru.vsu.cs.traffic.gui.{TrafficModelPanel, SwingApp}

object NetworkModelTest extends SwingApp {

  override val model = createModel(30)

  val nwDemo = NeuralNetwork.createFromFile("resultNN.nnet").asInstanceOf[FuzzyNetwork]

//  val networkModel = new NetworkModel(model, nwDemo, new TrainingElement((25, 20, 15.0), 10))

  val statistics = new Statistics(model)
  override val panel: TrafficModelPanel = new TrafficModelPanel(model) {
    override def paintComponent(g: Graphics2D): Unit = {
      super.paintComponent(g)
      g.drawString(statistics.averageQueuing.toString, 50, 60)
      g.drawString(model.currentTime.toString, 50, 70)
    }

  }



  model.run()


}
