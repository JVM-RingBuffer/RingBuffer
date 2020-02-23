package eu.menzani.ringbuffer;

import java.util.ArrayList;
import java.util.List;

public class PrefilledManyReadersBlockingTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.prefilled(5, Event::new)
                .manyReaders()
                .oneWriter()
                .blocking()
                .build();

        run();
        run();
    }

    // FIXME Make test pass
    private static void run() throws InterruptedException {
        List<Reader> readers = new ArrayList<>();
        for (int i = 0; i < Test.CONCURRENCY; i++) {
            readers.add(new Reader(Test.NUM_ITERATIONS));
        }
        new PrefilledWriter(Test.TOTAL_ELEMENTS);
        for (Reader reader : readers) {
            reader.join();
        }
        System.out.println(readers.stream().mapToInt(Reader::getSum).sum());
    }
}
