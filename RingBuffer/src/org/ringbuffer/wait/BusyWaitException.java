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

public class BusyWaitException extends RuntimeException {
    private static final long serialVersionUID = 0L;

    private final boolean wasReading;

    public static BusyWaitException whileReading(String message) {
        return new BusyWaitException(message, null, true, true, true);
    }

    public static BusyWaitException whileWriting(String message) {
        return new BusyWaitException(message, null, true, true, false);
    }

    public BusyWaitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, boolean wasReading) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.wasReading = wasReading;
    }

    public boolean wasReading() {
        return wasReading;
    }

    public boolean wasWriting() {
        return !wasReading;
    }
}
