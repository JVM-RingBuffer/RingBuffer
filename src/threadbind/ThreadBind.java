package eu.menzani.ringbuffer.threadbind;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ThreadBind {
    private static final String libraryName = "libthreadbind.so";

    private final Path libraryPath;

    public ThreadBind(Path libraryDirectory) {
        libraryPath = libraryDirectory.resolve(libraryName);
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
