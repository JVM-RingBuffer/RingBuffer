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

import org.ringbuffer.lang.Optional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Requires Linux or Windows. Tested on CentOS 7 and Windows 10.
 */
public class Threads {
    private static final AtomicReference<Path> libraryPath = new AtomicReference<>();

    public static @Optional Path getLibraryPath() {
        return libraryPath.get();
    }

    public static void loadNativeLibrary() {
        loadNativeLibrary(Platform.getTempFolder());
    }

    public static void loadNativeLibrary(Path libraryDirectory) {
        String libraryName = libraryName();
        Path libraryPath = libraryDirectory.resolve(libraryName);
        if (!Threads.libraryPath.compareAndSet(null, libraryPath)) {
            throw new IllegalStateException("A native library has already been loaded.");
        }
        if (Files.notExists(libraryPath)) {
            try (InputStream stream = Threads.class.getResourceAsStream(libraryName)) {
                Files.copy(stream, libraryPath);
            } catch (IOException e) {
                throw new ThreadManipulationException(e);
            }
        }
        try {
            System.load(libraryPath.toAbsolutePath().toString());
        } catch (UnsatisfiedLinkError e) {
            throw new ThreadManipulationException(e);
        }
    }

    private static String libraryName() {
        switch (Platform.current()) {
            case LINUX_32:
                return "libthreadmanipulation_32.so";
            case LINUX_64:
                return "libthreadmanipulation_64.so";
            case WINDOWS_32:
                return "ThreadManipulation_32.dll";
            case WINDOWS_64:
                return "ThreadManipulation_64.dll";
        }
        throw new AssertionError();
    }

    public static void bindCurrentThreadToCPU(int cpu) {
        try {
            int errorCode = bindCurrentThread(cpu);
            if (errorCode != 0) {
                throw new ThreadManipulationException(errorCode);
            }
        } catch (UnsatisfiedLinkError e) {
            throw new ThreadManipulationException(e);
        }
    }

    private static native int bindCurrentThread(int cpu);

    /**
     * On Linux, if not running under root, you need to add this to {@code /etc/security/limits.conf}:
     *
     * <pre>{@code
     * <user> hard rtprio 99
     * <user> soft rtprio 99
     * }</pre>
     */
    public static void setCurrentThreadPriorityToRealtime() {
        try {
            int errorCode = setCurrentThreadPriority();
            if (errorCode != 0) {
                throw new ThreadManipulationException(errorCode);
            }
        } catch (UnsatisfiedLinkError e) {
            throw new ThreadManipulationException(e);
        }
    }

    private static native int setCurrentThreadPriority();

    public static ThreadSpreader.Builder spreadOverCPUs() {
        return new ThreadSpreader.Builder();
    }
}
