package test.marshalling;

import eu.menzani.ringbuffer.marshalling.HeapRingBuffer;
import test.TestThread;
import test.TestThreadGroup;

import static eu.menzani.ringbuffer.marshalling.Offsets.*;

class Writer extends TestThread {
    static TestThreadGroup startGroupAsync(HeapRingBuffer ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(new Factory() {
            @Override
            public TestThread newInstance(int numIterations) {
                return new Writer(numIterations, ringBuffer);
            }
        });
        group.start();
        return group;
    }

    static void runGroupAsync(HeapRingBuffer ringBuffer) {
        startGroupAsync(ringBuffer).waitForCompletion();
    }

    static Writer startAsync(int numIterations, HeapRingBuffer ringBuffer) {
        Writer writer = new Writer(numIterations, ringBuffer);
        writer.startNow();
        return writer;
    }

    static void runAsync(int numIterations, HeapRingBuffer ringBuffer) {
        startAsync(numIterations, ringBuffer).waitForCompletion();
    }

    private Writer(int numIterations, HeapRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected String getProfilerName() {
        return "Writer";
    }

    @Override
    protected void loop() {
        HeapRingBuffer ringBuffer = getHeapRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int offset = ringBuffer.next();
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset + INT);
        }
    }
}
