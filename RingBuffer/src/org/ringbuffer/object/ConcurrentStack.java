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

package org.ringbuffer.object;

import jdk.internal.vm.annotation.Contended;

public class ConcurrentStack<T> extends Stack<T> {
    @Contended
    private final T[] elements;
    @Contended
    private int index;

    @SuppressWarnings("unchecked")
    public ConcurrentStack(int capacity) {
        elements = (T[]) new Object[capacity];
    }

    @Override
    public int getCapacity() {
        return elements.length;
    }

    public boolean canPush() {
        return index != elements.length;
    }

    public boolean canPop() {
        return index != 0;
    }

    public void push(T element) {
        elements[index++] = element;
    }

    public T pop() {
        return elements[--index];
    }

    public T peek() {
        return elements[index];
    }

    public synchronized boolean isFull() {
        return index == elements.length;
    }

    @Override
    public synchronized boolean isEmpty() {
        return index == 0;
    }

    @Override
    public synchronized void put(T element) {
        elements[index++] = element;
    }

    @Override
    public synchronized T take() {
        return elements[--index];
    }

    public synchronized T get() {
        return elements[index];
    }
}
