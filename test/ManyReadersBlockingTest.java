package eu.menzani.ringbuffer;

import java.util.ArrayList;
import java.util.List;

public class ManyReadersBlockingTest extends RingBufferTest {
    public ManyReadersBlockingTest() {
        super(AtomicReadBlockingOrDiscardingRingBuffer.class, RingBuffer.<Event>empty(SMALL_BUFFER_SIZE)
                .manyReaders()
                .oneWriter()
                .blocking()
                .withGC());
    }

    int run() throws InterruptedException {
        List<Reader> readers = new ArrayList<>();
        for (int i = 0; i < CONCURRENCY; i++) {
            readers.add(new Reader(NUM_ITERATIONS));
        }
        new Writer(TOTAL_ELEMENTS);
        for (Reader reader : readers) {
            reader.join();
        }
        return readers.stream().mapToInt(Reader::getSum).sum();
    }
}
