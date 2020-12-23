package test.runner.options;

import test.runner.Option;

public enum Concurrency implements Option {
    CONCURRENT("Concurrent"),
    MANY_READERS("Many Readers"),
    MANY_WRITERS("Many Writers"),
    VOLATILE("Volatile");

    private final String name;

    Concurrency(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
