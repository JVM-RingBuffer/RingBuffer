package eu.menzani.ringbuffer;

public class PrefilledOneToOneBlockingTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.prefilled(5, Event::new)
                .oneReader()
                .oneWriter()
                .blocking()
                .build();

        run();
        run();
    }

    private static void run() throws InterruptedException {
        Reader reader = new Reader(Test.NUM_ITERATIONS);
        new PrefilledWriter(Test.NUM_ITERATIONS);
        reader.join();
        System.out.println(reader.getSum());
    }
}
