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

package org.ringbuffer;

import org.ringbuffer.system.Platform;
import org.ringbuffer.system.Unsafe;
import org.ringbuffer.system.Version;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class UnsafeAccess {
    public static final jdk.internal.misc.Unsafe UNSAFE;

    static {
        final Module from = Unsafe.JAVA_BASE_MODULE;
        final Module to = Unsafe.class.getModule();
        final String packageName = "jdk.internal.misc";
        try {
            if (!from.isOpen(packageName, to)) {
                // Code is duplicated so that we do not have to use reflection in the other case.
                final Class<?> clazz = Module.class;
                Method implAddOpens = clazz.getDeclaredMethod("implAddOpens", String.class, clazz);

                long OVERRIDE;
                if (Version.current() == Version.JAVA_11) {
                    OVERRIDE = SunUnsafeAccess.UNSAFE.objectFieldOffset(AccessibleObject.class.getDeclaredField("override"));
                } else if (Platform.current().is32Bit()) {
                    OVERRIDE = 8L;
                } else {
                    long offset = SunUnsafeAccess.UNSAFE.objectFieldOffset(OopsCompressed.class.getDeclaredField("i"));
                    if (offset == 8L) {
                        assert Platform.current().is32Bit();
                        OVERRIDE = -1L;
                    } else if (offset == 12L) {
                        OVERRIDE = 12L;
                    } else if (offset == 16L) {
                        OVERRIDE = 16L;
                    } else {
                        throw new AssertionError();
                    }
                }
                SunUnsafeAccess.UNSAFE.putBoolean(implAddOpens, OVERRIDE, true);

                implAddOpens.invoke(from, packageName, to);
            }
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
        UNSAFE = jdk.internal.misc.Unsafe.getUnsafe();
    }

    public static class OopsCompressed {
        public static final boolean value;

        int i;

        // Duplicated in Unsafe.<clinit>
        static {
            long offset = Unsafe.objectFieldOffset(OopsCompressed.class, "i");
            if (offset == 8L) {
                assert Platform.current().is32Bit();
                value = false;
            } else if (offset == 12L) {
                value = true;
            } else if (offset == 16L) {
                value = false;
            } else {
                throw new AssertionError();
            }
        }
    }
}
