package eu.menzani.ringbuffer.system;

import eu.menzani.ringbuffer.java.Assume;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Requires Linux or Windows. Tested on CentOS 7 and Windows 10.
 */
public class ThreadBind {
    private static volatile Path libraryPath;

    public static Optional<Path> getLibraryPath() {
        return Optional.ofNullable(libraryPath);
    }

    public static void loadNativeLibrary() {
        loadNativeLibrary(Platform.current(), Platform.getTempFolder());
    }

    public static synchronized void loadNativeLibrary(Platform platform, Path libraryDirectory) {
        if (libraryPath != null) {
            throw new IllegalStateException("A native library has already been loaded.");
        }
        String libraryName = libraryNameFor(platform);
        libraryPath = libraryDirectory.resolve(libraryName);
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

    public static Spread spread() {
        int lastCPU = Runtime.getRuntime().availableProcessors() - 1;
        boolean manyCPUs = lastCPU >= 6 - 1;
        return spread(manyCPUs ? 2 : 0, lastCPU, manyCPUs ? 2 : 1, true);
    }

    public static Spread spread(int firstCPU, int lastCPU, int increment, boolean cycle) {
        return new Spread(firstCPU, lastCPU, increment, cycle);
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

    public static class Spread {
        private final int firstCPU;
        private final int lastCPU;
        private final int increment;
        private final boolean cycle;
        private final AtomicInteger cpu;

        private Spread(int firstCPU, int lastCPU, int increment, boolean cycle) {
            Assume.notNegative(firstCPU, "firstCPU");
            Assume.notNegative(lastCPU, "lastCPU");
            Assume.notGreater(firstCPU, lastCPU, "firstCPU", "lastCPU");
            Assume.notLesser(increment, 1, "increment");
            this.firstCPU = firstCPU;
            this.lastCPU = lastCPU;
            this.increment = increment;
            this.cycle = cycle;
            cpu = new AtomicInteger(firstCPU);
        }

        public void bindCurrentThreadToNextCPU() {
            ThreadBind.bindCurrentThreadToCPU(cpu.getAndUpdate(cpu -> {
                if (cpu >= lastCPU) {
                    if (cycle) {
                        return firstCPU;
                    }
                    throw new ThreadBindException("No more CPUs are available to bind to.");
                }
                return cpu + increment;
            }));
        }
    }
}
