package test.dependant;

import eu.menzani.object.ObjectFactory;

class PoolObject implements eu.menzani.object.PoolObject {
    static final ObjectFactory<PoolObject> FILLER = PoolObject::new;

    @Override
    public void gc() {
        throw new AssertionError();
    }

    @Override
    public void deallocate() {
        throw new AssertionError();
    }
}
