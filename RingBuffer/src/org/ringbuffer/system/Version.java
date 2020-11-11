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

public enum Version {
    JAVA_11, JAVA_12, JAVA_13, JAVA_14, JAVA_15, JAVA_16, JAVA_17, JAVA_18, JAVA_19;

    public boolean isNewerThan(Version other) {
        return compareTo(other) > 0;
    }

    public boolean isOlderThan(Version other) {
        return compareTo(other) < 0;
    }

    public boolean isNotOlderThan(Version other) {
        return compareTo(other) >= 0;
    }

    public boolean isNotNewerThan(Version other) {
        return compareTo(other) <= 0;
    }

    public static Version current() {
        return Current.value;
    }

    private static class Current {
        private static final Version value;

        static {
            switch (System.getProperty("java.version").charAt(1)) {
                case '1':
                    value = JAVA_11;
                    break;
                case '2':
                    value = JAVA_12;
                    break;
                case '3':
                    value = JAVA_13;
                    break;
                case '4':
                    value = JAVA_14;
                    break;
                case '5':
                    value = JAVA_15;
                    break;
                case '6':
                    value = JAVA_16;
                    break;
                case '7':
                    value = JAVA_17;
                    break;
                case '8':
                    value = JAVA_18;
                    break;
                case '9':
                    value = JAVA_19;
                    break;
                default:
                    throw new AssertionError("Version is unknown.");
            }
        }
    }
}
