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

package org.ringbuffer.system;

import org.ringbuffer.InternalUnsafe;
import org.ringbuffer.SunUnsafe;
import org.ringbuffer.concurrent.AtomicBoolean;
import org.ringbuffer.lang.Invokable;
import org.ringbuffer.lang.Lang;
import org.ringbuffer.lang.Method;

import java.lang.reflect.AccessibleObject;
import java.nio.ByteBuffer;
import java.security.ProtectionDomain;

import static org.ringbuffer.InternalUnsafe.UNSAFE;

public class Unsafe {
    public static final long ARRAY_BYTE_BASE_OFFSET;
    public static final long ARRAY_CHAR_BASE_OFFSET;
    public static final long ARRAY_SHORT_BASE_OFFSET;
    public static final long ARRAY_INT_BASE_OFFSET;
    public static final long ARRAY_LONG_BASE_OFFSET;
    public static final long ARRAY_FLOAT_BASE_OFFSET;
    public static final long ARRAY_DOUBLE_BASE_OFFSET;
    public static final long ARRAY_BOOLEAN_BASE_OFFSET;
    public static final long ARRAY_OBJECT_BASE_OFFSET;

    public static final long ARRAY_BYTE_INDEX_SCALE;
    public static final long ARRAY_CHAR_INDEX_SCALE;
    public static final long ARRAY_SHORT_INDEX_SCALE;
    public static final long ARRAY_INT_INDEX_SCALE;
    public static final long ARRAY_LONG_INDEX_SCALE;
    public static final long ARRAY_FLOAT_INDEX_SCALE;
    public static final long ARRAY_DOUBLE_INDEX_SCALE;
    public static final long ARRAY_BOOLEAN_INDEX_SCALE;
    public static final long ARRAY_OBJECT_INDEX_SCALE;

    private static final long OVERRIDE;

    static {
        InternalUnsafe.init();
        ARRAY_BYTE_BASE_OFFSET = jdk.internal.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
        ARRAY_CHAR_BASE_OFFSET = jdk.internal.misc.Unsafe.ARRAY_CHAR_BASE_OFFSET;
        ARRAY_SHORT_BASE_OFFSET = jdk.internal.misc.Unsafe.ARRAY_SHORT_BASE_OFFSET;
        ARRAY_INT_BASE_OFFSET = jdk.internal.misc.Unsafe.ARRAY_INT_BASE_OFFSET;
        ARRAY_LONG_BASE_OFFSET = jdk.internal.misc.Unsafe.ARRAY_LONG_BASE_OFFSET;
        ARRAY_FLOAT_BASE_OFFSET = jdk.internal.misc.Unsafe.ARRAY_FLOAT_BASE_OFFSET;
        ARRAY_DOUBLE_BASE_OFFSET = jdk.internal.misc.Unsafe.ARRAY_DOUBLE_BASE_OFFSET;
        ARRAY_BOOLEAN_BASE_OFFSET = jdk.internal.misc.Unsafe.ARRAY_BOOLEAN_BASE_OFFSET;
        ARRAY_OBJECT_BASE_OFFSET = jdk.internal.misc.Unsafe.ARRAY_OBJECT_BASE_OFFSET;

        ARRAY_BYTE_INDEX_SCALE = jdk.internal.misc.Unsafe.ARRAY_BYTE_INDEX_SCALE;
        ARRAY_CHAR_INDEX_SCALE = jdk.internal.misc.Unsafe.ARRAY_CHAR_INDEX_SCALE;
        ARRAY_SHORT_INDEX_SCALE = jdk.internal.misc.Unsafe.ARRAY_SHORT_INDEX_SCALE;
        ARRAY_INT_INDEX_SCALE = jdk.internal.misc.Unsafe.ARRAY_INT_INDEX_SCALE;
        ARRAY_LONG_INDEX_SCALE = jdk.internal.misc.Unsafe.ARRAY_LONG_INDEX_SCALE;
        ARRAY_FLOAT_INDEX_SCALE = jdk.internal.misc.Unsafe.ARRAY_FLOAT_INDEX_SCALE;
        ARRAY_DOUBLE_INDEX_SCALE = jdk.internal.misc.Unsafe.ARRAY_DOUBLE_INDEX_SCALE;
        ARRAY_BOOLEAN_INDEX_SCALE = jdk.internal.misc.Unsafe.ARRAY_BOOLEAN_INDEX_SCALE;
        ARRAY_OBJECT_INDEX_SCALE = jdk.internal.misc.Unsafe.ARRAY_OBJECT_INDEX_SCALE;

        if (Version.current() == Version.JAVA_11) {
            OVERRIDE = Lang.objectFieldOffset(AccessibleObject.class, "override");
        } else if (Platform.current().is32Bit()) {
            OVERRIDE = 8L;
        } else if (Platform.areOopsCompressed()) {
            OVERRIDE = 12L;
        } else {
            OVERRIDE = 16L;
        }
    }

    public static void setAccessible(AccessibleObject accessibleObject) {
        AtomicBoolean.setPlain(accessibleObject, OVERRIDE, true);
    }

    public static void addOpens(Module from, Module to, String packageName) {
        if (!from.isOpen(packageName, to)) {
            RequiringReflection.implAddOpens.setTargetInstance(from);
            RequiringReflection.implAddOpens.call(packageName, to);
        }
    }

    public static long getAddress(Object o, long offset) {
        return UNSAFE.getAddress(o, offset);
    }

    public static void putAddress(Object o, long offset, long x) {
        UNSAFE.putAddress(o, offset, x);
    }

    public static Object getUncompressedObject(long address) {
        return UNSAFE.getUncompressedObject(address);
    }

    public static long getAddress(long address) {
        return UNSAFE.getAddress(address);
    }

    public static void putAddress(long address, long x) {
        UNSAFE.putAddress(address, x);
    }

    public static long allocateMemory(long bytes) {
        return UNSAFE.allocateMemory(bytes);
    }

    public static long reallocateMemory(long address, long bytes) {
        return UNSAFE.reallocateMemory(address, bytes);
    }

    public static void setMemory(Object o, long offset, long bytes, byte value) {
        UNSAFE.setMemory(o, offset, bytes, value);
    }

    public static void setMemory(long address, long bytes, byte value) {
        UNSAFE.setMemory(address, bytes, value);
    }

    public static void copyMemory(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes) {
        UNSAFE.copyMemory(srcBase, srcOffset, destBase, destOffset, bytes);
    }

    public static void copyMemory(long srcAddress, long destAddress, long bytes) {
        UNSAFE.copyMemory(srcAddress, destAddress, bytes);
    }

    public static void copySwapMemory(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes, long elemSize) {
        UNSAFE.copySwapMemory(srcBase, srcOffset, destBase, destOffset, bytes, elemSize);
    }

    public static void copySwapMemory(long srcAddress, long destAddress, long bytes, long elemSize) {
        UNSAFE.copySwapMemory(srcAddress, destAddress, bytes, elemSize);
    }

    public static boolean shouldBeInitialized(Class<?> c) {
        return UNSAFE.shouldBeInitialized(c);
    }

    public static void ensureClassInitialized(Class<?> c) {
        UNSAFE.ensureClassInitialized(c);
    }

    public static int addressSize() {
        return UNSAFE.addressSize();
    }

    public static int pageSize() {
        return UNSAFE.pageSize();
    }

    public static Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) {
        return UNSAFE.defineClass(name, b, off, len, loader, protectionDomain);
    }

    public static Class<?> defineClass0(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) {
        return UNSAFE.defineClass0(name, b, off, len, loader, protectionDomain);
    }

    public static Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
        return UNSAFE.defineAnonymousClass(hostClass, data, cpPatches);
    }

    public static Object allocateInstance(Class<?> cls) throws InstantiationException {
        return UNSAFE.allocateInstance(cls);
    }

    public static Object allocateUninitializedArray(Class<?> componentType, int length) {
        return UNSAFE.allocateUninitializedArray(componentType, length);
    }

    public static void throwException(Throwable ee) {
        UNSAFE.throwException(ee);
    }

    public static int getLoadAverage(double[] loadavg, int nelems) {
        return UNSAFE.getLoadAverage(loadavg, nelems);
    }

    public static void loadFence() {
        UNSAFE.loadFence();
    }

    public static void storeFence() {
        UNSAFE.storeFence();
    }

    public static void fullFence() {
        UNSAFE.fullFence();
    }

    public static void loadLoadFence() {
        UNSAFE.loadLoadFence();
    }

    public static void storeStoreFence() {
        UNSAFE.storeStoreFence();
    }

    public static boolean isBigEndian() {
        return UNSAFE.isBigEndian();
    }

    public static boolean unalignedAccess() {
        return UNSAFE.unalignedAccess();
    }

    public static long getLongUnaligned(Object o, long offset) {
        return UNSAFE.getLongUnaligned(o, offset);
    }

    public static long getLongUnaligned(Object o, long offset, boolean bigEndian) {
        return UNSAFE.getLongUnaligned(o, offset, bigEndian);
    }

    public static int getIntUnaligned(Object o, long offset) {
        return UNSAFE.getIntUnaligned(o, offset);
    }

    public static int getIntUnaligned(Object o, long offset, boolean bigEndian) {
        return UNSAFE.getIntUnaligned(o, offset, bigEndian);
    }

    public static short getShortUnaligned(Object o, long offset) {
        return UNSAFE.getShortUnaligned(o, offset);
    }

    public static short getShortUnaligned(Object o, long offset, boolean bigEndian) {
        return UNSAFE.getShortUnaligned(o, offset, bigEndian);
    }

    public static char getCharUnaligned(Object o, long offset) {
        return UNSAFE.getCharUnaligned(o, offset);
    }

    public static char getCharUnaligned(Object o, long offset, boolean bigEndian) {
        return UNSAFE.getCharUnaligned(o, offset, bigEndian);
    }

    public static void putLongUnaligned(Object o, long offset, long x) {
        UNSAFE.putLongUnaligned(o, offset, x);
    }

    public static void putLongUnaligned(Object o, long offset, long x, boolean bigEndian) {
        UNSAFE.putLongUnaligned(o, offset, x, bigEndian);
    }

    public static void putIntUnaligned(Object o, long offset, int x) {
        UNSAFE.putIntUnaligned(o, offset, x);
    }

    public static void putIntUnaligned(Object o, long offset, int x, boolean bigEndian) {
        UNSAFE.putIntUnaligned(o, offset, x, bigEndian);
    }

    public static void putShortUnaligned(Object o, long offset, short x) {
        UNSAFE.putShortUnaligned(o, offset, x);
    }

    public static void putShortUnaligned(Object o, long offset, short x, boolean bigEndian) {
        UNSAFE.putShortUnaligned(o, offset, x, bigEndian);
    }

    public static void putCharUnaligned(Object o, long offset, char x) {
        UNSAFE.putCharUnaligned(o, offset, x);
    }

    public static void putCharUnaligned(Object o, long offset, char x, boolean bigEndian) {
        UNSAFE.putCharUnaligned(o, offset, x, bigEndian);
    }

    public static void invokeCleaner(ByteBuffer directBuffer) {
        SunUnsafe.UNSAFE.invokeCleaner(directBuffer);
    }

    private static class RequiringReflection {
        private static final Method<?> implAddOpens;

        static {
            final Class<?> clazz = Module.class;
            implAddOpens = Invokable.ofMethod(clazz, "implAddOpens", String.class, clazz);
            implAddOpens.forceAccessible();
        }
    }
}
