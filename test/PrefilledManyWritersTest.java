package eu.menzani.ringbuffer;

public class PrefilledManyWritersTest {
    public static void main(String[] args) throws InterruptedException {
        Test.ringBuffer = RingBuffer.prefilled(Test.TOTAL_ELEMENTS + 1, Event::new)
                .oneReader()
                .manyWriters()
                .build();

        ManyWritersTest.run();
        ManyWritersTest.run();
    }
}
