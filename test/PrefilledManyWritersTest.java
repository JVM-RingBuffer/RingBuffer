package eu.menzani.ringbuffer;

public class PrefilledManyWritersTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.prefilled(Test.TOTAL_ELEMENTS + 1, Event::new)
                .oneReader()
                .manyWriters()
                .build();

        run();
        run();
    }

    private static void run() throws InterruptedException {
        Reader reader = new Reader(Test.TOTAL_ELEMENTS);
        for (int i = 0; i < Test.CONCURRENCY; i++) {
            new PrefilledSynchronizedWriter(Test.NUM_ITERATIONS);
        }
        reader.join();
        System.out.println(reader.getSum());
    }
}
