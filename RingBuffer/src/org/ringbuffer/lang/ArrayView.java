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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class ArrayView<T> extends AbstractList<T> implements RandomAccess, Serializable {
    private static final long serialVersionUID = 0L;

    private final T[] array;
    private final int fromIndex;

    public ArrayView(T[] array) {
        this(array, 0);
    }

    public ArrayView(T[] array, int fromIndex) {
        Assume.notNegative(fromIndex);
        Assume.lesserThan(fromIndex, array.length);
        this.array = array;
        this.fromIndex = fromIndex;
    }

    @Override
    public int size() {
        return array.length - fromIndex;
    }

    @Override
    public T get(int index) {
        Assume.notNegative(index);
        return array[fromIndex + index];
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new Iterator();
    }

    private class Iterator implements java.util.Iterator<T> {
        private int cursor = fromIndex;

        Iterator() {
        }

        @Override
        public boolean hasNext() {
            return cursor < array.length;
        }

        @Override
        public T next() {
            if (cursor == array.length) {
                throw new NoSuchElementException();
            }
            return array[cursor++];
        }
    }
}
