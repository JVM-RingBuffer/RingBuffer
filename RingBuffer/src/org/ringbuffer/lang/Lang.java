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

import org.ringbuffer.InternalUnsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.ringbuffer.InternalUnsafe.UNSAFE;

public class Lang {
    public static final Module JAVA_BASE_MODULE = IllegalArgumentException.class.getModule();
    public static final Module ORG_RINGBUFFER_MODULE = InternalUnsafe.OopsCompressed.class.getModule();

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T uncheck(Throwable throwable) throws T {
        return (T) throwable;
    }

    public static Field getField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw uncheck(e);
        }
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw uncheck(e);
        }
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw uncheck(e);
        }
    }

    public static long objectFieldOffset(Class<?> clazz, String fieldName) {
        return UNSAFE.objectFieldOffset(getField(clazz, fieldName));
    }
}
