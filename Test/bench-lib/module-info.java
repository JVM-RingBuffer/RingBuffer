module bench {
    requires org.ringbuffer;
    requires org.agrona.core;
    requires org.jctools.core;
    requires java.desktop;

    opens bench.competitors to eu.menzani;
    opens bench.marshalling to eu.menzani;
    opens bench.object to eu.menzani;
    opens bench.wait to eu.menzani;
}
