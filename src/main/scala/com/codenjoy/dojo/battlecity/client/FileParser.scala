package com.codenjoy.dojo.battlecity.client

import scala.io.Source

object FileParser {

  /** NN layers*/
  val M1_W = 95
  val M1_H = 25

  val M2_W = 26
  val M2_H = 25

  val M3_W = 26
  val M3_H = 10

  def parseFile(filePath: String): (Matrix, Matrix, Matrix) = {

    val lines = Source.fromFile(filePath).getLines().toArray
    assert(lines.size == 1)
    val numbers = lines(0).split(" ").map(_.toDouble)

    assert(numbers.size == (M1_H * M1_W + M2_H * M2_W + M3_H * M3_W))
    val (matrix1, rest1) = buildMatrix(numbers, M1_W * M1_H, M1_W)
    matrix1.assertHeightAndWidth(M1_H, M1_W)

    assert(rest1.size == (M2_H * M2_W + M3_H * M3_W))
    val (matrix2, rest2) = buildMatrix(rest1, M2_W * M2_H, M2_W)
    matrix2.assertHeightAndWidth(M2_H, M2_W)

    assert(rest2.size == (M3_H * M3_W))
    val (matrix3, rest3) = buildMatrix(rest2, M3_W * M3_H, M3_W)
    matrix3.assertHeightAndWidth(M3_H, M3_W)

    (matrix1, matrix2, matrix2)
  }

  private def buildMatrix(numbers: Array[Double], size: Int, width: Int): (Matrix, Array[Double]) = {
    (Matrix(numbers.take(size).grouped(width).toArray), numbers.drop(size))
  }
}

case class Matrix(value: Array[Array[Double]]) {

  def assertHeightAndWidth(height: Int, width: Int): Unit = {
    assert(value.size == height)
    value.foreach(row => assert(row.size == width))
  }
}
