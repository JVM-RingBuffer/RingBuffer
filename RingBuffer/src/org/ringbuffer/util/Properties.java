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

import org.ringbuffer.lang.Lang;
import org.ringbuffer.lang.Optional;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class Properties {
    private static final Charset charset = StandardCharsets.UTF_16;

    private final Map<String, String> data = new LinkedHashMap<>();

    void load(Path path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(path, charset);
        } catch (IOException e) {
            throw Lang.uncheck(e);
        }
        for (String line : lines) {
            int indexOfEquals = line.indexOf('=');
            if (indexOfEquals == -1) {
                continue;
            }
            data.put(line.substring(0, indexOfEquals), line.substring(indexOfEquals + 1));
        }
    }

    void store(Path path) {
        List<String> lines = new ArrayList<>(data.size());
        for (var entry : data.entrySet()) {
            lines.add(entry.getKey() + '=' + entry.getValue());
        }
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, lines, charset);
        } catch (IOException e) {
            throw Lang.uncheck(e);
        }
    }

    @Optional String getProperty(String key) {
        return data.get(key);
    }

    void setProperty(String key, String value) {
        data.put(key, value);
    }
}
