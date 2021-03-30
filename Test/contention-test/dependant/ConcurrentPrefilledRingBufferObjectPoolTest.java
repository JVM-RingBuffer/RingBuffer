package test.dependant;

import eu.menzani.benchmark.Benchmark;
import eu.menzani.benchmark.ResultFormat;
import eu.menzani.lang.Numbers;
import eu.menzani.object.ObjectPool;
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

    private static final ObjectPool<PoolObject> objectPool =
            new ConcurrentPrefilledRingBufferObjectPool<>(Numbers.getNextPowerOfTwo(1_000_000), PoolObject::new);

    @Override
    protected void test(int i) {
        for (; i > 0; i--) {
            objectPool.acquire(objectPool.release());
        }
    }
}
