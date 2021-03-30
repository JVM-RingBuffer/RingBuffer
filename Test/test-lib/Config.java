package test;

import eu.menzani.lang.Check;
import eu.menzani.struct.PropertyConfiguration;
import eu.menzani.system.Platform;

import java.nio.file.Path;

public class Config {
    static final int concurrentProducersAndConsumers;
    public static final int ConcurrentPrefilledRingBufferObjectPoolTest_concurrency;

    public static void init() {
    }

    static {
        PropertyConfiguration configuration = new PropertyConfiguration(Path.of("cfg", "test.properties"));
        int numberOfCores = Platform.getNumberOfCores();
        final String useAllCPUs = "USE_ALL_CPUS";
        concurrentProducersAndConsumers = configuration.getInt("concurrent-producers-and-consumers", numberOfCores / 2, useAllCPUs);
        ConcurrentPrefilledRingBufferObjectPoolTest_concurrency = configuration.getInt("ConcurrentPrefilledRingBufferObjectPoolTest-concurrency", numberOfCores, useAllCPUs);
        configuration.saveDefault();
        Check.notLesser(concurrentProducersAndConsumers, 2);
        Check.notLesser(ConcurrentPrefilledRingBufferObjectPoolTest_concurrency, 1);
    }
}
