package eu.menzani.ringbuffer;

import java.util.HashSet;
import java.util.Set;

class ReaderGroup {
    private final Set<Reader> readers = new HashSet<>();

    void add(Reader reader) {
        readers.add(reader);
    }

    int getSum() throws InterruptedException {
        for (Reader reader : readers) {
            reader.join();
        }
        return readers.stream().mapToInt(Reader::getSum).sum();
    }
}
