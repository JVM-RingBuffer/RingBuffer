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

import java.nio.file.Path;

public enum Platform {
    LINUX_32(false, true),
    LINUX_64(false, false),
    WINDOWS_32(true, true),
    WINDOWS_64(true, false);

    private final boolean isWindows;
    private final boolean is32Bit;

    Platform(boolean isWindows, boolean is32Bit) {
        this.isWindows = isWindows;
        this.is32Bit = is32Bit;
    }

    public boolean isWindows() {
        return isWindows;
    }

    public boolean isLinux() {
        return !isWindows;
    }

    public boolean is32Bit() {
        return is32Bit;
    }

    public boolean is64Bit() {
        return !is32Bit;
    }

    public static Platform current() {
        return Current.value;
    }

    public static Path getTempFolder() {
        return TempFolder.value;
    }

    public static boolean areOopsCompressed() {
        return OopsCompressed.value;
    }

    private static class Current {
        static final Platform value;

        static {
            boolean is32Bit = System.getProperty("sun.arch.data.model").equals("32");
            if (System.getProperty("os.name").contains("Windows")) {
                if (is32Bit) {
                    value = WINDOWS_32;
                } else {
                    value = WINDOWS_64;
                }
            } else if (is32Bit) {
                value = LINUX_32;
            } else {
                value = LINUX_64;
            }
        }
    }

    private static class TempFolder {
        static final Path value = Path.of(System.getProperty("java.io.tmpdir"));
    }

    static class OopsCompressed {
        static final boolean value;

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
