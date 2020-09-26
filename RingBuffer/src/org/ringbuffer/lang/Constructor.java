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

import org.ringbuffer.system.Unsafe;

public class Constructor<T> implements Invokable<T> {
    private final java.lang.reflect.Constructor<T> constructor;

    Constructor(java.lang.reflect.Constructor<T> constructor) {
        this.constructor = constructor;
    }

    @Override
    public java.lang.reflect.Constructor<T> getExecutable() {
        return constructor;
    }

    @Override
    public void ensureAccessible() {
        if (!constructor.canAccess(null)) {
            constructor.setAccessible(true);
        }
    }

    @Override
    public void forceAccessible() {
        Unsafe.setAccessible(constructor);
    }

    @Override
    public T call(Object... arguments) {
        try {
            return constructor.newInstance(arguments);
        } catch (ReflectiveOperationException e) {
            throw Lang.uncheck(e);
        }
    }
}
