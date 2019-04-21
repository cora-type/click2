package clicker2.equipment

import play.api.libs.json.{JsValue, Json}

abstract class Equipment {

  var numberOwned: Int = 0
  var name: String = ""

  def goldPerSecond(): Double

  def goldPerClick(): Double

  def costOfNextPurchase(): Double

  def buy(): Unit = {
    this.numberOwned += 1
  }


  def buttonText(): String = {
    val cost = this.costOfNextPurchase()
    val gpc = this.goldPerClick()
    val gps = this.goldPerSecond()
    this.name + f"\n$cost%1.0f gold\n$gpc%1.0f gpc\n$gps%1.0f gps\nowned: " + this.numberOwned
  }

  def toJsValue(): JsValue = {
    Json.toJson(Map("numberOwned" -> Json.toJson(this.numberOwned), "name" -> Json.toJson(this.name), "buttonText" -> Json.toJson(this.buttonText())))
  }

}
