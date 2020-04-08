package eu.menzani.ringbuffer.system;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.java.Immutable;

import java.io.Serializable;

@Immutable
public final class MemorySize implements Comparable<MemorySize>, Serializable {
    public static final MemorySize ZERO = new MemorySize(0L);
    private static final long serialVersionUID = 0L;

    private final long bytes;

    public static MemorySize ofBytes(long bytes) {
        return new MemorySize(bytes);
    }

    public static MemorySize ofKilobytes(double kilobytes) {
        return new MemorySize(kilobytes * 1024D);
    }

    public static MemorySize ofMegabytes(double megabytes) {
        return new MemorySize(megabytes * (1024D * 1024D));
    }

    public static MemorySize ofGigabytes(double gigabytes) {
        return new MemorySize(gigabytes * (1024D * 1024D * 1024D));
    }

    public static MemorySize ofTerabytes(double terabytes) {
        return new MemorySize(terabytes * (1024D * 1024D * 1024D * 1024D));
    }

    public static MemorySize ofPetabytes(double petabytes) {
        return new MemorySize(petabytes * (1024D * 1024D * 1024D * 1024D * 1024D));
    }

    private MemorySize(long bytes) {
        Assume.notNegative(bytes);
        this.bytes = bytes;
    }

    private MemorySize(double bytes) {
        this(Math.round(bytes));
    }

    public Unit getUnit() {
        if (bytes < 1024L) {
            return Unit.BYTE;
        }
        if (bytes < 1024L * 1024L) {
            return Unit.KILOBYTE;
        }
        if (bytes < 1024L * 1024L * 1024L) {
            return Unit.MEGABYTE;
        }
        if (bytes < 1024L * 1024L * 1024L * 1024L) {
            return Unit.GIGABYTE;
        }
        if (bytes < 1024L * 1024L * 1024L * 1024L * 1024L) {
            return Unit.TERABYTE;
        }
        return Unit.PETABYTE;
    }

    public long toBytes() {
        return bytes;
    }

    public double toKilobytes() {
        return bytes / 1024D;
    }

    public double toMegabytes() {
        return bytes / (1024D * 1024D);
    }

    public double toGigabytes() {
        return bytes / (1024D * 1024D * 1024D);
    }

    public double toTerabytes() {
        return bytes / (1024D * 1024D * 1024D * 1024D);
    }

    public double toPetabytes() {
        return bytes / (1024D * 1024D * 1024D * 1024D * 1024D);
    }

    public long toWholeKilobytes() {
        return Math.round(toKilobytes());
    }

    public long toWholeMegabytes() {
        return Math.round(toMegabytes());
    }

    public long toWholeGigabytes() {
        return Math.round(toGigabytes());
    }

    public long toWholeTerabytes() {
        return Math.round(toTerabytes());
    }

    public long toWholePetabytes() {
        return Math.round(toPetabytes());
    }

    public int getBytes() {
        return narrowToInt(bytes);
    }

    public int getKilobytes() {
        return narrowToInt(toWholeKilobytes());
    }

    public int getMegabytes() {
        return narrowToInt(toWholeMegabytes());
    }

    public int getGigabytes() {
        return narrowToInt(toWholeGigabytes());
    }

    public int getTerabytes() {
        return narrowToInt(toWholeTerabytes());
    }

    public int getPetabytes() {
        return narrowToInt(toWholePetabytes());
    }

    private int narrowToInt(long value) {
        if (value > Integer.MAX_VALUE) {
            throw new ArithmeticException(toString() + " cannot be represented as int.");
        }
        return (int) value;
    }

    public boolean isBiggerThan(MemorySize memorySize) {
        return bytes > memorySize.bytes;
    }

    public boolean isSmallerThan(MemorySize memorySize) {
        return bytes < memorySize.bytes;
    }

    public MemorySize add(MemorySize memorySize) {
        return new MemorySize(bytes + memorySize.bytes);
    }

    public MemorySize subtract(MemorySize memorySize) {
        return new MemorySize(bytes - memorySize.bytes);
    }

    public MemorySize plusBytes(long bytes) {
        return new MemorySize(this.bytes + bytes);
    }

    public MemorySize plusKilobytes(double kilobytes) {
        return new MemorySize(bytes + kilobytes * 1024D);
    }

    public MemorySize plusMegabytes(double megabytes) {
        return new MemorySize(bytes + megabytes * (1024D * 1024D));
    }

    public MemorySize plusGigabytes(double gigabytes) {
        return new MemorySize(bytes + gigabytes * (1024D * 1024D * 1024D));
    }

    public MemorySize plusTerabytes(double terabytes) {
        return new MemorySize(bytes + terabytes * (1024D * 1024D * 1024D * 1024D));
    }

    public MemorySize plusPetabytes(double petabytes) {
        return new MemorySize(bytes + petabytes * (1024D * 1024D * 1024D * 1024D * 1024D));
    }

    public MemorySize minusBytes(long bytes) {
        return new MemorySize(this.bytes - bytes);
    }

    public MemorySize minusKilobytes(double kilobytes) {
        return new MemorySize(bytes - kilobytes * 1024D);
    }

    public MemorySize minusMegabytes(double megabytes) {
        return new MemorySize(bytes - megabytes * (1024D * 1024D));
    }

    public MemorySize minusGigabytes(double gigabytes) {
        return new MemorySize(bytes - gigabytes * (1024D * 1024D * 1024D));
    }

    public MemorySize minusTerabytes(double terabytes) {
        return new MemorySize(bytes - terabytes * (1024D * 1024D * 1024D * 1024D));
    }

    public MemorySize minusPetabytes(double petabytes) {
        return new MemorySize(bytes - petabytes * (1024D * 1024D * 1024D * 1024D * 1024D));
    }

    public MemorySize multiply(long factor) {
        return new MemorySize(bytes * factor);
    }

    public MemorySize divide(double dividend) {
        return new MemorySize(bytes / dividend);
    }

    public MemorySize getNextPowerOfTwo() {
        long highestOneBit = Long.highestOneBit(bytes);
        if (bytes == highestOneBit) {
            return this;
        }
        return new MemorySize(highestOneBit << 1);
    }

    @Override
    public int compareTo(MemorySize memorySize) {
        return Long.compare(bytes, memorySize.bytes);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        return bytes == ((MemorySize) object).bytes;
    }

    @Override
    public int hashCode() {
        return (int) (bytes ^ (bytes >>> 32));
    }

    @Override
    public String toString() {
        Unit unit = getUnit();
        return toWholeNumber(unit) + " " + unit.symbol;
    }

    private long toWholeNumber(Unit unit) {
        switch (unit) {
            case BYTE:
                return bytes;
            case KILOBYTE:
                return toWholeKilobytes();
            case MEGABYTE:
                return toWholeMegabytes();
            case GIGABYTE:
                return toWholeGigabytes();
            case TERABYTE:
                return toWholeTerabytes();
            case PETABYTE:
                return toWholePetabytes();
        }
        throw new AssertionError();
    }

    public enum Unit {
        BYTE("bytes"),
        KILOBYTE("KB"),
        MEGABYTE("MB"),
        GIGABYTE("GB"),
        TERABYTE("TB"),
        PETABYTE("PB");

        private final String symbol;

        Unit(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}
