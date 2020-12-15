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

package org.ringbuffer.lang;

import java.io.PrintStream;
import java.io.PrintWriter;

public class UncaughtException extends RuntimeException {
    private static final long serialVersionUID = 0L;

    private final Throwable delegate;

    public UncaughtException(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        delegate = throwable;
        fillInStackTrace();
    }

    public Throwable getThrowable() {
        return delegate;
    }

    @Override
    public String getMessage() {
        return delegate.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return delegate.getLocalizedMessage();
    }

    @Override
    public Throwable getCause() {
        return delegate.getCause();
    }

    @Override
    public Throwable initCause(Throwable cause) {
        return delegate.initCause(cause);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public void printStackTrace() {
        delegate.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        delegate.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        delegate.printStackTrace(s);
    }

    @Override
    public Throwable fillInStackTrace() {
        if (delegate == null) {
            return null;
        }
        return super.fillInStackTrace();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return delegate.getStackTrace();
    }

    @Override
    public void setStackTrace(StackTraceElement[] stackTrace) {
        delegate.setStackTrace(stackTrace);
    }
}
