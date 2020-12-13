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

package org.ringbuffer.util;

import org.ringbuffer.concurrent.Atomic;
import org.ringbuffer.lang.Lang;

public class ConcurrentObjectToggle<T> implements ObjectToggle<T> {
    private static final long VALUE = Lang.objectFieldOffset(ConcurrentObjectToggle.class, "value");

    private static final Object placeholder = new Object();

    private Object value;

    @Override
    public void ensureNotSet(String exceptionMessage) {
        if (!Atomic.compareAndSetVolatile(this, VALUE, null, placeholder)) {
            throw new IllegalStateException(exceptionMessage);
        }
    }

    @Override
    public void set(T value) {
        Atomic.setRelease(this, VALUE, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T ensureSet(String exceptionMessage) {
        Object value = Atomic.getAndSetVolatile(this, VALUE, null);
        if (value == null) {
            throw new IllegalStateException(exceptionMessage);
        }
        if (value == placeholder) {
            do {
                value = Atomic.getAndSetVolatile(this, VALUE, null);
            } while (value == null);
        }
        return (T) value;
    }
}
