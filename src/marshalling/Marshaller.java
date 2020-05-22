package eu.menzani.ringbuffer.marshalling;

class Marshaller {
    static void serializeByte(byte[] array, int mask, int index, byte value) {
        array[index & mask] = value;
    }

    static void serializeChar(byte[] array, int mask, int index, char value) {
        array[index & mask] = (byte) ((value >>> 8) & 0xFF);
        array[(index + 1) & mask] = (byte) (value & 0xFF);
    }

    static void serializeShort(byte[] array, int mask, int index, short value) {
        array[index & mask] = (byte) ((value >>> 8) & 0xFF);
        array[(index + 1) & mask] = (byte) (value & 0xFF);
    }

    static void serializeInt(byte[] array, int mask, int index, int value) {
        array[index & mask] = (byte) ((value >>> 24) & 0xFF);
        array[(index + 1) & mask] = (byte) ((value >>> 16) & 0xFF);
        array[(index + 2) & mask] = (byte) ((value >>> 8) & 0xFF);
        array[(index + 3) & mask] = (byte) (value & 0xFF);
    }

    static void serializeLong(byte[] array, int mask, int index, long value) {
        array[index & mask] = (byte) (value >>> 56);
        array[(index + 1) & mask] = (byte) (value >>> 48);
        array[(index + 2) & mask] = (byte) (value >>> 40);
        array[(index + 3) & mask] = (byte) (value >>> 32);
        array[(index + 4) & mask] = (byte) (value >>> 24);
        array[(index + 5) & mask] = (byte) (value >>> 16);
        array[(index + 6) & mask] = (byte) (value >>> 8);
        array[(index + 7) & mask] = (byte) value;
    }

    static void serializeBoolean(byte[] array, int mask, int index, boolean value) {
        serializeByte(array, mask, index, value ? (byte) 1 : (byte) 0);
    }

    static void serializeFloat(byte[] array, int mask, int index, float value) {
        serializeInt(array, mask, index, Float.floatToIntBits(value));
    }

    static void serializeDouble(byte[] array, int mask, int index, double value) {
        serializeLong(array, mask, index, Double.doubleToLongBits(value));
    }

    //

    static byte deserializeByte(byte[] array, int mask, int index) {
        return array[index & mask];
    }

    static char deserializeChar(byte[] array, int mask, int index) {
        return (char) ((array[index & mask] << 8) + array[(index + 1) & mask]);
    }

    static short deserializeShort(byte[] array, int mask, int index) {
        return (short) ((array[index & mask] << 8) + array[(index + 1) & mask]);
    }

    static int deserializeInt(byte[] array, int mask, int index) {
        return (array[index & mask] << 24) +
                (array[(index + 1) & mask] << 16) +
                (array[(index + 2) & mask] << 8) +
                array[(index + 3) & mask];
    }

    static long deserializeLong(byte[] array, int mask, int index) {
        return ((long) array[index & mask] << 56) +
                ((long) (array[(index + 1) & mask] & 255) << 48) +
                ((long) (array[(index + 2) & mask] & 255) << 40) +
                ((long) (array[(index + 3) & mask] & 255) << 32) +
                ((long) (array[(index + 4) & mask] & 255) << 24) +
                ((array[(index + 5) & mask] & 255) << 16) +
                ((array[(index + 6) & mask] & 255) << 8) +
                (array[(index + 7) & mask] & 255);
    }

    static boolean deserializeBoolean(byte[] array, int mask, int index) {
        return deserializeByte(array, mask, index) == (byte) 1;
    }

    static float deserializeFloat(byte[] array, int mask, int index) {
        return Float.intBitsToFloat(deserializeInt(array, mask, index));
    }

    static double deserializeDouble(byte[] array, int mask, int index) {
        return Double.longBitsToDouble(deserializeLong(array, mask, index));
    }
}
