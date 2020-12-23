package test.competitors;

import test.AbstractTestThread;
import test.object.Event;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

abstract class TestThread extends AbstractTestThread {
    TestThread(int numIterations, Queue<Event> queue) {
        super(numIterations, queue);
    }

    @SuppressWarnings("unchecked")
    Queue<Event> getQueue() {
        return (Queue<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    BlockingQueue<Event> getBlockingQueue() {
        return (BlockingQueue<Event>) dataStructure;
    }
}
