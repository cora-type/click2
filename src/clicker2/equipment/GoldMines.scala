package clicker2.equipment

class GoldMines extends Equipment{

  this.name = "Gold Mine"

  override def goldPerSecond(): Double = {
    this.numberOwned * 100
  }
  override def goldPerClick(): Double = {
    0.0
  }

  override def costOfNextPurchase(): Double = {
    1000 * Math.pow(1.1, this.numberOwned)
  }

}
