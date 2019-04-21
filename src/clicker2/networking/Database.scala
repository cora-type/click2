package clicker2.networking

import java.sql.{Connection, DriverManager, ResultSet}

import clicker2.Game

object Database {


  val url = "jdbc:mysql://localhost/mysql?serverTimezone=UTC"
  val username = "root"
  val password = "12345678"
  var connection: Connection = DriverManager.getConnection(url, username, password)

  /*
   * Change the username/password to your own, but do not modify the methods in this file. Your Database.scala
   * file will be deleted and replaced during testing so any changes you make will not be reflected in AutoLab.
   *
   * There are no objectives to complete here. You should read through the methods and determine
   * what they do, then call the methods you need to be able to complete the objectives
  */

  setupTable()

  def setupTable(): Unit = {
    val statement = connection.createStatement()
    statement.execute("CREATE TABLE IF NOT EXISTS players (username TEXT, gold DOUBLE, shovels INT, excavators INT, mines INT, lastUpdate BIGINT)")
  }


  def playerExists(username: String): Boolean = {
    val statement = connection.prepareStatement("SELECT * FROM players WHERE username=?")

    statement.setString(1, username)
    val result: ResultSet = statement.executeQuery()

    result.next()
  }


  def createPlayer(username: String): Unit = {
    val statement = connection.prepareStatement("INSERT INTO players VALUE (?, ?, ?, ?, ?, ?)")

    statement.setString(1, username)
    statement.setDouble(2, 0.0)
    statement.setInt(3, 0)
    statement.setInt(4, 0)
    statement.setInt(5, 0)
    statement.setLong(6, System.nanoTime())

    statement.execute()
  }


  def saveGame(username: String, gold: Double, shovels: Int, excavators: Int, mines: Int, lastUpdate: Long): Unit = {
    val statement = connection.prepareStatement("UPDATE players SET gold = ?, shovels = ?, excavators = ?, mines = ?, lastUpdate = ? WHERE username = ?")

    statement.setDouble(1, gold)
    statement.setInt(2, shovels)
    statement.setInt(3, excavators)
    statement.setInt(4, mines)
    statement.setLong(5, lastUpdate)
    statement.setString(6, username)

    statement.execute()
  }


  def loadGame(username: String, game: Game): Unit = {
    val statement = connection.prepareStatement("SELECT * FROM players WHERE username=?")
    statement.setString(1, username)
    val result: ResultSet = statement.executeQuery()

    result.next()
    game.gold = result.getDouble("gold")
    game.equipment("shovel").numberOwned = result.getInt("shovels")
    game.equipment("excavator").numberOwned = result.getInt("excavators")
    game.equipment("mine").numberOwned = result.getInt("mines")
    game.lastUpdateTime = result.getLong("lastUpdate")
  }


}
