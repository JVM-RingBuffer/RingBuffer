package test.runner.options;

import test.runner.Option;

public enum ThreadSafetyStrategy implements Option {
    LOCK_BASED("Lock-based"),
    LOCK_FREE("Lock-free");

    private final String name;

    ThreadSafetyStrategy(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
