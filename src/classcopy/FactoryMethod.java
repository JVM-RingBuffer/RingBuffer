package eu.menzani.ringbuffer.classcopy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class FactoryMethod<T> implements Invokable<T> {
    private final Method method;

    FactoryMethod(Method method) {
        this.method = method;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T call(Object... arguments) {
        try {
            return (T) method.invoke(arguments);
        } catch (IllegalAccessException e) {
            throw new AssertionError();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
