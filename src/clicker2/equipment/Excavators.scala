package clicker2.equipment

class Excavators extends Equipment{

  this.name = "Excavator"

  override def goldPerSecond(): Double = {
    this.numberOwned * 10.0
  }
  override def goldPerClick(): Double = {
    this.numberOwned * 20.0
  }

  override def costOfNextPurchase(): Double = {
     200 * Math.pow(1.1, this.numberOwned)
  }

}
