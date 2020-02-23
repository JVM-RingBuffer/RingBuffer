package eu.menzani.ringbuffer;

public class OneToOneTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.<Event>empty(Test.NUM_ITERATIONS + 1)
                .oneReader()
                .oneWriter()
                .build();

        _run();
        _run();
    }

    private static void _run() throws InterruptedException {
        new Writer(Test.NUM_ITERATIONS);
        run();
    }

    static void run() throws InterruptedException {
        Reader reader = new Reader(Test.NUM_ITERATIONS);
        reader.join();
        System.out.println(reader.getSum());
    }
}
