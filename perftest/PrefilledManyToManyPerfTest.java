package test;

class PrefilledManyToManyPerfTest extends PrefilledManyToManyTest {
    public static void main(String[] args) {
        new PrefilledManyToManyPerfTest().run();
    }

    @Override
    long testSum() {
        PrefilledWriter.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
