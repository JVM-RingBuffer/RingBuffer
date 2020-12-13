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

package test.runner;

import org.ringbuffer.lang.Lang;
import test.AbstractRingBufferTest;
import test.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ProcessStarter extends Thread {
    private static final String testModuleName = ProcessStarter.class.getModule().getName();

    private final BlockingQueue<Class<?>> queue = new LinkedBlockingQueue<>();
    private final TestRunner testRunner;

    private Process process;

    ProcessStarter(TestRunner testRunner) {
        this.testRunner = testRunner;
    }

    void runTest(Class<? extends AbstractRingBufferTest> testClass) {
        queue.add(testClass);
    }

    synchronized void terminate() {
        if (process != null) {
            process.destroy();
        }
    }

    @Override
    public void run() {
        List<String> command = new ArrayList<>(List.of(Config.getConfig().getJavaRuntime(), "-Xms8g", "-Xmx8g", "-XX:+UseLargePages", "-XX:+AlwaysPreTouch", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseEpsilonGC", "-XX:-RestrictContended", "-XX:-UseBiasedLocking", "--add-opens", "java.base/jdk.internal.misc=" + Lang.ORG_RINGBUFFER_MODULE.getName(), "-p", System.getProperty("jdk.module.path"), "-m"));
        command.add(null);
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);

        try {
            while (true) {
                Class<?> testClass = queue.take();
                command.set(command.size() - 1, testModuleName + '/' + testClass.getName());
                synchronized (this) {
                    process = builder.start();
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.ISO_8859_1))) {
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line);
                        output.append('\n');
                        testRunner.setOutput(output.toString());
                    }
                    testRunner.allowRun();
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
