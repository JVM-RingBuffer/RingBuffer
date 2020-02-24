package eu.menzani.ringbuffer;

import java.util.ArrayList;
import java.util.List;

public class PrefilledManyReadersTest extends RingBufferTest {
    public PrefilledManyReadersTest() {
        super(AtomicReadRingBuffer.class, RingBuffer.prefilled(TOTAL_ELEMENTS + 1, Event::new)
                .manyReaders()
                .oneWriter());
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
