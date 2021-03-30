package test.dependant;

class PoolObject implements eu.menzani.object.PoolObject {
    @Override
    public void gc() {
        throw new AssertionError();
    }

    @Override
    public void deallocate() {
        throw new AssertionError();
    }
}
