package eu.menzani.ringbuffer;

public class PrefilledManyWritersBlockingTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.prefilled(5, Event::new)
                .oneReader()
                .manyWriters()
                .blocking()
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
