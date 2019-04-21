package clicker2.desktopapp

import io.socket.client.{IO, Socket}
import io.socket.emitter.Emitter
import javafx.application.Platform
import javafx.event.ActionEvent
import play.api.libs.json.{JsValue, Json}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.layout.GridPane

class HandleMessagesFromPython() extends Emitter.Listener {
  override def call(objects: Object*): Unit = {
    // Use runLater when interacting with the GUI
    Platform.runLater(() => {
      val jsonGameState = objects.apply(0).toString
      println(jsonGameState)
      val gameState: JsValue = Json.parse(jsonGameState)
      val gold = (gameState \ "gold").as[Double]

      DesktopGUI.goldDisplay.text = Math.round(gold).toString

      val mapping = (gameState \ "equipment").as[Map[String, JsValue]]
      for ((k, v) <- mapping) {
        DesktopGUI.equipmentButtons(k).text = (v \ "buttonText").as[String]
      }
    })

  }
}

class MyButton(xScale: Double, yScale: Double) extends Button {
  minWidth = 100 * xScale
  minHeight = 100 * yScale
  style = "-fx-font: 12 ariel;"
}

class DigGoldButton(socket: Socket, xScale: Double = 1.0, yScale: Double = 1.0) extends MyButton(xScale, yScale) {
  text = "Gold!"
  style = "-fx-font: 24 ariel;"
  onAction = (event: ActionEvent) => socket.emit("clickGold")
}


class BuyEquipmentButton(val equipmentKey: String, socket: Socket, xScale: Double = 1.0, yScale: Double = 1.0) extends MyButton(xScale, yScale) {
  onAction = (event: ActionEvent) => socket.emit("buy", equipmentKey)
}


object DesktopGUI extends JFXApp {


  var socket: Socket = IO.socket("http://localhost:8080/")
  socket.on("message", new HandleMessagesFromPython)


  socket.connect()
  socket.emit("register", "myUsername")


  //  https://index.scala-lang.org/andyglow/websocket-scala-client/websocket-scala-client/0.2.4?target=_2.12
  //  https://www.codenuclear.com/websocket-client-api-in-java-9-with-example/


  var goldDisplay: TextField = new TextField {
    editable = false
    style = "-fx-font: 18 ariel;"
  }

  val digButton = new DigGoldButton(socket, 2, 2)

  val equipmentList: List[String] = List("shovel", "excavator", "mine")
  val equipmentButtons: Map[String, BuyEquipmentButton] = equipmentList.map((equipmentKey: String) => (equipmentKey, new BuyEquipmentButton(equipmentKey, socket))).toMap

  this.stage = new PrimaryStage {
    title = "CSE Clicker"
    scene = new Scene() {
      content = List(
        new GridPane {
          add(digButton, 0, 0, 2, 2)
          add(goldDisplay, 0, 2)
          equipmentList.indices.foreach(i => add(equipmentButtons(equipmentList.apply(i)), 2, i))
        }
      )
    }

  }

}
