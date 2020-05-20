package eu.menzani.ringbuffer.classcopy;

import java.lang.reflect.InvocationTargetException;

class Constructor<T> implements Invokable<T> {
    private final java.lang.reflect.Constructor<T> constructor;

    Constructor(java.lang.reflect.Constructor<T> constructor) {
        this.constructor = constructor;
    }

    @Override
    public T call(Object... arguments) {
        try {
            return constructor.newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new AssertionError();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
