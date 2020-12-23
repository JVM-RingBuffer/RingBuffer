package test;

import eu.menzani.lang.Check;
import eu.menzani.struct.PropertyConfiguration;
import eu.menzani.system.Platform;

import java.nio.file.Path;

public class Config {
    static final int concurrentProducersAndConsumers;

    public static void init() {
    }

    static {
        PropertyConfiguration configuration = new PropertyConfiguration(Path.of("cfg", "test.properties"));
        concurrentProducersAndConsumers = configuration.getInt("concurrent-producers-and-consumers", Platform.getNumberOfCores() / 2, "USE_ALL_CPUS");
        configuration.saveDefault();
        Check.notLesser(concurrentProducersAndConsumers, 2);
    }
}
