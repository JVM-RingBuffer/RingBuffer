package eu.menzani.ringbuffer.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class AtomicBoolean {
    private static final VarHandle VALUE;

    static {
        try {
            VALUE = MethodHandles.lookup().findVarHandle(AtomicBoolean.class, "value", boolean.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private boolean value;

    public AtomicBoolean() {}

    public AtomicBoolean(boolean value) {
        this.value = value;
    }

    public void setPlain(boolean value) {
        this.value = value;
    }

    public void setOpaque(boolean value) {
        VALUE.setOpaque(this, value);
    }

    public void setRelease(boolean value) {
        VALUE.setRelease(this, value);
    }

    public void setVolatile(boolean value) {
        VALUE.setVolatile(this, value);
    }

    public boolean getPlain() {
        return value;
    }

    public boolean getOpaque() {
        return (boolean) VALUE.getOpaque(this);
    }

    public boolean getAcquire() {
        return (boolean) VALUE.getAcquire(this);
    }

    public boolean getVolatile() {
        return (boolean) VALUE.getVolatile(this);
    }

    public boolean compareAndSetVolatile(boolean oldValue, boolean newValue) {
        return VALUE.compareAndSet(this, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(boolean oldValue, boolean newValue) {
        return VALUE.weakCompareAndSetPlain(this, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(boolean oldValue, boolean newValue) {
        return VALUE.weakCompareAndSetRelease(this, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(boolean oldValue, boolean newValue) {
        return VALUE.weakCompareAndSetAcquire(this, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(boolean oldValue, boolean newValue) {
        return VALUE.weakCompareAndSet(this, oldValue, newValue);
    }

    public boolean getPlainAndSetRelease(boolean value) {
        return (boolean) VALUE.getAndSetRelease(this, value);
    }

    public boolean getAcquireAndSetPlain(boolean value) {
        return (boolean) VALUE.getAndSetAcquire(this, value);
    }

    public boolean getAndSetVolatile(boolean value) {
        return (boolean) VALUE.getAndSet(this, value);
    }

    public boolean comparePlainAndExchangeRelease(boolean oldValue, boolean newValue) {
        return (boolean) VALUE.compareAndExchangeRelease(this, oldValue, newValue);
    }

    public boolean compareAcquireAndExchangePlain(boolean oldValue, boolean newValue) {
        return (boolean) VALUE.compareAndExchangeAcquire(this, oldValue, newValue);
    }

    public boolean compareAndExchangeVolatile(boolean oldValue, boolean newValue) {
        return (boolean) VALUE.compareAndExchange(this, oldValue, newValue);
    }

    public boolean getPlainAndBitwiseAndRelease(boolean mask) {
        return (boolean) VALUE.getAndBitwiseAndRelease(this, mask);
    }

    public boolean getAcquireAndBitwiseAndPlain(boolean mask) {
        return (boolean) VALUE.getAndBitwiseAndAcquire(this, mask);
    }

    public boolean getAndBitwiseAndVolatile(boolean mask) {
        return (boolean) VALUE.getAndBitwiseAnd(this, mask);
    }

    public boolean getPlainAndBitwiseOrRelease(boolean mask) {
        return (boolean) VALUE.getAndBitwiseOrRelease(this, mask);
    }

    public boolean getAcquireAndBitwiseOrPlain(boolean mask) {
        return (boolean) VALUE.getAndBitwiseOrAcquire(this, mask);
    }

    public boolean getAndBitwiseOrVolatile(boolean mask) {
        return (boolean) VALUE.getAndBitwiseOr(this, mask);
    }

    public boolean getPlainAndBitwiseXorRelease(boolean mask) {
        return (boolean) VALUE.getAndBitwiseXorRelease(this, mask);
    }

    public boolean getAcquireAndBitwiseXorPlain(boolean mask) {
        return (boolean) VALUE.getAndBitwiseXorAcquire(this, mask);
    }

    public boolean getAndBitwiseXorVolatile(boolean mask) {
        return (boolean) VALUE.getAndBitwiseXor(this, mask);
    }

    @Override
    public String toString() {
        return Boolean.toString(getVolatile());
    }
}
