/*
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

import org.ringbuffer.InternalUnsafe;

public class CleanerService {
    public static void freeMemory(Object object, long... addresses) {
        Cleaner.value.register(object, new FreeMemory(addresses));
    }

    public static void freeMemory(long address) {
        InternalUnsafe.UNSAFE.freeMemory(address);
    }

    private static class FreeMemory implements Runnable {
        private final long[] addresses;

        FreeMemory(long[] addresses) {
            this.addresses = addresses;
        }

        @Override
        public void run() {
            for (long address : addresses) {
                freeMemory(address);
            }
        }
    }

    private static class Cleaner {
        static final java.lang.ref.Cleaner value = java.lang.ref.Cleaner.create();
    }
}
