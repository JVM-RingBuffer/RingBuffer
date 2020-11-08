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

import test.AbstractRingBufferTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ProcessStarter extends Thread {
    private static final Class<?> poisonPill = ProcessStarter.class;

    private final BlockingQueue<Class<?>> queue = new LinkedBlockingQueue<>();
    private final TestRunner testRunner;

    ProcessStarter(TestRunner testRunner) {
        this.testRunner = testRunner;
    }

    void runTest(Class<? extends AbstractRingBufferTest> testClass) {
        queue.add(testClass);
    }

    void terminate() {
        queue.add(poisonPill);
    }

    @Override
    public void run() {
        String classPath = System.getProperty("java.class.path");
        List<String> command = new ArrayList<>(List.of("java", "-Xms8g", "-Xmx8g", "-XX:+UseLargePages", "-XX:+AlwaysPreTouch", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseEpsilonGC", "-XX:-RestrictContended", "-XX:-UseBiasedLocking", "--add-opens", "java.base/jdk.internal.misc=ALL-UNNAMED", "-classpath", classPath));
        command.add(null);
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);

        try {
            Class<?> testClass;
            while ((testClass = queue.take()) != poisonPill) {
                command.set(13, testClass.getName());
                Process process = builder.start();
                process.waitFor();
                String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.ISO_8859_1);
                testRunner.setOutput(output);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
