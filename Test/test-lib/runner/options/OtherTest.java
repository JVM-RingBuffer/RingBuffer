package test.runner.options;

import test.runner.Option;

public enum OtherTest implements Option {
    STACK("Stack"),
    COMPLEX("Complex"),
    WAIT("Wait"),
    WAIT_TWO_STEP("Wait Two Step");

    private final String name;

    OtherTest(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
