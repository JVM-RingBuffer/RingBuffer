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
        new PrefilledWriter(Test.NUM_ITERATIONS);
        OneToOneTest.run();
    }
}
