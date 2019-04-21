package clicker2

import clicker2.equipment._
import play.api.libs.json.{JsValue, Json}

class Game(username: String) {

  var gold: Double = 0.0
  var lastUpdateTime: Long = System.nanoTime()
  var equipment: Map[String, Equipment] = Map("shovel" -> new Shovels, "excavator" -> new Excavators, "mine" -> new GoldMines)

  def goldPerSecond(): Double = {
    var gps = 0.0
    for ((_, equipment) <- this.equipment) {
      gps += equipment.goldPerSecond()
    }
    gps
  }

  def goldPerClick(): Double = {
    var gpc = 1.0
    for ((_, equipment) <- this.equipment) {
      gpc += equipment.goldPerClick()
    }
    gpc
  }


  def toJSON(): String = {
    val gameState: Map[String, JsValue] = Map(
      "username" -> Json.toJson(this.username),
      "gold" -> Json.toJson(this.gold),
      "lastUpdateTime" -> Json.toJson(this.lastUpdateTime),
      "equipment" -> Json.toJson(this.equipment.map({case(k,v) => (k, v.toJsValue())}))
    )

    Json.stringify(Json.toJson(gameState))
  }


  def clickGold(): Unit = {
    gold += this.goldPerClick()
  }


  def buyEquipment(equipmentKey: String): Unit = {
    val equipmentToBuy = this.equipment.getOrElse(equipmentKey, null)
    if (equipmentToBuy != null && equipmentToBuy.costOfNextPurchase() <= this.gold) {
      this.gold -= equipmentToBuy.costOfNextPurchase()
      equipmentToBuy.buy()
    }
  }


  def update(time: Long): Unit = {
    val dt = (time - this.lastUpdateTime) / 1000000000.0
    gold += dt * this.goldPerSecond()
    this.lastUpdateTime = time
  }

}
