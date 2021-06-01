package bench.launcher.options;

import bench.launcher.Option;

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
