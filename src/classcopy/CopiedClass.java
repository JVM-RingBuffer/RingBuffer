package eu.menzani.ringbuffer.classcopy;

import eu.menzani.ringbuffer.concurrent.AtomicInt;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Copies a class to allow inlining of polymorphic calls.
 *
 * <pre>{@code
 * CopiedClass<Api> copiedClass = CopiedClass.of(Impl.class, MethodHandles.lookup());
 * Invokable<Api> constructor = copiedClass.getConstructor(int.class);
 * Api api = constructor.call(5);
 * api.method();
 * }</pre>
 *
 * @param <T> a superclass or superinterface used to represent the object
 */
public class CopiedClass<T> {
    private static final ByteBuddy byteBuddy = new ByteBuddy(ClassFileVersion.JAVA_V11);
    private static final AtomicInt ids = new AtomicInt(1);

    private final Class<T> clazz;

    /**
     * To be used for classes outside the package of the class performing the copy.
     */
    public CopiedClass(Class<?> clazz) {
        this(clazz, getLookup(clazz));
    }

    private static MethodHandles.Lookup getLookup(Class<?> clazz) {
        try {
            return MethodHandles.privateLookupIn(clazz, MethodHandles.lookup());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public CopiedClass(Class<?> clazz, MethodHandles.Lookup lookup) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalArgumentException("Class must be concrete.");
        }
        if ((lookup.lookupModes() & MethodHandles.Lookup.PACKAGE) == 0) {
            throw new IllegalArgumentException("lookup must have package privileges.");
        }
        Class<?> copy = byteBuddy
                .redefine(clazz)
                .name(clazz.getCanonicalName() + "$Copy" + ids.getAndIncrementVolatile())
                .make()
                .load(clazz.getClassLoader(), ClassLoadingStrategy.UsingLookup.of(lookup))
                .getLoaded();
        assert !copy.equals(clazz);
        this.clazz = cast(copy);
    }

    @SuppressWarnings("unchecked")
    private Class<T> cast(Class<?> clazz) {
        return (Class<T>) clazz;
    }

    public Invokable<T> getConstructor(Class<?>... parameterTypes) {
        java.lang.reflect.Constructor<T> constructor;
        try {
            constructor = clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
        if (!constructor.canAccess(null)) {
            throw new IllegalArgumentException("Constructor must be accessible.");
        }
        return new Constructor<>(constructor);
    }

    public Invokable<T> getFactoryMethod(String name, Class<?>... parameterTypes) {
        Method method;
        try {
            method = clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException("Method must be static.");
        }
        if (!method.canAccess(null)) {
            throw new IllegalArgumentException("Method must be accessible.");
        }
        if (!method.getReturnType().isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Method must be a factory.");
        }
        return new FactoryMethod<>(method);
    }
}
