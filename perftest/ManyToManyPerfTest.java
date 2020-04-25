package test;

class ManyToManyPerfTest extends ManyToManyTest {
    public static void main(String[] args) {
        new ManyToManyPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
