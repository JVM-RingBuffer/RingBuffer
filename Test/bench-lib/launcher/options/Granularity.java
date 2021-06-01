package bench.launcher.options;

import bench.launcher.Option;

public enum Granularity implements Option {
    NO_BATCH("No Batch"),
    BATCH("Batch");

    private final String name;

    Granularity(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
