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

package org.ringbuffer;

import org.ringbuffer.lang.Lang;
import org.ringbuffer.lang.UncaughtException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class SunUnsafe {
    public static final Unsafe UNSAFE;

    static {
        Field field = Lang.getField(Unsafe.class, "theUnsafe");
        field.setAccessible(true);
        try {
            UNSAFE = (Unsafe) field.get(null);
        } catch (IllegalAccessException e) {
            throw new UncaughtException(e);
        }
    }

    static long objectFieldOffset(Class<?> clazz, String fieldName) {
        return UNSAFE.objectFieldOffset(Lang.getField(clazz, fieldName));
    }
}
