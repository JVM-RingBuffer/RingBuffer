package eu.menzani.ringbuffer.system;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.java.Ensure;

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

    public static Spread.Builder spread() {
        return new Spread.Builder();
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

        Spread(Builder builder) {
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
            private int firstCPU = -1;
            private int increment = -1;
            private int lastCPU = -1;
            private boolean cycle;

            public Builder firstCPU(int firstCPU) {
                Assume.notNegative(firstCPU);
                this.firstCPU = firstCPU;
                return this;
            }

            public Builder fromFirstCPU() {
                firstCPU(0);
                return this;
            }

            public Builder increment(int increment) {
                Assume.notLesser(increment, 1);
                this.increment = increment;
                return this;
            }

            public Builder skipHyperthreads() {
                increment(2);
                return this;
            }

            public Builder lastCPU(int lastCPU) {
                Assume.notNegative(lastCPU);
                this.lastCPU = lastCPU;
                return this;
            }

            public Builder toLastCPU() {
                lastCPU(Runtime.getRuntime().availableProcessors() - 1);
                return this;
            }

            public Builder cycle() {
                cycle = true;
                return this;
            }

            public Spread build() {
                if (firstCPU == -1) {
                    throw new IllegalStateException("firstCPU not set");
                }
                if (increment == -1) {
                    throw new IllegalStateException("increment not set");
                }
                if (lastCPU == -1) {
                    throw new IllegalStateException("lastCPU not set");
                }
                Ensure.notGreater(firstCPU, lastCPU);
                return new Spread(this);
            }
        }
    }
}
