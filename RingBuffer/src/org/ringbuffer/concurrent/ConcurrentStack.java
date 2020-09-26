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

package org.ringbuffer.concurrent;

import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.lang.Optional;
import org.ringbuffer.system.Unsafe;

public class ConcurrentStack<T extends ConcurrentStackElement<T>> {
    private static final long HEAD = Unsafe.objectFieldOffset(ConcurrentStack.class, "head");

    @Contended
    private T head;

    public void push(T element) {
        T head;
        do {
            head = Atomic.getOpaque(this, HEAD);
            element.setNext(head);
        } while (!Atomic.compareAndSetVolatile(this, HEAD, head, element));
    }

    public @Optional T pop() {
        T head, newHead;
        do {
            head = Atomic.getAcquire(this, HEAD);
            if (head == null) {
                return null;
            }
            newHead = head.getNext();
        } while (!Atomic.compareAndSetVolatile(this, HEAD, head, newHead));
        return head;
    }

    public @Optional T peek() {
        return Atomic.getAcquire(this, HEAD);
    }
}
