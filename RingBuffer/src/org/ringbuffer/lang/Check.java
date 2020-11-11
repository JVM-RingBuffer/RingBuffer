/*
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

public class Check {
    public static void equal(int left, int right) {
        if (left != right) {
            throw new AssertionError(left);
        }
    }

    public static void equalTo(int left, int right) {
        if (left != right) {
            throw new IntAssertionError(left, right);
        }
    }

    public static void equal(long left, long right) {
        if (left != right) {
            throw new AssertionError(left);
        }
    }

    public static void equalTo(long left, long right) {
        if (left != right) {
            throw new LongAssertionError(left, right);
        }
    }

    public static void equal(Object left, Object right) {
        if (!left.equals(right)) {
            throw new AssertionError(left);
        }
    }

    public static void equalTo(Object left, Object right) {
        if (!left.equals(right)) {
            throw new ObjectAssertionError(left, right);
        }
    }

    public static void notEqual(int left, int right) {
        if (left == right) {
            throw new AssertionError();
        }
    }

    public static void notEqualTo(int left, int right) {
        if (left == right) {
            throw new AssertionError(left);
        }
    }

    public static void notEqual(long left, long right) {
        if (left == right) {
            throw new AssertionError();
        }
    }

    public static void notEqualTo(long left, long right) {
        if (left == right) {
            throw new AssertionError(left);
        }
    }

    public static void notEqual(Object left, Object right) {
        if (left.equals(right)) {
            throw new AssertionError();
        }
    }

    public static void notEqualTo(Object left, Object right) {
        if (left.equals(right)) {
            throw new AssertionError(left);
        }
    }

    public static void lesser(int value, int cap) {
        if (value >= cap) {
            throw new AssertionError(value);
        }
    }

    public static void lesserThan(int value, int cap) {
        if (value >= cap) {
            throw new IntAssertionError(value, cap);
        }
    }

    public static void lesser(long value, long cap) {
        if (value >= cap) {
            throw new AssertionError(value);
        }
    }

    public static void lesserThan(long value, long cap) {
        if (value >= cap) {
            throw new LongAssertionError(value, cap);
        }
    }

    public static void notLesser(int value, int cap) {
        if (value < cap) {
            throw new AssertionError(value);
        }
    }

    public static void notLesserThan(int value, int cap) {
        if (value < cap) {
            throw new IntAssertionError(value, cap);
        }
    }

    public static void notLesser(long value, long cap) {
        if (value < cap) {
            throw new AssertionError(value);
        }
    }

    public static void notLesserThan(long value, long cap) {
        if (value < cap) {
            throw new LongAssertionError(value, cap);
        }
    }

    public static void greater(int value, int cap) {
        if (value <= cap) {
            throw new AssertionError(value);
        }
    }

    public static void greaterThan(int value, int cap) {
        if (value <= cap) {
            throw new IntAssertionError(value, cap);
        }
    }

    public static void greater(long value, long cap) {
        if (value <= cap) {
            throw new AssertionError(value);
        }
    }

    public static void greaterThan(long value, long cap) {
        if (value <= cap) {
            throw new LongAssertionError(value, cap);
        }
    }

    public static void notGreater(int value, int cap) {
        if (value > cap) {
            throw new AssertionError(value);
        }
    }

    public static void notGreaterThan(int value, int cap) {
        if (value > cap) {
            throw new IntAssertionError(value, cap);
        }
    }

    public static void notGreater(long value, long cap) {
        if (value > cap) {
            throw new AssertionError(value);
        }
    }

    public static void notGreaterThan(long value, long cap) {
        if (value > cap) {
            throw new LongAssertionError(value, cap);
        }
    }

    public static void positive(int value) {
        if (value <= 0) {
            throw new AssertionError(value);
        }
    }

    public static void positive(long value) {
        if (value <= 0L) {
            throw new AssertionError(value);
        }
    }

    public static void notPositive(int value) {
        if (value > 0) {
            throw new AssertionError(value);
        }
    }

    public static void notPositive(long value) {
        if (value > 0L) {
            throw new AssertionError(value);
        }
    }

    public static void negative(int value) {
        if (value >= 0) {
            throw new AssertionError(value);
        }
    }

    public static void negative(long value) {
        if (value >= 0L) {
            throw new AssertionError(value);
        }
    }

    public static void notNegative(int value) {
        if (value < 0) {
            throw new AssertionError(value);
        }
    }

    public static void notNegative(long value) {
        if (value < 0L) {
            throw new AssertionError(value);
        }
    }

    private static final String exceptionClassName = AssertionError.class.getName() + ": ";

    private static class IntAssertionError extends AssertionError {
        private IntAssertionError(int one, int two) {
            super(exceptionClassName + one + " " + two);
        }

        @Override
        public String toString() {
            return getMessage();
        }
    }

    private static class LongAssertionError extends AssertionError {
        private LongAssertionError(long one, long two) {
            super(exceptionClassName + one + " " + two);
        }

        @Override
        public String toString() {
            return getMessage();
        }
    }

    private static class ObjectAssertionError extends AssertionError {
        private ObjectAssertionError(Object one, Object two) {
            super(exceptionClassName + one + " " + two);
        }

        @Override
        public String toString() {
            return getMessage();
        }
    }
}
