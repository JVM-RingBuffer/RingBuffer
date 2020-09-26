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

import java.lang.reflect.Modifier;

public class Method<T> implements Invokable<T> {
    private final java.lang.reflect.Method method;
    private Object targetInstance;

    Method(java.lang.reflect.Method method) {
        this.method = method;
    }

    @Override
    public java.lang.reflect.Method getExecutable() {
        return method;
    }

    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    @Override
    public void ensureAccessible() {
        if (!method.canAccess(targetInstance)) {
            method.setAccessible(true);
        }
    }

    @Override
    public void forceAccessible() {
        Unsafe.setAccessible(method);
    }

    public void setTargetInstance(Object targetInstance) {
        this.targetInstance = targetInstance;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T call(Object... arguments) {
        try {
            return (T) method.invoke(targetInstance, arguments);
        } catch (ReflectiveOperationException e) {
            throw Lang.uncheck(e);
        }
    }
}
