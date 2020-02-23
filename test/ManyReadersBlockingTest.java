package eu.menzani.ringbuffer;

import java.util.ArrayList;
import java.util.List;

public class ManyReadersBlockingTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.<Event>empty(5)
                .manyReaders()
                .oneWriter()
                .blocking()
                .withGC()
                .build();

        run();
        run();
    }

    private static void run() throws InterruptedException {
        List<Reader> readers = new ArrayList<>();
        for (int i = 0; i < Test.CONCURRENCY; i++) {
            readers.add(new Reader(Test.NUM_ITERATIONS));
        }
        new Writer(Test.TOTAL_ELEMENTS);
        for (Reader reader : readers) {
            reader.join();
        }
        System.out.println(readers.stream().mapToInt(Reader::getSum).sum());
    }
}
