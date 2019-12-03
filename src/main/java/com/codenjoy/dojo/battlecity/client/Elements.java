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

import com.codenjoy.dojo.services.printer.CharElements;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum Elements implements CharElements {

    NONE(' ', 3000.0),
    BATTLE_WALL('☼', 1000, 1.0),
    BANG('Ѡ', 0.0),

    CONSTRUCTION('╬', 3, 20.0),

    CONSTRUCTION_DESTROYED_DOWN('╩', 2, 22.0),
    CONSTRUCTION_DESTROYED_UP('╦', 2, 24.0),
    CONSTRUCTION_DESTROYED_LEFT('╠', 2, 26.0),
    CONSTRUCTION_DESTROYED_RIGHT('╣', 2, 28.0),

    CONSTRUCTION_DESTROYED_DOWN_TWICE('╨', 1, 36.0),
    CONSTRUCTION_DESTROYED_UP_TWICE('╥', 1, 38.0),
    CONSTRUCTION_DESTROYED_LEFT_TWICE('╞', 1, 40.0),
    CONSTRUCTION_DESTROYED_RIGHT_TWICE('╡', 1, 42.0),

    CONSTRUCTION_DESTROYED_LEFT_RIGHT('│', 1, 50.0),
    CONSTRUCTION_DESTROYED_UP_DOWN('─', 1, 54.0),

    CONSTRUCTION_DESTROYED_UP_LEFT('┌', 1, 64.0),
    CONSTRUCTION_DESTROYED_RIGHT_UP('┐', 1, 68.0),
    CONSTRUCTION_DESTROYED_DOWN_LEFT('└', 1, 72.0),
    CONSTRUCTION_DESTROYED_DOWN_RIGHT('┘', 1, 76.0),

    CONSTRUCTION_DESTROYED(' ', 0, 100.0),

    BULLET('•', 4048.0),

    TANK_UP('▲', 512.0),
    TANK_RIGHT('►', 513.0),
    TANK_DOWN('▼', 514.0),
    TANK_LEFT('◄', 515.0),

    OTHER_TANK_UP('˄', 1024.0),
    OTHER_TANK_RIGHT('˃', 1025.0),
    OTHER_TANK_DOWN('˅', 1026.0),
    OTHER_TANK_LEFT('˂', 1027.0),

    AI_TANK_UP('?', 2048.0),
    AI_TANK_RIGHT('»', 2049.0),
    AI_TANK_DOWN('¿', 2050.0),
    AI_TANK_LEFT('«', 2051.0);

    public final char ch;
    public final int power;
    public final double encoding;

    private static List<Elements> result = null;
    public static Collection<Elements> getConstructions() {
        if (result == null) {
            result = Arrays.stream(values())
                    .filter(e -> e.name().startsWith(CONSTRUCTION.name()))
                    .collect(toList());
        }
        return result;
    }

    @Override
    public char ch() {
        return ch;
    }

    Elements(char ch) {
        this.ch = ch;
        this.power = -1;
	this.encoding = -1;
    }

    Elements(char ch, int power) {
        this.ch = ch;
        this.power = power;
	this.encoding = -1;
    }

    Elements(char ch, double encoding) {
        this.ch = ch;
	this.power = -1;
        this.encoding = encoding;
    }

    Elements(char ch, int power, double encoding) {
        this.ch = ch;
        this.power = power;
        this.encoding = encoding;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

    public double encode() {
        return encoding;
    }
}
