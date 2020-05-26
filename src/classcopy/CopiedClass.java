package eu.menzani.ringbuffer.classcopy;

import eu.menzani.ringbuffer.concurrent.AtomicInt;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
 *
 * @param <T> a superclass or superinterface used to represent the object
 */
public class CopiedClass<T> {
    private static final ByteBuddy byteBuddy = new ByteBuddy();
    private static final AtomicInt ids = new AtomicInt(1);

    private final Class<T> copy;
    private final Class<?> original;

    /**
     * Uses deep reflection.
     *
     * @param original must be concrete
     */
    public static <T> CopiedClass<T> of(Class<?> original) {
        try {
            return of(original, MethodHandles.privateLookupIn(original, MethodHandles.lookup()));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param original must be concrete
     * @param lookup   must have package privileges
     */
    public static <T> CopiedClass<T> of(Class<?> original, MethodHandles.Lookup lookup) {
        if (Modifier.isAbstract(original.getModifiers())) {
            throw new IllegalArgumentException("Class must be concrete.");
        }
        if ((lookup.lookupModes() & MethodHandles.Lookup.PACKAGE) == 0) {
            throw new IllegalArgumentException("lookup must have package privileges.");
        }
        return new CopiedClass<>(byteBuddy
                .redefine(original)
                .name(original.getCanonicalName() + "$Copy" + ids.getAndIncrementVolatile())
                .make()
                .load(original.getClassLoader(), ClassLoadingStrategy.UsingLookup.of(lookup))
                .getLoaded(), original);
    }

    @SuppressWarnings("unchecked")
    private CopiedClass(Class<?> copy, Class<?> original) {
        assert !copy.equals(original);
        this.copy = (Class<T>) copy;
        this.original = original;
    }

    public Class<T> getCopy() {
        return copy;
    }

    public Class<?> getOriginal() {
        return original;
    }

    /**
     * The constructor must be accessible.
     */
    public Invokable<T> getConstructor(Class<?>... parameterTypes) {
        java.lang.reflect.Constructor<T> constructor;
        try {
            constructor = copy.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
        if (!constructor.canAccess(null)) {
            throw new IllegalArgumentException("Constructor must be accessible.");
        }
        return new Constructor<>(constructor);
    }

    /**
     * The method must be accessible, it must be static, and it must return a subtype of the original class.
     */
    public Invokable<T> getFactoryMethod(String name, Class<?>... parameterTypes) {
        Method method;
        try {
            method = copy.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException("Method must be static.");
        }
        if (!method.canAccess(null)) {
            throw new IllegalArgumentException("Method must be accessible.");
        }
        Class<?> returnType = method.getReturnType();
        if (returnType != copy && !original.isAssignableFrom(returnType)) {
            throw new IllegalArgumentException("Method must be a factory.");
        }
        return new FactoryMethod<>(method);
    }
}
