package eu.menzani.ringbuffer;

public class PrefilledManyReadersTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.prefilled(Test.TOTAL_ELEMENTS + 1, Event::new)
                .manyReaders()
                .oneWriter()
                .build();

        ManyReadersTest.run();
        ManyReadersTest.run();
    }
}
