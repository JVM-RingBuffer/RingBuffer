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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PlatformTest {
    @Test
    void isWindows() {
        assertTrue(Platform.WINDOWS_32.isWindows());
        assertTrue(Platform.WINDOWS_64.isWindows());
    }

    @Test
    void isLinux() {
        assertTrue(Platform.LINUX_32.isLinux());
        assertTrue(Platform.LINUX_64.isLinux());
    }

    @Test
    void is32Bit() {
        assertTrue(Platform.WINDOWS_32.is32Bit());
        assertTrue(Platform.LINUX_32.is32Bit());
    }

    @Test
    void is64Bit() {
        assertTrue(Platform.WINDOWS_64.is64Bit());
        assertTrue(Platform.LINUX_64.is64Bit());
    }
}
