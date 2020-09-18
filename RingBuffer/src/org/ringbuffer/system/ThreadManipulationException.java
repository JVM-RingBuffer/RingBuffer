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

import org.ringbuffer.lang.Assume;
import org.ringbuffer.lang.Ensure;

public class ThreadManipulationException extends RuntimeException {
    private static final long serialVersionUID = 0L;

    private final int errorCode;

    ThreadManipulationException(Throwable cause) {
        super(cause);
        errorCode = 0;
    }

    ThreadManipulationException(String message) {
        super(message);
        errorCode = 0;
    }

    ThreadManipulationException(int errorCode) {
        super("Error code = " + errorCode);
        Assume.notZero(errorCode);
        this.errorCode = errorCode;
    }

    public boolean hasErrorCode() {
        return errorCode != 0;
    }

    public int getErrorCode() {
        Ensure.notZero(errorCode);
        return errorCode;
    }
}
