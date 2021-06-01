package bench.launcher.options;

import bench.launcher.Option;

public enum Type implements Option {
    CLEARING("Clearing"),
    BLOCKING("Blocking"),
    DISCARDING("Discarding");

    private final String name;

    Type(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
