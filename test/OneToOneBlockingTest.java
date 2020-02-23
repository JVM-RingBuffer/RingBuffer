package eu.menzani.ringbuffer;

public class OneToOneBlockingTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.<Event>empty(5)
                .oneReader()
                .oneWriter()
                .blocking()
                .withGC()
                .build();

        run();
        run();
    }

    private static void run() throws InterruptedException {
        Reader reader = new Reader(Test.NUM_ITERATIONS);
        new Writer(Test.NUM_ITERATIONS);
        reader.join();
        System.out.println(reader.getSum());
    }
}
