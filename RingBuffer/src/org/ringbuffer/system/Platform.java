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

import org.ringbuffer.InternalUnsafe;

import java.nio.file.Path;

public enum Platform {
    LINUX_32(false, true, false, true),
    LINUX_64(false, true, false, false),
    WINDOWS_32(true, false, false, true),
    WINDOWS_64(true, false, false, false),
    MAC_32(false, false, true, true),
    MAC_64(false, false, true, false);

    private final boolean isWindows, isLinux, isMac;
    private final boolean is32Bit;

    Platform(boolean isWindows, boolean isLinux, boolean isMac, boolean is32Bit) {
        this.isWindows = isWindows;
        this.isLinux = isLinux;
        this.isMac = isMac;
        this.is32Bit = is32Bit;
    }

    public boolean isWindows() {
        return isWindows;
    }

    public boolean isLinux() {
        return isLinux;
    }

    public boolean isMac() {
        return isMac;
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
        return InternalUnsafe.OopsCompressed.value;
    }

    private static class Current {
        static final Platform value;

        static {
            String osName = System.getProperty("os.name");
            boolean is32Bit = System.getProperty("sun.arch.data.model").equals("32");
            if (osName.contains("Windows")) {
                if (is32Bit) {
                    value = WINDOWS_32;
                } else {
                    value = WINDOWS_64;
                }
            } else if (osName.contains("Linux")) {
                if (is32Bit) {
                    value = LINUX_32;
                } else {
                    value = LINUX_64;
                }
            } else if (osName.contains("Mac")) {
                if (is32Bit) {
                    value = MAC_32;
                } else {
                    value = MAC_64;
                }
            } else {
                throw new AssertionError();
            }
        }
    }

    private static class TempFolder {
        static final Path value = Path.of(System.getProperty("java.io.tmpdir"));
    }
}
