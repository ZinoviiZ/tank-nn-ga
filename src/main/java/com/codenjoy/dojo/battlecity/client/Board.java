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

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Board extends AbstractBoard<Elements> {
    private Point lastMe;

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    @Override
    protected int inversionY(int y) { // TODO разобраться с этим чудом
        return size - 1 - y;
    }

    public List<Point> getBarriers() {
        return get(Elements.BATTLE_WALL,
                Elements.CONSTRUCTION,
                Elements.CONSTRUCTION_DESTROYED_DOWN,
                Elements.CONSTRUCTION_DESTROYED_UP,
                Elements.CONSTRUCTION_DESTROYED_LEFT,
                Elements.CONSTRUCTION_DESTROYED_RIGHT,
                Elements.CONSTRUCTION_DESTROYED_DOWN_TWICE,
                Elements.CONSTRUCTION_DESTROYED_UP_TWICE,
                Elements.CONSTRUCTION_DESTROYED_LEFT_TWICE,
                Elements.CONSTRUCTION_DESTROYED_RIGHT_TWICE,
                Elements.CONSTRUCTION_DESTROYED_LEFT_RIGHT,
                Elements.CONSTRUCTION_DESTROYED_UP_DOWN,
                Elements.CONSTRUCTION_DESTROYED_UP_LEFT,
                Elements.CONSTRUCTION_DESTROYED_RIGHT_UP,
                Elements.CONSTRUCTION_DESTROYED_DOWN_LEFT,
                Elements.CONSTRUCTION_DESTROYED_DOWN_RIGHT);
    }

    public boolean isEnemyAt(int x, int y){
        return isAIAt(x, y) || isRealAt(x, y);
    }


    public boolean isAIAt(int x, int y){
        Elements E = this.getAt(x, y);
        if(
                E == Elements.AI_TANK_UP     || E == Elements.AI_TANK_DOWN||
                E == Elements.AI_TANK_LEFT   || E == Elements.AI_TANK_RIGHT){
           return true;
        } return false;
    }

    public boolean isRealAt(int x, int y){
        Elements E = this.getAt(x, y);
        if(
                E == Elements.OTHER_TANK_UP  || E == Elements.OTHER_TANK_DOWN||
                E == Elements.OTHER_TANK_LEFT|| E == Elements.OTHER_TANK_RIGHT){
            return true;
        } return false;
    }



    public boolean is_horizontal_movement(int x, int y){
        Elements E = this.getAt(x, y);
        if(
                        E == Elements.AI_TANK_LEFT   || E == Elements.AI_TANK_RIGHT||
                        E == Elements.OTHER_TANK_LEFT|| E == Elements.OTHER_TANK_RIGHT){
            return true;
        } else
            return false;
    }

    public Optional<Point> getMe() {
        List<Point> points = get(Elements.TANK_UP,
                Elements.TANK_DOWN,
                Elements.TANK_LEFT,
                Elements.TANK_RIGHT);
        if (!points.isEmpty()) lastMe = points.get(0);
        return lastMe == null ? Optional.empty() : Optional.of(lastMe);
    }

    public List<Point> getEnemies() {
        return get(Elements.AI_TANK_UP,
                Elements.AI_TANK_DOWN,
                Elements.AI_TANK_LEFT,
                Elements.AI_TANK_RIGHT,
                Elements.OTHER_TANK_UP,
                Elements.OTHER_TANK_DOWN,
                Elements.OTHER_TANK_LEFT,
                Elements.OTHER_TANK_RIGHT);
    }

    public List<Point> getEnemRight(){
        return get(Elements.AI_TANK_RIGHT,
                Elements.OTHER_TANK_RIGHT);
    }
    public List<Point> getEnemLeft(){
        return get(Elements.AI_TANK_LEFT,
                Elements.OTHER_TANK_LEFT);
    }
    public List<Point> getEnemUp(){
        return get(Elements.AI_TANK_UP,
                Elements.OTHER_TANK_UP);
    }
    public List<Point> getEnemDown(){
        return get(Elements.AI_TANK_RIGHT,
                Elements.OTHER_TANK_UP);
    }


    public List<Point> getRealEnemRight(){
        return get(Elements.OTHER_TANK_RIGHT);
    }
    public List<Point> getRealEnemLeft(){
        return get(Elements.OTHER_TANK_LEFT);
    }
    public List<Point> getRealEnemUp(){
        return get(Elements.OTHER_TANK_UP);
    }
    public List<Point> getRealEnemDown(){
        return get(Elements.OTHER_TANK_UP);
    }





    public List<Point> getAI(){
        return get(Elements.AI_TANK_UP,
                Elements.AI_TANK_DOWN,
                Elements.AI_TANK_LEFT,
                Elements.AI_TANK_RIGHT);
    }

    public List<Point> getReal(){
        return get(Elements.OTHER_TANK_UP,
                Elements.OTHER_TANK_DOWN,
                Elements.OTHER_TANK_LEFT,
                Elements.OTHER_TANK_RIGHT);
    }

    public List<Point> getBullets() {
        return get(Elements.BULLET);
    }

    public boolean isGameOver() {
        return get(Elements.TANK_UP,
                Elements.TANK_DOWN,
                Elements.TANK_LEFT,
                Elements.TANK_RIGHT).isEmpty();
    }

    public Elements getAt(int x, int y) {
        if (isOutOfField(x, y)) {
            return Elements.BATTLE_WALL;
        }
        return super.getAt(x, y);
    }


    public boolean isBarrierAt(int x, int y) {
        if (isOutOfField(x, y)) {
            return true;
        }
        Elements c = this.getAt(x, y);
        if(c.power > 0) // c.power >= 0 for NOT passing DESTROYED walls
            return true;
        return false;
    }


    public boolean isBulletAt(int x, int y) {
        return getAt(x, y).equals(Elements.BULLET);
    }

    public boolean isBulletAt(Point point) {
        return isBulletAt(point.getX(), point.getY());
    }

    @Override
    public String toString() {
        Optional<Point> me = getMe();
        String me_pos = (me.isPresent() ? me.get().toString() : "KILLED");
        return String.format(
                        "My tank at: %s\n" +
                        "AI at: %s\n" +
                        "Real users at: %s\n" +

                        "Bullets at: %s\n",
                me_pos,
                getAI(),
                getReal(),
                getBullets());
    }

    public List<Double> extractFeatures() {
        List<Double> features = new ArrayList<>();
        Point me = getMe().get();
        int x = me.getX(), y = me.getY();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (i == 4 && j == 4) continue;
                if (isOutOfField(x + i - 4, y + j - 4)) {
                    features.add(0.0);
                } else {
                    features.add(getAt(x + i - 4, y + j - 4).encode());
                }
            }
        }
        features.add((double)len(me, Direction.UP));
        features.add((double)len(me, Direction.DOWN));
        features.add((double)len(me, Direction.LEFT));
        features.add((double)len(me, Direction.RIGHT));
        List<Point> enemies = getEnemies();
        enemies.sort((Point p1, Point p2) -> Integer.compare(manhattan(me, p1), manhattan(me, p2)));
        for (int i = 0; i < 5; i++) {
            features.add((double)(enemies.get(i).getX() - me.getX()));
            features.add((double)(enemies.get(i).getY() - me.getY()));
        }
        return features;
    }

    public int len(Point p, Direction d) {
        int x = p.getX(), y = p.getY();
        while (!isOutOfField(x, y) && getAt(x, y) == Elements.NONE) {
            if (d == Direction.UP) {
                y--;
            } else if (d == Direction.DOWN) {
                y++;
            } else if (d == Direction.LEFT) {
                x--;
            } else if (d == Direction.RIGHT) {
                x++;
            }
        }
        return manhattan(p, pt(x, y));
    }

    public int manhattan(Point p1, Point p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    private final int INF = 1000*1000;

    int[][] bfs(int x_from, int y_from){

        int[][] res = new int[size][size];
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                res[i][j] = INF;

        res[x_from][y_from] = 0;
        LinkedList<int[]> Q = new LinkedList<>();
        Q.addLast(new int[]{x_from, y_from});

        while(!Q.isEmpty()){
            int[] pos = Q.getFirst();
            Q.removeFirst();

            int x = pos[0], y = pos[1];
            int dist = res[x][y] + 1;

            if(!isBarrierAt(x - 1, y) && res[x - 1][y] == INF) {
                res[x - 1][y] = dist;
                Q.addLast(new int[]{x - 1, y});
            }
            if(!isBarrierAt(x + 1, y) && res[x + 1][y] == INF){
                res[x + 1][y] = dist;
                Q.addLast(new int[]{x + 1, y});
            }
            if(!isBarrierAt(x, y + 1) && res[x][y + 1] == INF){
                res[x][y + 1] = dist;
                Q.addLast(new int[]{x, y + 1});
            }
            if(!isBarrierAt(x, y - 1) && res[x][y - 1] == INF){
                res[x][y - 1] = dist;
                Q.addLast(new int[]{x, y - 1});
            }

        }

        return res;
    }


    public int getDist(Point p1, Point p2){
        int[][] dist = this.bfs(p1.getX(), p1.getY());
        return dist[p2.getX()][p2.getY()];
    }

    final Random random = new Random();

    public Direction getBestMove(Point To, Set<Point> denied_points){
        int[][] dist = this.bfs(To.getX(), To.getY());

        Point me = getMe().get();
        int x = me.getX(), y = me.getY();

        Direction[] Dir = new Direction[]{Direction.RIGHT, Direction.LEFT, Direction.UP, Direction.DOWN, Direction.STOP};
        int[] dx = new int[]{1, -1, 0, 0, 0};
        int[] dy = new int[]{0, 0, 1, -1, 0};
        int best = 0;


        for(int i = 1; i < 5; i++){
            if(denied_points.contains(pt(x + dx[best], y + dy[best])))
                best = i;
            if(denied_points.contains(pt(x + dx[i], y + dy[i])))
                continue;

            if(dist[x + dx[best] ][y + dy[best]] > dist[x + dx[i] ][y + dy[i]] ||
              (dist[x + dx[best] ][y + dy[best]] == dist[x + dx[i] ][y + dy[i]]
                      && random.nextBoolean())){
                best = i;
            }
        }
        System.out.println(String.format("R %d,L %d,U %d,P %d, S %d",
                dist[x + dx[0] ][y + dy[0]],
                dist[x + dx[1] ][y + dy[1]],
                dist[x + dx[2] ][y + dy[2]],
                dist[x + dx[3] ][y + dy[3]],
                dist[x + dx[4] ][y + dy[4]]));
        if(dist[x + dx[best]][y + dy[best]] == INF)
            return null;

        return Dir[best];
    }

    public Direction getBestMove(int x_to, int y_to){
        return getBestMove(pt(x_to, y_to), new HashSet<>());
    }

    public Direction getBestMove(int x_to, int y_to, Set<Point> denied_list){
        return getBestMove(pt(x_to, y_to), denied_list);
    }

    public void printBar(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++)
                System.out.print(isBarrierAt(j, i)? "#" : ' ');
            System.out.println();
        }
    }
}
