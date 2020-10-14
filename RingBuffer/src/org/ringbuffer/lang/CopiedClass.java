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

import net.bytebuddy.ByteBuddy;
import org.ringbuffer.util.ConcurrentKeyedCounter;
import org.ringbuffer.util.KeyedCounter;

import java.lang.reflect.Modifier;

/**
 * Copies a class to allow inlining of polymorphic calls.
 *
 * <pre>{@code
 * CopiedClass<Api> copiedClass = CopiedClass.of(Impl.class);
 *
 * Invokable<Api> invokable = copiedClass.getConstructor(int.class);
 * Invokable<Api> invokable = copiedClass.getFactoryMethod("getInstance", int.class);
 *
 * Api api = invokable.call(5);
 * }</pre>
 *
 * @param <T> a superclass or superinterface used to represent the object
 */
public class CopiedClass<T> {
    private static final ByteBuddy byteBuddy = new ByteBuddy();
    private static final KeyedCounter<Class<?>> ids = new ConcurrentKeyedCounter<>();

    private final Class<T> copy;
    private final Class<?> original;

    public static <T> CopiedClass<T> of(Class<?> original) {
        if (Modifier.isAbstract(original.getModifiers())) {
            throw new IllegalArgumentException("original must be concrete.");
        }
        return new CopiedClass<>(byteBuddy
                .redefine(original)
                .name(original.getName() + "$Copy" + ids.increment(original))
                .make()
                .load(original.getClassLoader(), ByteBuddyClassLoadingStrategy.INHERIT_PROTECTION_DOMAIN)
                .getLoaded(), original);
    }

    @SuppressWarnings("unchecked")
    private CopiedClass(Class<?> copy, Class<?> original) {
        Assert.notEqual(copy, original);
        this.copy = (Class<T>) copy;
        this.original = original;
    }

    public Class<T> getCopy() {
        return copy;
    }

    public Class<?> getOriginal() {
        return original;
    }

    public Invokable<T> getConstructor(Class<?>... parameterTypes) {
        Invokable<T> constructor = Invokable.ofConstructor(copy, parameterTypes);
        constructor.ensureAccessible();
        return constructor;
    }

    /**
     * The method must be static, and it must return a subtype of the original class.
     */
    public Invokable<T> getFactoryMethod(String name, Class<?>... parameterTypes) {
        Method<T> method = Invokable.ofMethod(copy, name, parameterTypes);
        if (!method.isStatic()) {
            throw new IllegalArgumentException("Method must be static.");
        }
        Class<?> returnType = method.getExecutable().getReturnType();
        if (returnType != copy && !original.isAssignableFrom(returnType)) {
            throw new IllegalArgumentException("Method must be a factory.");
        }
        method.ensureAccessible();
        return method;
    }
}
