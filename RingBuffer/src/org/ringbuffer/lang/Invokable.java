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

import java.lang.reflect.Executable;

public interface Invokable<T> {
    Executable getExecutable();

    void ensureAccessible();

    void forceAccessible();

    T call(Object... arguments);

    @SuppressWarnings("unchecked")
    static <T> Invokable<T> of(Executable executable) {
        if (executable instanceof java.lang.reflect.Constructor<?>) {
            return of((java.lang.reflect.Constructor<T>) executable);
        }
        return of((java.lang.reflect.Method) executable);
    }

    static <T> Method<T> of(java.lang.reflect.Method method) {
        return new Method<>(method);
    }

    static <T> Constructor<T> of(java.lang.reflect.Constructor<T> constructor) {
        return new Constructor<>(constructor);
    }

    static <T> Method<T> ofMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        return of(Lang.getMethod(clazz, name, parameterTypes));
    }

    static <T> Constructor<T> ofConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        return of(Lang.getConstructor(clazz, parameterTypes));
    }
}
