package bench.launcher.options;

import bench.launcher.Option;

public enum Other implements Option {
    STACK("Stack"),
    COMPLEX("Complex"),
    WAIT("Wait"),
    WAIT_TWO_STEP("Wait Two Step");

    private final String name;

    Other(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
