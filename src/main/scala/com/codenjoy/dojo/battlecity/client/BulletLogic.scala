package com.codenjoy.dojo.battlecity.client

import java.util.Set

import com.codenjoy.dojo.battlecity.client.BulletLogic.BULLET_MOVE
import com.codenjoy.dojo.battlecity.client.BulletVector._
import com.codenjoy.dojo.battlecity.client.Elements
import com.codenjoy.dojo.services.{Point, PointImpl}

import scala.collection.JavaConverters._

object BulletLogic {

    val BULLET_MOVE: Int = 2

    def findBullets(bord: Board, previousCache: java.util.Set[Bullet]) : Set[Bullet] = {
        val nextMoveOfPrevious = previousCache.asScala.map(_.nextMove()).filter(bord.isBulletAt).toSet
        val tankBullets = (findLeft(bord) ++ findRight(bord) ++ findUp(bord) ++ findDown(bord))
          .filter(b => bord.isBulletAt(b) && !bulletsContain(previousCache, b)).toSet
        (nextMoveOfPrevious ++ tankBullets).asJava
    }

    def findDangerousPoints(board: Board, previousCache: java.util.Set[Bullet]): Set[Point] = {
        findBullets(board, previousCache).asScala.flatMap(_.attackingDistanceOnNextStep()).map(b=>PointImpl.pt(b.xCor,b.yCor)).asJava
    }

    private def findLeft(board: Board): List[Bullet] = {
        board.get(Elements.AI_TANK_LEFT, Elements.OTHER_TANK_LEFT)
          .asScala
          .flatMap(eP => List(Bullet(eP.getX - 1, eP.getY), Bullet(eP.getX - 2, eP.getY)))
          .map(p => Bullet(p.getX, p.getY, RIGHT_LEFT))
          .toList
    }

    private def findRight(board: Board): List[Bullet] = {
        board.get(Elements.AI_TANK_RIGHT, Elements.OTHER_TANK_RIGHT)
          .asScala
          .flatMap(eP => List(Bullet(eP.getX + 1, eP.getY), Bullet(eP.getX + 2, eP.getY)))
          .map(p => Bullet(p.getX, p.getY, LEFT_RIGHT))
          .toList
    }

    private def findUp(board: Board): List[Bullet] = {
        board.get(Elements.AI_TANK_UP, Elements.OTHER_TANK_UP)
          .asScala
          .flatMap(eP => List(Bullet(eP.getX, eP.getY + 1), Bullet(eP.getX, eP.getY + 2)))
          .map(p => Bullet(p.getX, p.getY, DOWN_UP))
          .toList
    }

    private def findDown(board: Board): List[Bullet] = {
        board.get(Elements.AI_TANK_DOWN, Elements.OTHER_TANK_DOWN)
          .asScala
          .flatMap(eP => List(Bullet(eP.getX, eP.getY - 1), Bullet(eP.getX, eP.getY - 2)))
          .map(p => Bullet(p.getX, p.getY, UP_DOWN))
          .toList
    }

    def bulletsContain(bullets: Set[Bullet], specificBullet: Bullet): Boolean = {
        bullets.asScala.exists(b => b.xCor == specificBullet.xCor && b.yCor == specificBullet.yCor)
    }
}

case class Bullet(xCor: Int, yCor: Int, vector: BulletVector = ABSENT) extends PointImpl(xCor, yCor) {

    def nextMove(move: Int = BULLET_MOVE): Bullet = {
        vector match {
            case LEFT_RIGHT => Bullet(x + move, y, vector)
            case RIGHT_LEFT => Bullet(x - move, y, vector)
            case UP_DOWN => Bullet(x, y - move, vector)
            case DOWN_UP => Bullet(x, y + move, vector)
            case ABSENT => Bullet(x, y, vector)
        }
    }

    def attackingDistanceOnNextStep(): List[Bullet] =
        List(nextMove(0), nextMove(1), nextMove(2))
    def attackingDistanceOnNextStep2(): List[Bullet] =
        List(nextMove(1), nextMove(2))


    override def toString: String = s"[X=$xCor|Y=$yCor|V=$vector]"
}
