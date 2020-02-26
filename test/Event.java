package eu.menzani.ringbuffer;

import java.util.function.Supplier;

class Event {
    static final Supplier<Event> RING_BUFFER_FILLER = Event::new;

    private int data;

    private Event() {}

    Event(int data) {
        this.data = data;
    }

    int getData() {
        return data;
    }

    void setData(int data) {
        this.data = data;
    }
}
