package test.runner.options;

import test.runner.Option;

public enum ElementType implements Option {
    OBJECT("Object"),
    HEAP("Heap"),
    DIRECT("Direct");

    private final String name;

    ElementType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
