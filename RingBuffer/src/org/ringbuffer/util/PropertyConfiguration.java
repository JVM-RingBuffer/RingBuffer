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

package org.ringbuffer.util;

import java.nio.file.Files;
import java.nio.file.Path;

public class PropertyConfiguration {
    private final Path file;
    private final Properties properties = new Properties();
    private final Properties defaultProperties = new Properties();

    public PropertyConfiguration(Path file) {
        if (Files.exists(file)) {
            properties.load(file);
        }
        this.file = file;
    }

    public String getString(String key, String defaultValue) {
        defaultProperties.setProperty(key, defaultValue);
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public int getInt(String key, int defaultValue) {
        defaultProperties.setProperty(key, Integer.toString(defaultValue));
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public int getInt(String key, int defaultValue, String defaultPropertyValue) {
        defaultProperties.setProperty(key, defaultPropertyValue);
        String value = properties.getProperty(key);
        if (value == null || value.equals(defaultPropertyValue)) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public void saveDefault() {
        if (Files.notExists(file)) {
            defaultProperties.store(file);
        }
    }
}
