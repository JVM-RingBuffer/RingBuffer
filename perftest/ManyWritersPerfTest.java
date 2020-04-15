package test;

class ManyWritersPerfTest extends ManyWritersTest {
    public static void main(String[] args) {
        new ManyWritersPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
