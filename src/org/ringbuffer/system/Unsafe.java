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

public class Unsafe {
    public static final sun.misc.Unsafe UNSAFE;

    static {
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static final long OVERRIDE;

    static {
        try {
            OVERRIDE = UNSAFE.objectFieldOffset(AccessibleObject.class.getDeclaredField("override"));
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void setAccessible(AccessibleObject accessibleObject) {
        UNSAFE.putBoolean(accessibleObject, OVERRIDE, true);
    }

    public static void addOpensConditionally(Module from, Module to, String packageName) {
        if (!from.isOpen(packageName, to)) {
            addOpens(from, to, packageName);
        }
    }

    public static void addOpens(Module from, Module to, String packageName) {
        final Class<?> clazz = Module.class;
        try {
            Method method = clazz.getDeclaredMethod("implAddOpens", String.class, clazz);
            setAccessible(method);
            method.invoke(from, packageName, to);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError();
        }
    }
}
