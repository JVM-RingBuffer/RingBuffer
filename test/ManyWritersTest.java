package eu.menzani.ringbuffer;

public class ManyWritersTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.<Event>empty(Test.TOTAL_ELEMENTS + 1)
                .oneReader()
                .manyWriters()
                .build();

        run();
        run();
    }

    private static void run() throws InterruptedException {
        Reader reader = new Reader(Test.TOTAL_ELEMENTS);
        for (int i = 0; i < Test.CONCURRENCY; i++) {
            new Writer(Test.NUM_ITERATIONS);
        }
        reader.join();
        System.out.println(reader.getSum());
    }
}
