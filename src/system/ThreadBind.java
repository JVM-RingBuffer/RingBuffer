package eu.menzani.ringbuffer.system;

import eu.menzani.ringbuffer.java.Assume;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
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

    public static Spread.Builder spread(int firstCPU, int increment) {
        return new Spread.Builder(firstCPU, increment);
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
        private final AtomicInteger nextCPU;

        private Spread(Builder builder) {
            firstCPU = builder.firstCPU;
            lastCPU = builder.lastCPU;
            increment = builder.increment;
            cycle = builder.cycle;
            nextCPU = new AtomicInteger(firstCPU);
        }

        public void bindCurrentThreadToNextCPU() {
            ThreadBind.bindCurrentThreadToCPU(nextCPU.getAndUpdate(cpu -> {
                int next = cpu + increment;
                if (next <= lastCPU) {
                    return next;
                }
                if (cycle) {
                    return firstCPU;
                }
                throw new ThreadBindException("No more CPUs are available to bind to.");
            }));
        }

        public static class Builder {
            private final int firstCPU;
            private final int increment;
            private int lastCPU;
            private boolean cycle;

            private Builder(int firstCPU, int increment) {
                Assume.notNegative(firstCPU);
                Assume.notLesser(increment, 1);
                this.firstCPU = firstCPU;
                this.increment = increment;
            }

            public Builder lastCPU(int lastCPU) {
                Assume.notNegative(lastCPU);
                Assume.notGreater(firstCPU, lastCPU);
                this.lastCPU = lastCPU;
                return this;
            }

            public Builder cycle() {
                cycle = true;
                return this;
            }

            public Spread build() {
                return new Spread(this);
            }
        }
    }
}
