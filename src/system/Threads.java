package eu.menzani.ringbuffer.system;

import eu.menzani.ringbuffer.concurrent.Atomic;
import eu.menzani.ringbuffer.java.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Requires Linux or Windows. Tested on CentOS 7 and Windows 10.
 */
public class Threads {
    private static final Atomic<Path> libraryPath = new Atomic<>();

    public static @Nullable Path getLibraryPath() {
        return libraryPath.getVolatile();
    }

    public static void loadNativeLibrary() {
        loadNativeLibrary(Platform.current(), Platform.getTempFolder());
    }

    public static void loadNativeLibrary(Platform platform, Path libraryDirectory) {
        String libraryName = libraryNameFor(platform);
        Path libraryPath = libraryDirectory.resolve(libraryName);
        if (!Threads.libraryPath.compareAndSetVolatile(null, libraryPath)) {
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

    private static String libraryNameFor(Platform platform) {
        switch (platform) {
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
