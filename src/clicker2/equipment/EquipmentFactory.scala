package clicker2.equipment

import play.api.libs.json.JsValue

object EquipmentFactory {

  def createObject(equipmentType: String, data: JsValue): Equipment = {

    val eq: Equipment = if(equipmentType == "shovel"){
      new Shovels
    }else if(equipmentType == "excavator"){
      new Excavators
    }else if(equipmentType == "mine"){
      new GoldMines
    }else{
      null
    }

    eq.name = (data \ "name").as[String]
    eq.numberOwned = (data \ "numberOwned").as[Int]

    eq
  }

}
