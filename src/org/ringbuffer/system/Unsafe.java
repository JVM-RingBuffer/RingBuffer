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

package org.ringbuffer.system;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * To avoid using reflection, consider adding the VM option: {@code --add-opens java.base/jdk.internal.misc=org.ringbuffer}
 */
public class Unsafe {
    public static final Module JAVA_BASE_MODULE = Class.class.getModule();

    public static final jdk.internal.misc.Unsafe UNSAFE;

    private static final sun.misc.Unsafe unsafe;
    private static final long OVERRIDE = objectFieldOffset(AccessibleObject.class, "override");
    private static final Method implAddOpens;

    static {
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (sun.misc.Unsafe) field.get(null);

            final Class<?> clazz = Module.class;
            implAddOpens = clazz.getDeclaredMethod("implAddOpens", String.class, clazz);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
        setAccessible(implAddOpens);

        addOpens(JAVA_BASE_MODULE, Unsafe.class.getModule(), "jdk.internal.misc");
        UNSAFE = jdk.internal.misc.Unsafe.getUnsafe();
    }

    public static long objectFieldOffset(Class<?> clazz, String fieldName) {
        try {
            return unsafe.objectFieldOffset(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void setAccessible(AccessibleObject accessibleObject) {
        unsafe.putBoolean(accessibleObject, OVERRIDE, true);
    }

    public static void addOpens(Module from, Module to, String packageName) {
        if (!from.isOpen(packageName, to)) {
            try {
                implAddOpens.invoke(from, packageName, to);
            } catch (ReflectiveOperationException e) {
                throw new AssertionError();
            }
        }
    }
}
