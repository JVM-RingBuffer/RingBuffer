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

import java.io.PrintStream;

public class StreamBuffer {
    private final PrintStream stream;
    public final StringBuilder builder;
    private char[] buffer;

    public static StreamBuffer standardOutput(int initialCapacity) {
        return new StreamBuffer(System.out, initialCapacity);
    }

    public static StreamBuffer standardError(int initialCapacity) {
        return new StreamBuffer(System.err, initialCapacity);
    }

    public StreamBuffer(PrintStream stream, int initialCapacity) {
        this.stream = stream;
        builder = new StringBuilder(initialCapacity);
        buffer = new char[initialCapacity];
    }

    public void reset() {
        builder.setLength(0);
    }

    public void println() {
        builder.append(System.lineSeparator());
        print();
    }

    public void print() {
        int capacity = builder.capacity();
        if (buffer.length != capacity) {
            buffer = new char[capacity];
        }
        builder.getChars(0, builder.length(), buffer, 0);

        stream.print(buffer);
    }
}
