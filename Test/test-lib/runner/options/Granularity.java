package test.runner.options;

import test.runner.Option;

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
