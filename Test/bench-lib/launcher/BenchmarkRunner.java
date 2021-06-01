package bench.launcher;

import bench.Config;
import bench.competitors.*;
import bench.launcher.options.*;
import bench.marshalling.*;
import bench.object.*;
import bench.wait.*;
import eu.menzani.benchmark.Benchmark;
import eu.menzani.swing.DisableFocus;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class BenchmarkRunner {
    public static void main(String[] args) {
        new BenchmarkRunner().initialize();
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
    private final OptionGroup<Other> other;
    private final OptionGroup<Wait> wait;

    private final Button runButton = new Button("Run");
    private final TextArea outputTextArea = new TextArea();
    private final Frame frame = new Frame();

    private BenchmarkRunner() {
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
        other = factory.create(Other.values());
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

        DisableFocus disableFocus = new DisableFocus();
        disableFocus.exclude(outputTextArea);
        disableFocus.apply(frame);

        outputTextArea.setFont(outputTextArea.getFont().deriveFont(16F));
    }

    private void initialize() {
        BenchmarkLauncher benchmarkLauncher = new BenchmarkLauncher(this);
        benchmarkLauncher.start();

        runButton.addActionListener(e -> {
            runButton.setEnabled(false);

            Class<? extends Benchmark> benchmarkClass = getSelectedBenchmarkClass();
            benchmarkLauncher.launchBenchmark(benchmarkClass);
            frame.setTitle(benchmarkClass.getSimpleName());
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                benchmarkLauncher.terminate();
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

    private Class<? extends Benchmark> getSelectedBenchmarkClass() {
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
                                                                        return ManyToManyContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyToManyBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyToManyBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyToManyBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyToManyContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyToManyBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyToManyBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyToManyBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyHeapContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyHeapBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyDirectContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyDirectBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
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
                                                                        return ManyToManyBlockingContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyToManyBlockingBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyToManyBlockingContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyToManyBlockingBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyToManyBlockingBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyToManyBlockingContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyToManyBlockingContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyToManyBlockingBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyToManyBlockingContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyToManyBlockingBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyToManyBlockingBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyToManyBlockingContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyHeapBlockingContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyHeapBlockingBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyToManyHeapBlockingContentionPerfBenchmark.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyDirectBlockingContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyDirectBlockingBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyToManyHeapBlockingContentionPerfBenchmark.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                        }
                                    case DISCARDING:
                                    default:
                                        throw new AssertionError();
                                }
                            case LOCK_FREE:
                                switch (elementType.getOption()) {
                                    case OBJECT:
                                        switch (elementSource.getOption()) {
                                            case EMPTY:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return LockfreeManyToManyContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return LockfreeManyToManyBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return LockfreePrefilledManyToManyContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return LockfreePrefilledManyToManyBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return LockfreeManyToManyHeapContentionBenchmark.class;
                                            case NO_CONTENTION:
                                                return LockfreeManyToManyHeapBenchmark.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return LockfreeManyToManyDirectContentionBenchmark.class;
                                            case NO_CONTENTION:
                                                return LockfreeManyToManyDirectBenchmark.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
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
                                                                        return ManyReadersContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyReadersBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyReadersBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyReadersBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyReadersContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyReadersBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyReadersBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyReadersBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersHeapContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersHeapBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersDirectContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersDirectBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
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
                                                                        return ManyReadersBlockingContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyReadersBlockingBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyReadersBlockingContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyReadersBlockingBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyReadersBlockingBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyReadersBlockingContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyReadersBlockingContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyReadersBlockingBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyReadersBlockingContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyReadersBlockingBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyReadersBlockingBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyReadersBlockingBatchContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersHeapBlockingContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersHeapBlockingBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyReadersHeapBlockingContentionPerfBenchmark.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersDirectBlockingContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersDirectBlockingBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyReadersDirectBlockingContentionPerfBenchmark.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                        }
                                    case DISCARDING:
                                    default:
                                        throw new AssertionError();
                                }
                            case LOCK_FREE:
                                switch (elementType.getOption()) {
                                    case OBJECT:
                                        switch (elementSource.getOption()) {
                                            case EMPTY:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return LockfreeManyReadersContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return LockfreeManyReadersBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return LockfreePrefilledManyReadersContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return LockfreePrefilledManyReadersBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return LockfreeManyReadersHeapContentionBenchmark.class;
                                            case NO_CONTENTION:
                                                return LockfreeManyReadersHeapBenchmark.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return LockfreeManyReadersDirectContentionBenchmark.class;
                                            case NO_CONTENTION:
                                                return LockfreeManyReadersDirectBenchmark.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
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
                                                                        return ManyWritersContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyWritersBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyWritersBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyWritersBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyWritersContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyWritersBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyWritersBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyWritersBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersHeapContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersHeapBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersDirectContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersDirectBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
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
                                                                        return ManyWritersBlockingContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyWritersBlockingBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyWritersBlockingContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyWritersBlockingBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyWritersBlockingBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyWritersBlockingBatchContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyWritersBlockingContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyWritersBlockingBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyWritersBlockingContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyWritersBlockingBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyWritersBlockingBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyWritersBlockingBatchContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersHeapBlockingContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersHeapBlockingBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyWritersHeapBlockingContentionPerfBenchmark.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersDirectBlockingContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersDirectBlockingBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyWritersDirectBlockingContentionPerfBenchmark.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                        }
                                    case DISCARDING:
                                    default:
                                        throw new AssertionError();
                                }
                            case LOCK_FREE:
                                switch (elementType.getOption()) {
                                    case OBJECT:
                                        switch (elementSource.getOption()) {
                                            case EMPTY:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return LockfreeManyWritersContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return LockfreeManyWritersBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return LockfreePrefilledManyWritersContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return LockfreePrefilledManyWritersBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return LockfreeManyWritersHeapContentionBenchmark.class;
                                            case NO_CONTENTION:
                                                return LockfreeManyWritersHeapBenchmark.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return LockfreeManyWritersDirectContentionBenchmark.class;
                                            case NO_CONTENTION:
                                                return LockfreeManyWritersDirectBenchmark.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
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
                                                                        return OneToOneContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return OneToOneBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return OneToOneBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return OneToOneBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledOneToOneContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledOneToOneBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledOneToOneBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledOneToOneBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneHeapContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneHeapBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneDirectContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneDirectBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
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
                                                                        return OneToOneBlockingContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return OneToOneBlockingBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return OneToOneBlockingContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return OneToOneBlockingBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return OneToOneBlockingBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return OneToOneBlockingBatchContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                    case PREFILLED:
                                                        switch (granularity.getOption()) {
                                                            case NO_BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledOneToOneBlockingContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledOneToOneBlockingBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledOneToOneBlockingContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledOneToOneBlockingBatchContentionBenchmark.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledOneToOneBlockingBatchBenchmark.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledOneToOneBlockingBatchContentionPerfBenchmark.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                        }
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneHeapBlockingContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneHeapBlockingBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return OneToOneHeapBlockingContentionPerfBenchmark.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneDirectBlockingContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneDirectBlockingBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return OneToOneDirectBlockingContentionPerfBenchmark.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                        }
                                    case DISCARDING:
                                    default:
                                        throw new AssertionError();
                                }
                            case LOCK_FREE:
                                switch (elementType.getOption()) {
                                    case OBJECT:
                                        switch (elementSource.getOption()) {
                                            case EMPTY:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return LockfreeOneToOneContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return LockfreeOneToOneBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return LockfreePrefilledOneToOneContentionBenchmark.class;
                                                    case NO_CONTENTION:
                                                        return LockfreePrefilledOneToOneBenchmark.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return LockfreeOneToOneHeapContentionBenchmark.class;
                                            case NO_CONTENTION:
                                                return LockfreeOneToOneHeapBenchmark.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return LockfreeOneToOneDirectContentionBenchmark.class;
                                            case NO_CONTENTION:
                                                return LockfreeOneToOneDirectBenchmark.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                }
                        }
                }
            case AGRONA:
                switch (concurrency.getOption()) {
                    case CONCURRENT:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return AgronaManyToManyContentionBenchmark.class;
                            case NO_CONTENTION:
                                return AgronaManyToManyBenchmark.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    case MANY_WRITERS:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return AgronaManyWritersContentionBenchmark.class;
                            case NO_CONTENTION:
                                return AgronaManyWritersBenchmark.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    case VOLATILE:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return AgronaOneToOneContentionBenchmark.class;
                            case NO_CONTENTION:
                                return AgronaOneToOneBenchmark.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    default:
                        throw new AssertionError();
                }
            case JCTOOLS:
                switch (concurrency.getOption()) {
                    case CONCURRENT:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return JCToolsManyToManyContentionBenchmark.class;
                            case NO_CONTENTION:
                                return JCToolsManyToManyBenchmark.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    case MANY_READERS:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return JCToolsManyReadersContentionBenchmark.class;
                            case NO_CONTENTION:
                                return JCToolsManyReadersBenchmark.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    case MANY_WRITERS:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return JCToolsManyWritersContentionBenchmark.class;
                            case NO_CONTENTION:
                                return JCToolsManyWritersBenchmark.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    case VOLATILE:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return JCToolsOneToOneContentionBenchmark.class;
                            case NO_CONTENTION:
                                return JCToolsOneToOneBenchmark.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                }
            case JDK:
                switch (jdkQueue.getOption()) {
                    case ARRAY_BLOCKING:
                        switch (concurrency.getOption()) {
                            case CONCURRENT:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return ArrayManyToManyContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return ArrayManyToManyBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return ArrayManyReadersContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return ArrayManyReadersBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return ArrayManyWritersContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return ArrayManyWritersBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return ArrayOneToOneContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return ArrayOneToOneBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                        }
                    case LINKED_BLOCKING:
                        switch (concurrency.getOption()) {
                            case CONCURRENT:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedBlockingManyToManyContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedBlockingManyToManyBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedBlockingManyReadersContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedBlockingManyReadersBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedBlockingManyWritersContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedBlockingManyWritersBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedBlockingOneToOneContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedBlockingOneToOneBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                        }
                    case LINKED_TRANSFER:
                        switch (concurrency.getOption()) {
                            case CONCURRENT:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedTransferManyToManyContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedTransferManyToManyBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedTransferManyReadersContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedTransferManyReadersBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedTransferManyWritersContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedTransferManyWritersBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedTransferOneToOneContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedTransferOneToOneBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                        }
                    case LINKED_CONCURRENT:
                        switch (concurrency.getOption()) {
                            case CONCURRENT:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedConcurrentManyToManyContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedConcurrentManyToManyBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedConcurrentManyReadersContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedConcurrentManyReadersBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedConcurrentManyWritersContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedConcurrentManyWritersBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedConcurrentOneToOneContentionBenchmark.class;
                                    case NO_CONTENTION:
                                        return LinkedConcurrentOneToOneBenchmark.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                        }
                }
            case OTHER:
                switch (other.getOption()) {
                    case STACK:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return StackContentionBenchmark.class;
                            case NO_CONTENTION:
                                return StackBenchmark.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    case COMPLEX:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return ProducersToProcessorToConsumersContentionBenchmark.class;
                            case NO_CONTENTION:
                                return ProducersToProcessorToConsumersBenchmark.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    case WAIT:
                        switch (wait.getOption()) {
                            case ARRAY:
                                return ArrayMultiStepBenchmark.class;
                            case LINKED:
                                return LinkedMultiStepBenchmark.class;
                            case MANUAL:
                                return ManualMultiStepBenchmark.class;
                            default:
                                throw new AssertionError();
                        }
                    case WAIT_TWO_STEP:
                        switch (wait.getOption()) {
                            case ARRAY:
                                return TwoStepArrayMultiStepBenchmark.class;
                            case LINKED:
                                return TwoStepLinkedMultiStepBenchmark.class;
                            case MANUAL:
                                return TwoStepManualMultiStepBenchmark.class;
                            default:
                                throw new AssertionError();
                        }
                }
            default:
                throw new AssertionError();
        }
    }
}
