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

import org.ringbuffer.Native;
import org.ringbuffer.lang.Assume;

public class Threads {
    static {
        Native.init();
    }

    public static void bindCurrentThreadToCPU(int cpu) {
        Assume.notNegative(cpu);
        int errorCode = bindCurrentThread(cpu);
        if (errorCode != 0) {
            throw new ThreadManipulationException(errorCode);
        }
    }

    private static native int bindCurrentThread(int cpu);

    /**
     * On Linux, if not running under root, you need to add this to {@code /etc/security/limits.conf}:
     *
     * <pre>{@code
     * <user> hard rtprio 99
     * <user> soft rtprio 99
     * }</pre>
     */
    public static void setCurrentThreadPriorityToRealtime() {
        int errorCode = setCurrentThreadPriority();
        if (errorCode != 0) {
            throw new ThreadManipulationException(errorCode);
        }
    }

    private static native int setCurrentThreadPriority();

    public static ThreadSpreader.Builder spreadOverCPUs() {
        return new ThreadSpreader.Builder();
    }
}
