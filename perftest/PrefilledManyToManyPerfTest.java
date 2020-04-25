package test;

class PrefilledManyToManyPerfTest extends PrefilledManyToManyTest {
    public static void main(String[] args) {
        new PrefilledManyToManyPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledSynchronizedWriter.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
