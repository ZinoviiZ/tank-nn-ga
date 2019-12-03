package com.codenjoy.dojo.battlecity.client

import com.codenjoy.dojo.client.Solver
import com.codenjoy.dojo.services.Direction

class NNEngine(matrix1: Matrix, matrix2: Matrix, matrix3: Matrix) extends Solver[Board] {

  override def get(board: Board): String = {
    if(board.isGameOver) return ""

    mapResult2Position(
      BoardProcessor.processBoard(
        matrix1.value,
        matrix2.value,
        matrix3.value,
        board
      )
    )
  }

  /** Final decision of the NN mapped to he action*/
  def mapResult2Position(result: Int): String = {
    result match {
      case 0 => Direction.LEFT.toString   + ',' + Direction.ACT.toString
      case 1 => Direction.RIGHT.toString  + ',' + Direction.ACT.toString
      case 2 => Direction.DOWN.toString   + ',' + Direction.ACT.toString
      case 3 => Direction.UP.toString     + ',' + Direction.ACT.toString
      case 4 => Direction.ACT.toString
      case 5 => Direction.LEFT.toString
      case 6 => Direction.RIGHT.toString
      case 7 => Direction.DOWN.toString
      case 8 => Direction.UP.toString
      case 9 => ""
      case r => throw new RuntimeException(s"Incorrect max index in last NN result, should be [0..9] but actually is $r")
    }
  }
}

object NNEngine {

  def apply(filePath: String): NNEngine = {
    val (matrix1, matrix2, matrix3) = FileParser.parseFile(filePath)
    new NNEngine(matrix1, matrix2, matrix3)
  }
}

