/*
 * Copyright 2020 Francesco Menzani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ringbuffer.lang;

public class Assert {
    public static void equal(int left, int right) {
        assert left == right : left;
    }

    public static void equalTo(int left, int right) {
        assert left == right : left + " " + right;
    }

    public static void equal(long left, long right) {
        assert left == right : left;
    }

    public static void equalTo(long left, long right) {
        assert left == right : left + " " + right;
    }

    public static void equal(Object left, Object right) {
        assert left.equals(right) : left;
    }

    public static void equalTo(Object left, Object right) {
        assert left.equals(right) : left + " " + right;
    }

    public static void notEqual(int left, int right) {
        assert left != right;
    }

    public static void notEqualTo(int left, int right) {
        assert left != right : left;
    }

    public static void notEqual(long left, long right) {
        assert left != right;
    }

    public static void notEqualTo(long left, long right) {
        assert left != right : left;
    }

    public static void notEqual(Object left, Object right) {
        assert !left.equals(right);
    }

    public static void notEqualTo(Object left, Object right) {
        assert !left.equals(right) : left;
    }

    public static void lesser(int value, int cap) {
        assert value < cap : value;
    }

    public static void lesserThan(int value, int cap) {
        assert value < cap : value + " " + cap;
    }

    public static void lesser(long value, long cap) {
        assert value < cap : value;
    }

    public static void lesserThan(long value, long cap) {
        assert value < cap : value + " " + cap;
    }

    public static void notLesser(int value, int cap) {
        assert value >= cap : value;
    }

    public static void notLesserThan(int value, int cap) {
        assert value >= cap : value + " " + cap;
    }

    public static void notLesser(long value, long cap) {
        assert value >= cap : value;
    }

    public static void notLesserThan(long value, long cap) {
        assert value >= cap : value + " " + cap;
    }

    public static void greater(int value, int cap) {
        assert value > cap : value;
    }

    public static void greaterThan(int value, int cap) {
        assert value > cap : value + " " + cap;
    }

    public static void greater(long value, long cap) {
        assert value > cap : value;
    }

    public static void greaterThan(long value, long cap) {
        assert value > cap : value + " " + cap;
    }

    public static void notGreater(int value, int cap) {
        assert value <= cap : value;
    }

    public static void notGreaterThan(int value, int cap) {
        assert value <= cap : value + " " + cap;
    }

    public static void notGreater(long value, long cap) {
        assert value <= cap : value;
    }

    public static void notGreaterThan(long value, long cap) {
        assert value <= cap : value + " " + cap;
    }

    public static void positive(int value) {
        assert value > 0 : value;
    }

    public static void positive(long value) {
        assert value > 0L : value;
    }

    public static void notPositive(int value) {
        assert value <= 0 : value;
    }

    public static void notPositive(long value) {
        assert value <= 0L : value;
    }

    public static void negative(int value) {
        assert value < 0 : value;
    }

    public static void negative(long value) {
        assert value < 0L : value;
    }

    public static void notNegative(int value) {
        assert value >= 0 : value;
    }

    public static void notNegative(long value) {
        assert value >= 0L : value;
    }
}
