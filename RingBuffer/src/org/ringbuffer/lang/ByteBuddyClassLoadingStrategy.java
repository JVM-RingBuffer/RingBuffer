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

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.security.ProtectionDomain;
import java.util.Map;

public class ByteBuddyClassLoadingStrategy implements ClassLoadingStrategy<ClassLoader> {
    public static final ByteBuddyClassLoadingStrategy INHERIT_PROTECTION_DOMAIN = new ByteBuddyClassLoadingStrategy(null);

    private final ProtectionDomain protectionDomain;

    public ByteBuddyClassLoadingStrategy(ProtectionDomain protectionDomain) {
        this.protectionDomain = protectionDomain;
    }

    @Override
    public Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types) {
        return new ByteBuddyClassInjector(classLoader, protectionDomain).inject(types);
    }
}
