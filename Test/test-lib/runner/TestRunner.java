/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.runner;

import test.AbstractRingBufferTest;
import test.competitors.*;
import test.marshalling.*;
import test.object.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestRunner {
    public static void main(String[] args) {
        new TestRunner().initialize();
    }

    private final OptionGroup<Concurrency> concurrency;
    private final OptionGroup<Contention> contention;
    private final OptionGroup<Type> type;
    private final OptionGroup<Granularity> granularity;
    private final OptionGroup<ElementType> elementType;
    private final OptionGroup<ElementSource> elementSource;
    private final OptionGroup<ThreadSafetyStrategy> threadSafetyStrategy;
    private final OptionGroup<Library> library;
    private final OptionGroup<JDKQueue> jdkQueue;

    private final Button runButton = new Button("Run");
    private final TextArea outputTextArea = new TextArea();
    private final Frame frame = new Frame();

    TestRunner() {
        Panel panel = new Panel(new GridLayout(10, 0));
        OptionGroupFactory factory = new OptionGroupFactory(panel);
        concurrency = factory.create(Concurrency.values());
        contention = factory.create(Contention.values());
        type = factory.create(Type.values());
        granularity = factory.create(Granularity.values());
        elementType = factory.create(ElementType.values());
        elementSource = factory.create(ElementSource.values());
        threadSafetyStrategy = factory.create(ThreadSafetyStrategy.values());
        library = factory.create(Library.values());
        jdkQueue = factory.create(JDKQueue.values());

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
    }

    void initialize() {
        ProcessStarter processStarter = new ProcessStarter(this);
        processStarter.start();

        runButton.addActionListener(e -> {
            runButton.setEnabled(false);
            processStarter.runTest(getSelectedTestClass());
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                processStarter.terminate();
                frame.dispose();
            }
        });
    }

    void setOutput(String output) {
        outputTextArea.setText(output);
        runButton.setEnabled(true);
    }

    private Class<? extends AbstractRingBufferTest> getSelectedTestClass() {
        switch (library.getOption()) {
            case RING_BUFFER:
                switch (concurrency.getOption()) {
                    case CONCURRENT:
                        switch (threadSafetyStrategy.getOption()) {
                            case LOCK_BASED:
                                switch (type.getOption()) {
                                    case OVERWRITING:
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyToManyBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyToManyBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyToManyBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyToManyBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
                                                        }
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyHeapContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyHeapTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyDirectContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyDirectTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyToManyBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyToManyBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyToManyBlockingContentionPerfTest.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyToManyBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyToManyBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyToManyBlockingContentionPerfTest.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
                                                        }
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyHeapBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyHeapBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyToManyHeapBlockingContentionPerfTest.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyToManyDirectBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyToManyDirectBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyToManyHeapBlockingContentionPerfTest.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
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
                                                        return FastManyToManyContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastManyToManyTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastPrefilledManyToManyContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastPrefilledManyToManyTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyToManyHeapContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyToManyHeapTest.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyToManyDirectContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyToManyDirectTest.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    default:
                                        throw new AssertionError();
                                }
                            default:
                                throw new AssertionError();
                        }
                    case MANY_READERS:
                        switch (threadSafetyStrategy.getOption()) {
                            case LOCK_BASED:
                                switch (type.getOption()) {
                                    case OVERWRITING:
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyReadersBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyReadersBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyReadersBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyReadersBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
                                                        }
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersHeapContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersHeapTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersDirectContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersDirectTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyReadersBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyReadersBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyReadersBlockingContentionPerfTest.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyReadersBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyReadersBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyReadersBlockingBatchContentionPerfTest.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
                                                        }
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersHeapBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersHeapBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyReadersHeapBlockingContentionPerfTest.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyReadersDirectBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyReadersDirectBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyReadersDirectBlockingContentionPerfTest.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
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
                                                        return FastManyReadersContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastManyReadersTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastPrefilledManyReadersContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastPrefilledManyReadersTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyReadersHeapContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyReadersHeapTest.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyReadersDirectContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyReadersDirectTest.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    default:
                                        throw new AssertionError();
                                }
                            default:
                                throw new AssertionError();
                        }
                    case MANY_WRITERS:
                        switch (threadSafetyStrategy.getOption()) {
                            case LOCK_BASED:
                                switch (type.getOption()) {
                                    case OVERWRITING:
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyWritersBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyWritersBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyWritersBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyWritersBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
                                                        }
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersHeapContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersHeapTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersDirectContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersDirectTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return ManyWritersBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return ManyWritersBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return ManyWritersBlockingBatchContentionPerfTest.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledManyWritersBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledManyWritersBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledManyWritersBlockingBatchContentionPerfTest.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
                                                        }
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersHeapBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersHeapBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyWritersHeapBlockingContentionPerfTest.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return ManyWritersDirectBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return ManyWritersDirectBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return ManyWritersDirectBlockingContentionPerfTest.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
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
                                                        return FastManyWritersContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastManyWritersTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastPrefilledManyWritersContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastPrefilledManyWritersTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyWritersHeapContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyWritersHeapTest.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastManyWritersDirectContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastManyWritersDirectTest.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    default:
                                        throw new AssertionError();
                                }
                            default:
                                throw new AssertionError();
                        }
                    case VOLATILE:
                        switch (threadSafetyStrategy.getOption()) {
                            case LOCK_BASED:
                                switch (type.getOption()) {
                                    case OVERWRITING:
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return OneToOneBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return OneToOneBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledOneToOneBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledOneToOneBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
                                                        }
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneHeapContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneHeapTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneDirectContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneDirectTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return OneToOneBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return OneToOneBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return OneToOneBlockingBatchContentionPerfTest.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
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
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            case BATCH:
                                                                switch (contention.getOption()) {
                                                                    case CONTENTION:
                                                                        return PrefilledOneToOneBlockingBatchContentionTest.class;
                                                                    case NO_CONTENTION:
                                                                        return PrefilledOneToOneBlockingBatchTest.class;
                                                                    case UNBLOCKED_CONTENTION:
                                                                        return PrefilledOneToOneBlockingBatchContentionPerfTest.class;
                                                                    default:
                                                                        throw new AssertionError();
                                                                }
                                                            default:
                                                                throw new AssertionError();
                                                        }
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case HEAP:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneHeapBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneHeapBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return OneToOneHeapBlockingContentionPerfTest.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case DIRECT:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return OneToOneDirectBlockingContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return OneToOneDirectBlockingTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                        return OneToOneDirectBlockingContentionPerfTest.class;
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
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
                                                        return FastOneToOneContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastOneToOneTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            case PREFILLED:
                                                switch (contention.getOption()) {
                                                    case CONTENTION:
                                                        return FastPrefilledOneToOneContentionTest.class;
                                                    case NO_CONTENTION:
                                                        return FastPrefilledOneToOneTest.class;
                                                    case UNBLOCKED_CONTENTION:
                                                    default:
                                                        throw new AssertionError();
                                                }
                                            default:
                                                throw new AssertionError();
                                        }
                                    case HEAP:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastOneToOneHeapContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastOneToOneHeapTest.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    case DIRECT:
                                        switch (contention.getOption()) {
                                            case CONTENTION:
                                                return FastOneToOneDirectContentionTest.class;
                                            case NO_CONTENTION:
                                                return FastOneToOneDirectTest.class;
                                            case UNBLOCKED_CONTENTION:
                                            default:
                                                throw new AssertionError();
                                        }
                                    default:
                                        throw new AssertionError();
                                }
                            default:
                                throw new AssertionError();
                        }
                    default:
                        throw new AssertionError();
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
                            default:
                                throw new AssertionError();
                        }
                    case MANY_WRITERS:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return AgronaManyWritersContentionTest.class;
                            case NO_CONTENTION:
                                return AgronaManyWritersTest.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    case VOLATILE:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return AgronaOneToOneContentionTest.class;
                            case NO_CONTENTION:
                                return AgronaOneToOneTest.class;
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
                                return JCToolsManyToManyContentionTest.class;
                            case NO_CONTENTION:
                                return JCToolsManyToManyTest.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    case MANY_READERS:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return JCToolsManyReadersContentionTest.class;
                            case NO_CONTENTION:
                                return JCToolsManyReadersTest.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    case MANY_WRITERS:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return JCToolsManyWritersContentionTest.class;
                            case NO_CONTENTION:
                                return JCToolsManyWritersTest.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    case VOLATILE:
                        switch (contention.getOption()) {
                            case CONTENTION:
                                return JCToolsOneToOneContentionTest.class;
                            case NO_CONTENTION:
                                return JCToolsOneToOneTest.class;
                            case UNBLOCKED_CONTENTION:
                            default:
                                throw new AssertionError();
                        }
                    default:
                        throw new AssertionError();
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
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return ArrayManyReadersContentionTest.class;
                                    case NO_CONTENTION:
                                        return ArrayManyReadersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return ArrayManyWritersContentionTest.class;
                                    case NO_CONTENTION:
                                        return ArrayManyWritersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return ArrayOneToOneContentionTest.class;
                                    case NO_CONTENTION:
                                        return ArrayOneToOneTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            default:
                                throw new AssertionError();
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
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedBlockingManyReadersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedBlockingManyReadersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedBlockingManyWritersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedBlockingManyWritersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedBlockingOneToOneContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedBlockingOneToOneTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            default:
                                throw new AssertionError();
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
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedTransferManyReadersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedTransferManyReadersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedTransferManyWritersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedTransferManyWritersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedTransferOneToOneContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedTransferOneToOneTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            default:
                                throw new AssertionError();
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
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_READERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedConcurrentManyReadersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedConcurrentManyReadersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case MANY_WRITERS:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedConcurrentManyWritersContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedConcurrentManyWritersTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            case VOLATILE:
                                switch (contention.getOption()) {
                                    case CONTENTION:
                                        return LinkedConcurrentOneToOneContentionTest.class;
                                    case NO_CONTENTION:
                                        return LinkedConcurrentOneToOneTest.class;
                                    case UNBLOCKED_CONTENTION:
                                    default:
                                        throw new AssertionError();
                                }
                            default:
                                throw new AssertionError();
                        }
                    default:
                        throw new AssertionError();
                }
            default:
                throw new AssertionError();
        }
    }
}
