module test {
    requires org.ringbuffer;
    requires org.agrona.core;
    requires jctools.core;
    requires java.desktop;

    opens test.competitors to eu.menzani;
    opens test.marshalling to eu.menzani;
    opens test.object to eu.menzani;
    opens test.wait to eu.menzani;
}
