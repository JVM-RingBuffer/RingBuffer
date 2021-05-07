package test.dependant;

import eu.menzani.benchmark.Benchmark;
import eu.menzani.benchmark.ResultFormat;
import eu.menzani.lang.Numbers;
import eu.menzani.object.ConcurrentObjectPoolThreadLocalCache;
import eu.menzani.object.ObjectPool;
import eu.menzani.object.PrefilledObjectPool;
import org.ringbuffer.dependant.ConcurrentPrefilledRingBufferObjectPool;
import test.Config;

public class ConcurrentPrefilledRingBufferObjectPoolTest extends Benchmark {
    public static void main(String[] args) {
        new ConcurrentPrefilledRingBufferObjectPoolTest().launchBenchmark();
    }

    @Override
    protected int getConcurrency() {
        return Config.ConcurrentPrefilledRingBufferObjectPoolTest_concurrency;
    }

    @Override
    protected ResultFormat getAutoProfileResultFormat() {
        return ResultFormat.THROUGHPUT;
    }

    private ObjectPool<PoolObject> objectPool;

    @Override
    protected void init() {
        objectPool = new ConcurrentObjectPoolThreadLocalCache<>(
                () -> new PrefilledObjectPool<>(64, PoolObject.FILLER),
                new ConcurrentPrefilledRingBufferObjectPool<>(Numbers.getNextPowerOfTwo(1_000_000), PoolObject.FILLER));
    }

    @Override
    protected void measure(int i) {
        var objectPool = this.objectPool;
        for (; i > 0; i--) {
            PoolObject one = objectPool.release();
            PoolObject two = objectPool.release();
            PoolObject three = objectPool.release();
            PoolObject four = objectPool.release();
            PoolObject five = objectPool.release();
            objectPool.acquire(objectPool.release());
            objectPool.acquire(one);
            objectPool.acquire(two);
            objectPool.acquire(three);
            objectPool.acquire(four);
            objectPool.acquire(five);
        }
    }
}
