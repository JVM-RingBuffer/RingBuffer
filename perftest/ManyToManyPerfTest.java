package test;

class ManyToManyPerfTest extends ManyToManyTest {
    public static void main(String[] args) {
        new ManyToManyPerfTest().run();
    }

    @Override
    long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}