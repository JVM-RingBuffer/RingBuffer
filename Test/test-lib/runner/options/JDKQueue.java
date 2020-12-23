package test.runner.options;

import test.runner.Option;

public enum JDKQueue implements Option {
    ARRAY_BLOCKING("ArrayBlocking"),
    LINKED_BLOCKING("LinkedBlocking"),
    LINKED_TRANSFER("LinkedTransfer"),
    LINKED_CONCURRENT("LinkedConcurrent");

    private final String name;

    JDKQueue(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
