package bench.launcher.options;

import bench.launcher.Option;

public enum Group implements Option {
    RING_BUFFER("RingBuffer"),
    AGRONA("Agrona"),
    JCTOOLS("JCTools"),
    JDK("JDK"),
    OTHER("Other");

    private final String name;

    Group(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
