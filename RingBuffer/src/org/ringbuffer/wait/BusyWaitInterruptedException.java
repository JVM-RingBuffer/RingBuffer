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

package org.ringbuffer.wait;

public class BusyWaitInterruptedException extends BusyWaitException {
    public static final BusyWaitInterruptedException WHILE_READING = new BusyWaitInterruptedException(true);
    public static final BusyWaitInterruptedException WHILE_WRITING = new BusyWaitInterruptedException(false);

    public static BusyWaitInterruptedException getInstance(boolean whileReading) {
        return whileReading ? WHILE_READING : WHILE_WRITING;
    }

    private static final long serialVersionUID = 0L;

    protected BusyWaitInterruptedException(boolean wasReading) {
        super(null, wasReading);
    }
}
