package eu.menzani.ringbuffer;

public class PrefilledOneToOneTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.prefilled(Test.NUM_ITERATIONS + 1, Event::new)
                .oneReader()
                .oneWriter()
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
