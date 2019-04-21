package clicker2.equipment

class Shovels extends Equipment {

  this.name = "Shovel"

  override def goldPerSecond(): Double = {
    0.0
  }
  override def goldPerClick(): Double = {
    this.numberOwned * 1.0
  }

  override def costOfNextPurchase(): Double = {
    10 * Math.pow(1.05, this.numberOwned)
  }

}
