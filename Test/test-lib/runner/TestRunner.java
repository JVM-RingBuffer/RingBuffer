package test.runner;

import eu.menzani.benchmark.Benchmark;
import eu.menzani.swing.Swing;
import test.Config;
import test.competitors.*;
import test.marshalling.*;
import test.object.*;
import test.runner.options.*;
import test.wait.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class TestRunner {
    public static void main(String[] args) {
        Swing.run(() -> new TestRunner().initialize());
        Config.init();
    }

    private final OptionGroup<Concurrency> concurrency;
    private final OptionGroup<Contention> contention;
    private final OptionGroup<Type> type;
    private final OptionGroup<Granularity> granularity;
    private final OptionGroup<ElementType> elementType;
    private final OptionGroup<ElementSource> elementSource;
    private final OptionGroup<ThreadSafetyStrategy> threadSafetyStrategy;
    private final OptionGroup<Group> group;
    private final OptionGroup<JDKQueue> jdkQueue;
    private final OptionGroup<OtherTest> otherTest;
    private final OptionGroup<Wait> wait;

    private final Button runButton = new Button("Run");
    private final TextArea outputTextArea = new TextArea();
    private final Frame frame = new Frame();

    private TestRunner() {
        OptionGroupFactory factory = new OptionGroupFactory();
        concurrency = factory.create(Concurrency.values());
        contention = factory.create(Contention.values());
        type = factory.create(Type.values());
        granularity = factory.create(Granularity.values());
        elementType = factory.create(ElementType.values());
        elementSource = factory.create(ElementSource.values());
        threadSafetyStrategy = factory.create(ThreadSafetyStrategy.values());
        group = factory.create(Group.values());
        jdkQueue = factory.create(JDKQueue.values());
        otherTest = factory.create(OtherTest.values());
        wait = factory.create(Wait.values());

        Panel panel = new Panel(new GridLayout(factory.getCreatedCount() + 1, 0));
        factory.addTo(panel);
        panel.add(runButton);
        outputTextArea.setEditable(false);
        outputTextArea.setBackground(Color.WHITE);

        frame.setLayout(new GridLayout(2, 0));
        frame.add(panel);
        frame.add(outputTextArea);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        outputTextArea.setFont(outputTextArea.getFont().deriveFont(16F));
        outputTextArea.requestFocus();
    }

    private void initialize() {
        BenchmarkRunner benchmarkRunner = new BenchmarkRunner(this);
        benchmarkRunner.start();

        runButton.addActionListener(e -> {
            runButton.setEnabled(false);
            benchmarkRunner.runTest(getSelectedTestClass());
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                benchmarkRunner.terminate();
                System.exit(0);
            }
        });
    }

    void setOutput(String output) {
        outputTextArea.setText(output);
    }

    void allowRun() {
        runButton.setEnabled(true);
    }

    private Class<? extends Benchmark> getSelectedTestClass() {
        switch (group.getOption()) {
            case RING_BUFFER:
                switch (concurrency.getOption()) {
                    case CONCURRENT:
                        switch (threadSafetyStrategy.getOption()) {
                            case LOCK_BASED:
                                switch (type.getOption()) {
                                    case CLEARING:
                                        switch (elementType.getOption()) {
                                            case OBJECT:
                                                switch (elementSource.getOption()) {
                                                    case EMPTY:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyToManyContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyToManyTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyToManyBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyToManyBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyToManyContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyToManyTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyToManyBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyToManyBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyHeapContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyHeapTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyDirectContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyDirectTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                        }
                                    case BLOCKING:
                                        switch (elementType.getOption()) {
                                            case OBJECT:
                                                switch (elementSource.getOption()) {
                                                    case EMPTY:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyToManyBlockingContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyToManyBlockingTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyToManyBlockingContentionPerfTest.class;
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyToManyBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyToManyBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyToManyBlockingContentionPerfTest.class;
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyToManyBlockingContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyToManyBlockingTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyToManyBlockingContentionPerfTest.class;
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyToManyBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyToManyBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyToManyBlockingContentionPerfTest.class;
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyHeapBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyHeapBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyToManyHeapBlockingContentionPerfTest.class;
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyDirectBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyDirectBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyToManyHeapBlockingContentionPerfTest.class;
                                                }
                                        }
                                    case DISCARDING:
                                }
                            case LOCK_FREE:
                                switch (elementType.getOption()) {
                                    case OBJECT:
                                        switch (elementSource.getOption()) {
                                            case EMPTY:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastManyToManyContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastManyToManyTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastPrefilledManyToManyContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastPrefilledManyToManyTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyToManyHeapContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyToManyHeapTest.class;
                                            case UNBLOCKED_CONTENTION:
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyToManyDirectContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyToManyDirectTest.class;
                                            case UNBLOCKED_CONTENTION:
                                        }
                                }
                        }
                    case MANY_READERS:
                        switch (threadSafetyStrategy.getOption()) {
                            case LOCK_BASED:
                                switch (type.getOption()) {
                                    case CLEARING:
                                        switch (elementType.getOption()) {
                                            case OBJECT:
                                                switch (elementSource.getOption()) {
                                                    case EMPTY:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyReadersContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyReadersTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyReadersBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyReadersBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyReadersContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyReadersTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyReadersBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyReadersBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersHeapContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersHeapTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersDirectContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersDirectTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                        }
                                    case BLOCKING:
                                        switch (elementType.getOption()) {
                                            case OBJECT:
                                                switch (elementSource.getOption()) {
                                                    case EMPTY:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyReadersBlockingContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyReadersBlockingTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyReadersBlockingContentionPerfTest.class;
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyReadersBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyReadersBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyReadersBlockingContentionPerfTest.class;
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyReadersBlockingContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyReadersBlockingTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyReadersBlockingContentionPerfTest.class;
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyReadersBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyReadersBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyReadersBlockingBatchContentionPerfTest.class;
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersHeapBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersHeapBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyReadersHeapBlockingContentionPerfTest.class;
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersDirectBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersDirectBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyReadersDirectBlockingContentionPerfTest.class;
                                                }
                                        }
                                    case DISCARDING:
                                }
                            case LOCK_FREE:
                                switch (elementType.getOption()) {
                                    case OBJECT:
                                        switch (elementSource.getOption()) {
                                            case EMPTY:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastManyReadersContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastManyReadersTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastPrefilledManyReadersContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastPrefilledManyReadersTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyReadersHeapContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyReadersHeapTest.class;
                                            case UNBLOCKED_CONTENTION:
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyReadersDirectContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyReadersDirectTest.class;
                                            case UNBLOCKED_CONTENTION:
                                        }
                                }
                        }
                    case MANY_WRITERS:
                        switch (threadSafetyStrategy.getOption()) {
                            case LOCK_BASED:
                                switch (type.getOption()) {
                                    case CLEARING:
                                        switch (elementType.getOption()) {
                                            case OBJECT:
                                                switch (elementSource.getOption()) {
                                                    case EMPTY:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyWritersContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyWritersTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyWritersBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyWritersBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyWritersContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyWritersTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyWritersBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyWritersBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersHeapContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersHeapTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersDirectContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersDirectTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                        }
                                    case BLOCKING:
                                        switch (elementType.getOption()) {
                                            case OBJECT:
                                                switch (elementSource.getOption()) {
                                                    case EMPTY:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyWritersBlockingContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyWritersBlockingTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyWritersBlockingContentionPerfTest.class;
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyWritersBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyWritersBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyWritersBlockingBatchContentionPerfTest.class;
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyWritersBlockingContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyWritersBlockingTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyWritersBlockingContentionPerfTest.class;
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyWritersBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyWritersBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyWritersBlockingBatchContentionPerfTest.class;
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersHeapBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersHeapBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyWritersHeapBlockingContentionPerfTest.class;
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersDirectBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersDirectBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyWritersDirectBlockingContentionPerfTest.class;
                                                }
                                        }
                                    case DISCARDING:
                                }
                            case LOCK_FREE:
                                switch (elementType.getOption()) {
                                    case OBJECT:
                                        switch (elementSource.getOption()) {
                                            case EMPTY:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastManyWritersContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastManyWritersTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastPrefilledManyWritersContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastPrefilledManyWritersTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyWritersHeapContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyWritersHeapTest.class;
                                            case UNBLOCKED_CONTENTION:
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyWritersDirectContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyWritersDirectTest.class;
                                            case UNBLOCKED_CONTENTION:
                                        }
                                }
                        }
                    case VOLATILE:
                        switch (threadSafetyStrategy.getOption()) {
                            case LOCK_BASED:
                                switch (type.getOption()) {
                                    case CLEARING:
                                        switch (elementType.getOption()) {
                                            case OBJECT:
                                                switch (elementSource.getOption()) {
                                                    case EMPTY:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return OneToOneContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return OneToOneTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return OneToOneBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return OneToOneBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledOneToOneContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledOneToOneTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledOneToOneBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledOneToOneBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneHeapContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneHeapTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneDirectContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneDirectTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                        }
                                    case BLOCKING:
                                        switch (elementType.getOption()) {
                                            case OBJECT:
                                                switch (elementSource.getOption()) {
                                                    case EMPTY:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return OneToOneBlockingContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return OneToOneBlockingTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return OneToOneBlockingContentionPerfTest.class;
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return OneToOneBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return OneToOneBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return OneToOneBlockingBatchContentionPerfTest.class;
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledOneToOneBlockingContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledOneToOneBlockingTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledOneToOneBlockingContentionPerfTest.class;
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledOneToOneBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledOneToOneBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledOneToOneBlockingBatchContentionPerfTest.class;
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneHeapBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneHeapBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return OneToOneHeapBlockingContentionPerfTest.class;
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneDirectBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneDirectBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return OneToOneDirectBlockingContentionPerfTest.class;
                                                }
                                        }
                                    case DISCARDING:
                                }
                            case LOCK_FREE:
                                switch (elementType.getOption()) {
                                    case OBJECT:
                                        switch (elementSource.getOption()) {
                                            case EMPTY:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastOneToOneContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastOneToOneTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastPrefilledOneToOneContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastPrefilledOneToOneTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                }
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastOneToOneHeapContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastOneToOneHeapTest.class;
                                            case UNBLOCKED_CONTENTION:
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastOneToOneDirectContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastOneToOneDirectTest.class;
                                            case UNBLOCKED_CONTENTION:
                                        }
                                }
                        }
                }
            case AGRONA:
                switch (concurrency.getOption()) {
                    case CONCURRENT:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return AgronaManyToManyContentionTest.class;
                            case NO_CONTENTION:
                                return AgronaManyToManyTest.class;
                            case UNBLOCKED_CONTENTION:
                        }
                    case MANY_WRITERS:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return AgronaManyWritersContentionTest.class;
                            case NO_CONTENTION:
                                return AgronaManyWritersTest.class;
                            case UNBLOCKED_CONTENTION:
                        }
                    case VOLATILE:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return AgronaOneToOneContentionTest.class;
                            case NO_CONTENTION:
                                return AgronaOneToOneTest.class;
                            case UNBLOCKED_CONTENTION:
                        }
                }
            case JCTOOLS:
                switch (concurrency.getOption()) {
                    case CONCURRENT:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return JCToolsManyToManyContentionTest.class;
                            case NO_CONTENTION:
                                return JCToolsManyToManyTest.class;
                            case UNBLOCKED_CONTENTION:
                        }
                    case MANY_READERS:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return JCToolsManyReadersContentionTest.class;
                            case NO_CONTENTION:
                                return JCToolsManyReadersTest.class;
                            case UNBLOCKED_CONTENTION:
                        }
                    case MANY_WRITERS:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return JCToolsManyWritersContentionTest.class;
                            case NO_CONTENTION:
                                return JCToolsManyWritersTest.class;
                            case UNBLOCKED_CONTENTION:
                        }
                    case VOLATILE:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return JCToolsOneToOneContentionTest.class;
                            case NO_CONTENTION:
                                return JCToolsOneToOneTest.class;
                            case UNBLOCKED_CONTENTION:
                        }
                }
            case JDK:
                switch (jdkQueue.getOption()) {
                    case ARRAY_BLOCKING:
                        switch (concurrency.getOption()) {
                            case CONCURRENT:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return ArrayManyToManyContentionTest.class;
                                    case NO_CONTENTION:
                                        return ArrayManyToManyTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return ArrayManyReadersContentionTest.class;
                                    case NO_CONTENTION:
                                        return ArrayManyReadersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return ArrayManyWritersContentionTest.class;
                                    case NO_CONTENTION:
                                        return ArrayManyWritersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return ArrayOneToOneContentionTest.class;
                                    case NO_CONTENTION:
                                        return ArrayOneToOneTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                        }
                    case LINKED_BLOCKING:
                        switch (concurrency.getOption()) {
                            case CONCURRENT:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedBlockingManyToManyContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedBlockingManyToManyTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedBlockingManyReadersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedBlockingManyReadersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedBlockingManyWritersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedBlockingManyWritersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedBlockingOneToOneContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedBlockingOneToOneTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                        }
                    case LINKED_TRANSFER:
                        switch (concurrency.getOption()) {
                            case CONCURRENT:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedTransferManyToManyContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedTransferManyToManyTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedTransferManyReadersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedTransferManyReadersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedTransferManyWritersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedTransferManyWritersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedTransferOneToOneContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedTransferOneToOneTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                        }
                    case LINKED_CONCURRENT:
                        switch (concurrency.getOption()) {
                            case CONCURRENT:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedConcurrentManyToManyContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedConcurrentManyToManyTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedConcurrentManyReadersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedConcurrentManyReadersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedConcurrentManyWritersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedConcurrentManyWritersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedConcurrentOneToOneContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedConcurrentOneToOneTest.class;
                                    case UNBLOCKED_CONTENTION:
                                }
                        }
                }
            case OTHER:
                switch (otherTest.getOption()) {
                    case STACK:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return ConcurrentStackContentionTest.class;
                            case NO_CONTENTION:
                                return ConcurrentStackTest.class;
                            case UNBLOCKED_CONTENTION:
                        }
                    case COMPLEX:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return ProducersToProcessorToConsumersContentionTest.class;
                            case NO_CONTENTION:
                                return ProducersToProcessorToConsumersTest.class;
                            case UNBLOCKED_CONTENTION:
                        }
                    case WAIT:
                        switch (wait.getOption()) {
                            case ARRAY:
                                return ArrayMultiStepTest.class;
                            case LINKED:
                                return LinkedMultiStepTest.class;
                            case MANUAL:
                                return ManualMultiStepTest.class;
                        }
                    case WAIT_TWO_STEP:
                        switch (wait.getOption()) {
                            case ARRAY:
                                return TwoStepArrayMultiStepTest.class;
                            case LINKED:
                                return TwoStepLinkedMultiStepTest.class;
                            case MANUAL:
                                return TwoStepManualMultiStepTest.class;
                        }
                }
        }
        throw new AssertionError();
    }
}
