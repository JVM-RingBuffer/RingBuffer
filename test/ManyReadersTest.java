package eu.menzani.ringbuffer;

import java.util.ArrayList;
import java.util.List;

public class ManyReadersTest extends RingBufferTest {
    public ManyReadersTest() {
        super(AtomicReadRingBuffer.class, RingBuffer.<Event>empty(TOTAL_ELEMENTS + 1)
                .manyReaders()
                .oneWriter());
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
