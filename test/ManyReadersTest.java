package eu.menzani.ringbuffer;

public class ManyReadersTest extends RingBufferTest {
    public ManyReadersTest() {
        super(AtomicReadRingBuffer.class, 17999997000000L, RingBuffer.<Event>empty(TOTAL_ELEMENTS + 1)
                .manyReaders()
                .oneWriter());
    }

    long run() {
        TestThreadGroup readerGroup = Reader.newGroup(ringBuffer);
        Writer writer = new Writer(TOTAL_ELEMENTS, ringBuffer);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
