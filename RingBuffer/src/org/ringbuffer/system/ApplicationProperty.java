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

import org.ringbuffer.lang.Optional;

public class ApplicationProperty {
    private final String value;

    public ApplicationProperty(Class<?> clazz, String first, String... more) {
        this(clazz.getPackageName(), first, more);
    }

    private ApplicationProperty(String initialValue, String first, String[] more) {
        StringBuilder builder = new StringBuilder(initialValue);
        builder.append('.');
        builder.append(first);
        for (String segment : more) {
            builder.append('.');
            builder.append(segment);
        }
        value = builder.toString();
    }

    public @Optional String get() {
        return System.getProperty(value);
    }

    public boolean getAsBoolean() {
        return get() != null;
    }

    public ApplicationProperty resolve(String first, String... more) {
        return new ApplicationProperty(value, first, more);
    }
}
