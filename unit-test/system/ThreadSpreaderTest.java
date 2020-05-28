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

package org.ringbuffer.system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreadSpreaderTest {
    @Test
    void testNonCyclying() {
        ThreadSpreader spreader = Threads.spreadOverCPUs().fromFirstCPU().toCPU(5).skipHyperthreads().build();
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
        assertEquals(4, spreader.nextCPU());
        assertThrows(ThreadManipulationException.class, spreader::nextCPU);
        assertThrows(ThreadManipulationException.class, spreader::nextCPU);
        spreader.reset();
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
        assertEquals(4, spreader.nextCPU());
        assertThrows(ThreadManipulationException.class, spreader::nextCPU);
        assertThrows(ThreadManipulationException.class, spreader::nextCPU);
    }

    @Test
    void testCyclying() {
        ThreadSpreader spreader = Threads.spreadOverCPUs().fromFirstCPU().toCPU(5).skipHyperthreads().cycle().build();
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
        assertEquals(4, spreader.nextCPU());
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
        spreader.reset();
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
        assertEquals(4, spreader.nextCPU());
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
    }
}
