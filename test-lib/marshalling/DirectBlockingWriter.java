package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import test.Profiler;
import test.TestThreadGroup;

import static eu.menzani.ringbuffer.marshalling.DirectOffsets.*;

class DirectBlockingWriter extends TestThread {
    static TestThreadGroup startGroupAsync(DirectMarshallingBlockingRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new DirectBlockingWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(DirectMarshallingBlockingRingBuffer ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static DirectBlockingWriter startAsync(int numIterations, DirectMarshallingBlockingRingBuffer ringBuffer, Profiler profiler) {
        DirectBlockingWriter writer = new DirectBlockingWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, DirectMarshallingBlockingRingBuffer ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private DirectBlockingWriter(int numIterations, DirectMarshallingBlockingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        DirectMarshallingBlockingRingBuffer ringBuffer = getDirectMarshallingBlockingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            long offset = ringBuffer.next(INT);
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset + INT);
        }
    }
}
