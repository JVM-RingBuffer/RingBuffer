package eu.menzani.ringbuffer.system;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Requires Linux or Windows. Tested on CentOS 7 and Windows 10.
 */
public class ThreadBind {
    private static final AtomicReference<Path> libraryPath = new AtomicReference<>();

    public static Optional<Path> getLibraryPath() {
        return Optional.ofNullable(libraryPath.get());
    }

    public static void loadNativeLibrary() {
        loadNativeLibrary(Platform.current(), Platform.getTempFolder());
    }

    public static void loadNativeLibrary(Platform platform, Path libraryDirectory) {
        String libraryName = libraryNameFor(platform);
        Path libraryPath = libraryDirectory.resolve(libraryName);
        if (!ThreadBind.libraryPath.compareAndSet(null, libraryPath)) {
            throw new IllegalStateException("A native library has already been loaded.");
        }
        if (Files.notExists(libraryPath)) {
            try (InputStream stream = ThreadBind.class.getResourceAsStream(libraryName)) {
                Files.copy(stream, libraryPath);
            } catch (IOException e) {
                throw new ThreadBindException(e);
            }
        }
        try {
            System.load(libraryPath.toAbsolutePath().toString());
        } catch (UnsatisfiedLinkError e) {
            throw new ThreadBindException(e);
        }
    }

    private static String libraryNameFor(Platform platform) {
        switch (platform) {
            case LINUX_32:
                return "libthreadbind_32.so";
            case LINUX_64:
                return "libthreadbind_64.so";
            case WINDOWS_32:
                return "ThreadBind_32.dll";
            case WINDOWS_64:
                return "ThreadBind_64.dll";
        }
        throw new AssertionError();
    }

    public static void bindCurrentThreadToCPU(int cpu) {
        try {
            int errorCode = bindCurrentThread(cpu);
            if (errorCode != 0) {
                throw new ThreadBindException(errorCode);
            }
        } catch (UnsatisfiedLinkError e) {
            throw new ThreadBindException(e);
        }
    }

    private static native int bindCurrentThread(int cpu);

    public static ThreadSpreader.Builder spreadOverCPUs() {
        return new ThreadSpreader.Builder();
    }
}
