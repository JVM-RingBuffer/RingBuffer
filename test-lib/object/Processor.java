/*
 * Copyright 2020 Francesco Menzani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.object;

import org.ringbuffer.object.ObjectRingBuffer;
import org.ringbuffer.object.RingBuffer;

import static test.object.ProducersToProcessorToConsumersContentionTest.*;

class Processor extends TestThread {
    static Processor startAsync(int numIterations, ObjectRingBuffer<Event> producersRingBuffer) {
        Processor processor = new Processor(numIterations, producersRingBuffer);
        processor.startNow(null);
        return processor;
    }

    static void runAsync(int numIterations, ObjectRingBuffer<Event> producersRingBuffer) {
        startAsync(numIterations, producersRingBuffer).waitForCompletion(null);
    }

    private Processor(int numIterations, ObjectRingBuffer<Event> producersRingBuffer) {
        super(numIterations, producersRingBuffer);
    }

    @Override
    protected void loop() {
        RingBuffer<Event> producersRingBuffer = getEmptyRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int eventData = producersRingBuffer.take().getData();
            int key = CONSUMERS_RING_BUFFER.nextKey();
            CONSUMERS_RING_BUFFER.next(key).setData(eventData);
            CONSUMERS_RING_BUFFER.put(key);
        }
    }
}
