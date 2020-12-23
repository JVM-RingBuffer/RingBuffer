package test.runner.options;

import test.runner.Option;

public enum ElementSource implements Option {
    EMPTY("Empty"),
    PREFILLED("Pre-filled");

    private final String name;

    ElementSource(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
