package eu.menzani.ringbuffer.system;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Requires Linux or Windows. Tested on CentOS 7 and Windows 10.
 */
public class ThreadBind {
    private final Path libraryPath;
    private final String libraryName;

    public ThreadBind(Path libraryDirectory, Platform platform) {
        libraryName = libraryNameFor(platform);
        libraryPath = libraryDirectory.resolve(libraryName);
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

    public Path getLibraryPath() {
        return libraryPath;
    }

    public void loadNativeLibrary() throws ThreadBindException {
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

    public void bindCurrentThreadToCPU(int cpu) throws ThreadBindException {
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
}
