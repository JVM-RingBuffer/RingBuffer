package test.runner.options;

import test.runner.Option;

public enum Wait implements Option {
    ARRAY("Array"),
    LINKED("Linked"),
    MANUAL("Manual");

    private final String name;

    Wait(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
