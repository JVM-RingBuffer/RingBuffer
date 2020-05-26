package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.AbstractRingBufferBuilder;
import eu.menzani.ringbuffer.object.EmptyRingBufferBuilder;
import eu.menzani.ringbuffer.object.PrefilledOverwritingRingBufferBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferBuilderTest {
    private EmptyRingBufferBuilder<?> builder;

    @BeforeEach
    void setUp() {
        builder = new EmptyRingBufferBuilder<>(2);
    }

    @Test
    void testFillerNotSet() {
        AbstractRingBufferBuilder<?> builder = new PrefilledOverwritingRingBufferBuilder<>(2);
        builder.oneReader();
        builder.oneWriter();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testConcurrencyNotSet() {
        builder.blocking();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testWriterConcurrencyNotSet() {
        builder.oneReader();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testWriterConcurrencyNotSet2() {
        builder.manyReaders();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testReaderConcurrencyNotSet() {
        builder.oneWriter();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testReaderConcurrencyNotSet2() {
        builder.manyWriters();
        assertThrows(IllegalStateException.class, builder::build);
    }
}
