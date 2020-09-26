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
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copies a class to allow inlining of polymorphic calls.
 *
 * <pre>{@code
 * CopiedClass<Api> copiedClass = CopiedClass.of(Impl.class, MethodHandles.lookup());
 *
 * Invokable<Api> constructor = copiedClass.getConstructor(int.class);
 * Invokable<Api> factory = copiedClass.getFactoryMethod("getInstance", int.class);
 *
 * Api api = invokable.call(5);
 * }</pre>
 * <p>
 * This is not a win-win: as with C++ templates, duplicated code will put more pressure on the CPU caches,
 * so performance should be evaluated for each case.
 *
 * @param <T> a superclass or superinterface used to represent the object
 */
public class CopiedClass<T> {
    private static final ByteBuddy byteBuddy = new ByteBuddy();
    private static final AtomicInteger ids = new AtomicInteger(1);

    private final Class<T> copy;
    private final Class<?> original;

    /**
     * Uses deep reflection to access a class for which a {@link MethodHandles.Lookup} created in the same package
     * is not available.
     *
     * @param original must be concrete
     * @param lookup   must be allowed to do deep reflection on {@code original}
     */
    public static <T> CopiedClass<T> ofExternal(Class<?> original, MethodHandles.Lookup lookup) {
        try {
            return of(original, MethodHandles.privateLookupIn(original, lookup));
        } catch (IllegalAccessException e) {
            throw Lang.uncheck(e);
        }
    }

    /**
     * @param original must be concrete
     * @param lookup   must have package privileges, and it must be able to access {@code original}
     */
    public static <T> CopiedClass<T> of(Class<?> original, MethodHandles.Lookup lookup) {
        if (Modifier.isAbstract(original.getModifiers())) {
            throw new IllegalArgumentException("original must be concrete.");
        }
        if ((lookup.lookupModes() & MethodHandles.Lookup.PACKAGE) == 0) {
            throw new IllegalArgumentException("lookup must have package privileges.");
        }
        try {
            lookup.accessClass(original);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("original cannot be accessed by lookup.");
        }
        return new CopiedClass<>(byteBuddy
                .redefine(original)
                .name(original.getName() + "$Copy" + ids.getAndIncrement())
                .make()
                .load(original.getClassLoader(), ClassLoadingStrategy.UsingLookup.of(lookup))
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
