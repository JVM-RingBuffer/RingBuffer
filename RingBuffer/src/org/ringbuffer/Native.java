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

import org.ringbuffer.lang.UncaughtException;
import org.ringbuffer.system.ApplicationProperty;
import org.ringbuffer.system.Platform;
import org.ringbuffer.system.PlatformNotSupportedException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Native {
    public static void init() {
    }

    static {
        String libraryName = libraryName();
        Path libraryPath = libraryDirectory().resolve(libraryName);

        try (InputStream stream = Native.class.getResourceAsStream(libraryName)) {
            Files.copy(stream, libraryPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncaughtException(e);
        }
        System.load(libraryPath.toAbsolutePath().toString());
    }

    private static String libraryName() {
        switch (Platform.current()) {
            case LINUX_32:
                return "libringbuffer_32.so";
            case LINUX_64:
                return "libringbuffer_64.so";
            case WINDOWS_32:
                return "RingBuffer_32.dll";
            case WINDOWS_64:
                return "RingBuffer_64.dll";
        }
        throw new PlatformNotSupportedException();
    }

    private static Path libraryDirectory() {
        String property = new ApplicationProperty(Native.class, "nativeLibrary", "directory").get();
        if (property == null) {
            return Platform.getTempFolder();
        }
        return Path.of(property);
    }
}
