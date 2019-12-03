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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;


import static com.codenjoy.dojo.services.PointImpl.pt;

public class Tank implements Point {
    @Override
    public int compareTo(Point point) {
        return 0;
    }

    int x, y;
    public final QDirection dir;

    public Tank(Point p, QDirection dir){
        this.x = p.getX();
        this.y = p.getY();
        this.dir = dir;
    }

    public Tank(Point me, Board board){
        this.x = me.getX();
        this.y= me.getY();
        Elements me_pos = board.getAt(me);
        switch (me_pos){
            case TANK_UP: this.dir = QDirection.UP; break;
            case TANK_DOWN: this.dir = QDirection.DOWN; break;
            case TANK_LEFT: this.dir = QDirection.LEFT;break;
            case TANK_RIGHT: this.dir = QDirection.RIGHT;break;
            default: this.dir = null;
        }

    }

    public BulletVector getBullet(){
        switch (dir){
            case RIGHT: return BulletVector.LEFT_RIGHT;
            case LEFT: return BulletVector.RIGHT_LEFT;
            case DOWN: return BulletVector.UP_DOWN;
            case UP: return BulletVector.DOWN_UP;
        }
        return BulletVector.ABSENT;
    }


    public void moveForward(){
        this.change(dir);
    }
    public void moveBackward(){
        this.change_oppsite(dir);
    }


    public Point[] getDangPoint(){
        Point[] res = new Point[2];
        Tank t = this.copy();
        t.moveForward();
        res[0] = t;
        t.moveForward();
        res[1] = t;
        return res;
    }

    public Point getXY(){
        return pt(this.x, this.y);
    }
    @Override
    public int getX(){
        return this.x;
    }

    @Override
    public int getY(){
        return this.y;
    }

    @Override
    public void move(int x, int y){
        throw new SecurityException("Tank has limited thing it can do");
    }
    @Override
    public void move(Point p){
        throw new SecurityException("Tank has limited thing it can do");
    }
    @Override
    public Tank copy(){
        return new Tank(this.getXY(), this.dir);
    }
    public Tank clone(){
        return copy();
    }

    @Override
    public void setX(int x) {
        throw new SecurityException("Tank has limited thing it can do");
    }

    @Override
    public void setY(int y) {
        throw new SecurityException("Tank has limited thing it can do");
    }

    @Override
    public boolean itsMe(Point point) {
        throw new SecurityException("Tank has limited thing it can do");
    }

    @Override
    public boolean itsMe(int x, int y) {
        throw new SecurityException("Tank has limited thing it can do");
    }

    @Override
    public boolean isOutOf(int size) {
        throw new SecurityException("Tank has limited thing it can do");
    }

    @Override
    public boolean isOutOf(int dw, int dh, int size) {
        throw new SecurityException("Tank has limited thing it can do");
    }

    @Override
    public double distance(Point point2) {
        throw new SecurityException("Tank has limited thing it can do, not DISTANCE");
    }

    @Override
    public void change(Point delta) {
        throw new SecurityException("Tank has limited thing it can do, not CHANGE");

    }

    @Override
    public void change(Direction direction){
        throw new SecurityException("Tank has limited thing it can do, not CHANGE");
    }


    @Override
    public void change(QDirection direction) {
        x = direction.changeX(x);
        y = direction.changeY(y);
    }

    public void change_oppsite(QDirection direction) {
        QDirection dirInv = direction.inverted();
        x = dirInv.changeX(x);
        y = dirInv.changeY(y);
    }

    @Override
    public String toString(){
        return String.format("(%d, %d)", this.x, this.y);
    }
}


