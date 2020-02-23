package eu.menzani.ringbuffer;

import java.util.ArrayList;
import java.util.List;

public class PrefilledManyReadersTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.prefilled(Test.TOTAL_ELEMENTS + 1, Event::new)
                .manyReaders()
                .oneWriter()
                .build();

        run();
        run();
    }

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
