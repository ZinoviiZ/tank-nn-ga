package com.codenjoy.dojo.battlecity.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import scala.collection.JavaConverters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TankLogic {
    public static List<Tank> getTanks(Board board){
        ArrayList<Tank> res = new ArrayList<Tank>();

        List<Point> tmp;
        tmp = board.getEnemDown();
        for(Point p : tmp){
            res.add(new Tank(p, QDirection.DOWN));
        }
        tmp = board.getEnemUp();
        for(Point p : tmp){
            res.add(new Tank(p, QDirection.UP));
        }
        tmp = board.getEnemLeft();
        for(Point p : tmp){
            res.add(new Tank(p, QDirection.LEFT));
        }
        tmp = board.getEnemRight();
        for(Point p : tmp){
            res.add(new Tank(p, QDirection.RIGHT));
        }
        Point me = board.getMe().get();
        for(Tank t: res)
            if(me.getX() == t.getX() && me.getY() == t.getY()){
                System.out.println("FUCasdhjbadasdjbajdbasjd asdas dbas!!!!!d");
            }

        return res;
    }



    public static List<Tank> getRealTanks(Board board){
        ArrayList<Tank> res = new ArrayList<Tank>();

        List<Point> tmp;
        tmp = board.getRealEnemDown();
        for(Point p : tmp){
            res.add(new Tank(p, QDirection.DOWN));
        }

        tmp = board.getRealEnemUp();
        for(Point p : tmp){
            res.add(new Tank(p, QDirection.UP));
        }

        tmp = board.getRealEnemLeft();
        for(Point p : tmp){
            res.add(new Tank(p, QDirection.LEFT));
        }

        tmp = board.getRealEnemRight();
        for(Point p : tmp){
            res.add(new Tank(p, QDirection.RIGHT));
        }

        Point me = board.getMe().get();

        return res;
    }
    public static void makeMove(List<Tank> T){
        for(Tank t : T)
            t.moveForward();
    }

    public static List<Tank> copy_tanks(List<Tank> T){
        ArrayList<Tank> TT = new ArrayList<Tank>();
        for(Tank t : T)
            TT.add(t.clone());
        return TT;
    }

    public static int shouldFireDistNORMAL(Point P, BulletVector dir, List<Tank> T, Board board){
        int MAX_DIST = 15;
        int MAX_DIST_PLAYER = 7;

        int MAX_DIST_KILL_ANYDIR = 3;


        Bullet bullet = new Bullet(P.getX(), P.getY(), dir);
        scala.collection.immutable.List<Bullet> kaboom_points = bullet.attackingDistanceOnNextStep2();

        System.out.println(kaboom_points);

        for(Bullet b : JavaConverters.seqAsJavaListConverter(kaboom_points).asJava()){
            if((b.distance(P) == 2 &&
                    !board.isBarrierAt(
                            (b.getX() + P.getX() ) / 2,
                            (b.getY() + P.getY() ) / 2))
                || b.distance(P) == 1 ) {
                for (Tank t : T) {
                    Tank t_next = t.clone();
                    t_next.moveForward();
                    if (t_next.getX() == b.getX() && t_next.getY() == b.getY())
                        return 1;
                }
            }
        }

        Random random = new Random();

        if(dir == BulletVector.LEFT_RIGHT){
            for(int dx = 1; dx <= MAX_DIST; dx++) {
                int x = P.getX() + dx, y = P.getY();
                if (board.isBarrierAt(x, y))
                    break;
                if(board.isEnemyAt(x, y) && dx <= MAX_DIST_KILL_ANYDIR && random.nextBoolean())
                    return dx*2;
                if(board.isEnemyAt(x, y) && board.is_horizontal_movement(x, y))
                    if(dx <= MAX_DIST_PLAYER || board.isAIAt(x, y) )
                        return dx * 2;

            }
        }
        if(dir == BulletVector.RIGHT_LEFT){
            for(int dx = 1; dx <= MAX_DIST; dx++) {
                int x = P.getX() - dx, y = P.getY();
                if (board.isBarrierAt(x, y))
                    break;

                if(board.isEnemyAt(x, y) && dx <= MAX_DIST_KILL_ANYDIR && random.nextBoolean())
                    return dx*2;

                if(board.isEnemyAt(x, y) && board.is_horizontal_movement(x, y))
                    if(dx <= MAX_DIST_PLAYER || board.isAIAt(x, y))
                        return dx * 2;
            }
        }
        if(dir == BulletVector.DOWN_UP){
            for(int dy = 1; dy <= MAX_DIST; dy++) {
                int x = P.getX(), y = P.getY() + dy;
                if (board.isBarrierAt(x, y))
                    break;

                if(board.isEnemyAt(x, y) && dy <= MAX_DIST_KILL_ANYDIR && random.nextBoolean())
                    return dy*2;

                if(board.isEnemyAt(x, y) && !board.is_horizontal_movement(x, y))
                    if(dy <= MAX_DIST_PLAYER || board.isAIAt(x, y))
                        return dy * 2;
            }
        }
        if(dir == BulletVector.UP_DOWN){
            for(int dy = 1; dy <= MAX_DIST; dy++) {
                int x = P.getX(), y = P.getY() - dy;
                if (board.isBarrierAt(x, y))
                    break;

                if(board.isEnemyAt(x, y) && dy <= MAX_DIST_KILL_ANYDIR && random.nextBoolean())
                    return dy*2;

                if(board.isEnemyAt(x, y) && !board.is_horizontal_movement(x, y))
                    if(dy <= MAX_DIST_PLAYER || board.isAIAt(x, y))
                        return dy * 2;
            }
        }

        for(Bullet b : JavaConverters.seqAsJavaListConverter(kaboom_points).asJava()){
            if((b.distance(P) == 2 &&
                    !board.isBarrierAt(
                            (b.getX() + P.getX() ) / 2,
                            (b.getY() + P.getY()) / 2))
                    || b.distance(P) == 1 ) {
                for (Tank t : T) {
                    if (t.getX() == b.getX() && t.getY() == b.getY())
                        return 20;
                }
            }
        }


        return 999;
    }

    public static int shouldFireDist(Point P, BulletVector dir, List<Tank> T, Board board){
        System.out.println("To strike or not to strike?");
        Bullet B = new Bullet(P.getX(), P.getY(), dir);


        List<Tank> TT = copy_tanks(T);

        for(int i = 0; i < 5; i++){
            for (Tank t : TT) {
                t.moveForward();
                if(board.isBarrierAt(t.getX(), t.getY()) ||
                        (t.getX() == P.getX() && t.getY() == P.getY())){
                    t.moveBackward();
                }
            }
            scala.collection.immutable.List<Bullet> attack = B.attackingDistanceOnNextStep2();


            for(Bullet bullet : JavaConverters.seqAsJavaListConverter(attack).asJava()){
                if(board.isBarrierAt(bullet.getX(), bullet.getY()))
                    return 100;
                for(Tank t: TT)
                    if(t.getX() == bullet.getX() && t.getY() == bullet.getY()) {
                        System.out.println(String.format("TANK at (%d, %d) in %d", t.getX(), t.getY(), i));
                        return i;
                    }
            }

            B.nextMove(BulletLogic.BULLET_MOVE());
        }
        return 100;
    }


}
