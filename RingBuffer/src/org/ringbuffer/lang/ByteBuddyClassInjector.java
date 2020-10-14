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

import net.bytebuddy.dynamic.loading.ClassInjector;
import org.ringbuffer.system.Unsafe;

import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

public class ByteBuddyClassInjector extends ClassInjector.AbstractBase {
    private final ClassLoader classLoader;
    private final ProtectionDomain protectionDomain;

    public ByteBuddyClassInjector(ClassLoader classLoader) {
        this(classLoader, null);
    }

    public ByteBuddyClassInjector(ClassLoader classLoader, ProtectionDomain protectionDomain) {
        this.classLoader = classLoader;
        this.protectionDomain = protectionDomain;
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public Map<String, Class<?>> injectRaw(Map<? extends String, byte[]> types) {
        Map<String, Class<?>> result = new HashMap<>();
        synchronized (classLoader) {
            for (Map.Entry<? extends String, byte[]> entry : types.entrySet()) {
                String name = entry.getKey();
                try {
                    result.put(name, Class.forName(name, false, classLoader));
                } catch (ClassNotFoundException e) {
                    byte[] binaryRepresentation = entry.getValue();
                    result.put(name, Unsafe.defineClass(name, binaryRepresentation, 0, binaryRepresentation.length, classLoader, protectionDomain));
                }
            }
        }
        return result;
    }
}
