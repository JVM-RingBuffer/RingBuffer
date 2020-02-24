package eu.menzani.ringbuffer;

import java.util.ArrayList;
import java.util.List;

public class PrefilledManyReadersBlockingTest extends RingBufferTest {
    public PrefilledManyReadersBlockingTest() {
        super(AtomicReadBlockingOrDiscardingRingBuffer.class, RingBuffer.prefilled(SMALL_BUFFER_SIZE, Event::new)
                .manyReaders()
                .oneWriter()
                .blocking());
    }

    int run() throws InterruptedException {
        List<Reader> readers = new ArrayList<>();
        for (int i = 0; i < CONCURRENCY; i++) {
            readers.add(new Reader(NUM_ITERATIONS));
        }
        new PrefilledWriter(TOTAL_ELEMENTS);
        for (Reader reader : readers) {
            reader.join();
        }
        return readers.stream().mapToInt(Reader::getSum).sum();
    }
}
