package test.runner.options;

import test.runner.Option;

public enum Contention implements Option {
    CONTENTION("Contention"),
    NO_CONTENTION("No Contention"),
    UNBLOCKED_CONTENTION("Unblocked Contention");

    private final String name;

    Contention(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
