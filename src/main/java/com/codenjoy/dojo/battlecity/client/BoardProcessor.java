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

import com.codenjoy.dojo.battlecity.client.Board;
import java.util.stream.Collectors;
import java.util.Collections;

import java.util.List;
import java.util.ArrayList;

public class BoardProcessor {

    static int processBoard(double[][] matrix1, double[][] matrix2, double[][] matrix3, Board board) {
        List<Double> features = board.extractFeatures();
        features.add(1.0);
        features = multiply(matrix1, features);
        features = features.stream().map(Math::tanh).collect(Collectors.toList());

        features.add(1.0);
        features = multiply(matrix2, features);
        features = features.stream().map(Math::tanh).collect(Collectors.toList());

        features.add(1.0);
        features = multiply(matrix3, features);
        features = features.stream().map(Math::tanh).collect(Collectors.toList());

        double m = Collections.max(features);
        for (int i = 0; i < features.size(); i++) {
            if (Math.abs(features.get(i) - m) < 0.001) return i;
        }
        return -1;
    }

    static List<Double> multiply(double[][] m, List<Double> v) {
        List<Double> res = new ArrayList<>();
        for (int i = 0; i < m.length; i++) {
            res.add(multiply(m[i], v));
        }
        return res;
    }

    static double multiply(double[] v1, List<Double> v2) {
        double res = 0.0;
        for (int i = 0; i < v1.length; i++) {
            res += v1[i] * v2.get(i);
        }
        return res;
    }
}
