package com.codenjoy.dojo.battlecity.client

import com.codenjoy.dojo.client.{Solver, WebSocketRunner}

object Main {


  def main(args: Array[String]): Unit = {

    val algorithm = sys.props.get("algorithm").map(decideAlgorithm).getOrElse(throw new RuntimeException("Algorithm is not setup"))
    val botUrl = sys.props.get("url").getOrElse(throw new RuntimeException("Url is not setup"))

    WebSocketRunner.runClient(
      botUrl,
      algorithm,
      new Board()
    )
  }

  def decideAlgorithm(param: String): Solver[Board] =
    param match {
      case "N" =>
        val featureFile = sys.props.get("featureFile").getOrElse(throw new RuntimeException("Feature file is not setup for running NN"))
        NNEngine(featureFile)
      case _ => throw new RuntimeException("Such algorithm doesn't exist")
    }
}
