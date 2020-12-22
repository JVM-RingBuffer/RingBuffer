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

package test;

import eu.menzani.lang.Check;
import eu.menzani.util.PropertyConfiguration;

import java.nio.file.Path;

public class Config {
    static final int hardwareThreadsPerCore;
    public static final Path javaRuntime;
    static final int concurrentProducersAndConsumers;

    static {
        PropertyConfiguration configuration = new PropertyConfiguration(Path.of("cfg", "test.properties"));
        hardwareThreadsPerCore = configuration.getInt("hardware-threads-per-core", 2);
        javaRuntime = configuration.getPath("java-runtime", Path.of("java"));
        concurrentProducersAndConsumers = configuration.getInt("concurrent-producers-and-consumers", Runtime.getRuntime().availableProcessors() / hardwareThreadsPerCore / 2, "USE_ALL_CPUS");
        configuration.saveDefault();
        Check.positive(hardwareThreadsPerCore);
        Check.notLesser(concurrentProducersAndConsumers, 2);
    }
}
