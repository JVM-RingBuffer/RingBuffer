package test.marshalling;

import eu.menzani.ringbuffer.marshalling.MarshallingRingBuffer;
import test.TestThread;
import test.TestThreadGroup;

import static eu.menzani.ringbuffer.marshalling.Offsets.*;

class Writer extends TestThread {
    static TestThreadGroup startGroupAsync(MarshallingRingBuffer ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new Writer(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static void runGroupAsync(MarshallingRingBuffer ringBuffer) {
        startGroupAsync(ringBuffer).waitForCompletion();
    }

    static Writer startAsync(int numIterations, MarshallingRingBuffer ringBuffer) {
        Writer writer = new Writer(numIterations, ringBuffer);
        writer.startNow();
        return writer;
    }

    static void runAsync(int numIterations, MarshallingRingBuffer ringBuffer) {
        startAsync(numIterations, ringBuffer).waitForCompletion();
    }

    private Writer(int numIterations, MarshallingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected String getProfilerName() {
        return "Writer";
    }

    @Override
    protected void loop() {
        MarshallingRingBuffer ringBuffer = getHeapRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int offset = ringBuffer.next();
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset + INT);
        }
    }
}
