/*
 * Source code of our CBMS 2014 paper "A benchmark of globally-optimal 
 *      methods for the de-identification of biomedical data"
 *      
 * Copyright (C) 2014 Florian Kohlmayer, Fabian Prasser
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.deidentifier.arx;

import java.io.IOException;

import org.deidentifier.arx.BenchmarkSetup.BenchmarkAlgorithm;
import org.deidentifier.arx.BenchmarkSetup.BenchmarkCriterion;
import org.deidentifier.arx.BenchmarkSetup.BenchmarkDataset;
import org.deidentifier.arx.algorithm.AbstractBenchmarkAlgorithm;
import org.deidentifier.arx.algorithm.AlgorithmBFS;
import org.deidentifier.arx.algorithm.AlgorithmDFS;
import org.deidentifier.arx.algorithm.AlgorithmFlash;
import org.deidentifier.arx.algorithm.AlgorithmIncognito;
import org.deidentifier.arx.algorithm.AlgorithmOLA;
import org.deidentifier.arx.framework.check.INodeChecker;
import org.deidentifier.arx.framework.check.NodeChecker;
import org.deidentifier.arx.framework.data.DataManager;
import org.deidentifier.arx.framework.data.Dictionary;
import org.deidentifier.arx.framework.lattice.Lattice;
import org.deidentifier.arx.framework.lattice.LatticeBuilder;
import org.deidentifier.arx.framework.lattice.Node;
import org.deidentifier.arx.test.TestConfiguration;

import de.linearbits.subframe.Benchmark;

/**
 * This class implements the main benchmark driver
 * @author Fabian Prasser
 */
public class BenchmarkDriver {

    /** Snapshot size. */
    private final double    snapshotSizeDataset  = 0.2d;

    /** Snapshot size snapshot */
    private final double    snapshotSizeSnapshot = 0.8d;

    /** History size. */
    private final int       historySize          = 200;

    /** The benchmark instance */
    private final Benchmark benchmark;

    /**
     * Creates a new benchmark driver
     * 
     * @param benchmark
     */
    public BenchmarkDriver(Benchmark benchmark) {
        this.benchmark = benchmark;
    }

    /**
     * Performs data anonymization
     * 
     * @param dataset
     * @param criteria
     * @param algorithm
     * @param warmup
     * @throws IOException
     */
    public void anonymize(BenchmarkDataset dataset,
                          BenchmarkCriterion[] criteria,
                          BenchmarkAlgorithm algorithm,
                          boolean warmup) throws IOException {

        // Build implementation
        AbstractBenchmarkAlgorithm implementation = getImplementation(dataset, criteria, algorithm);

        // Execute
        if (!warmup) benchmark.startTimer(BenchmarkMain.EXECUTION_TIME);
        implementation.traverse();
        if (!warmup) benchmark.addStopTimer(BenchmarkMain.EXECUTION_TIME);
        if (!warmup) benchmark.addValue(BenchmarkMain.NUMBER_OF_CHECKS, implementation.getNumChecks());
        if (!warmup) benchmark.addValue(BenchmarkMain.NUMBER_OF_ROLLUPS, implementation.getNumRollups());
    }

    /**
     * Performs data anonymization and returns a TestConfiguration
     * 
     * @param dataset
     * @param criteria
     * @param algorithm
     * @param warmup
     * @throws IOException
     */
    public TestConfiguration test(BenchmarkDataset dataset,
                                  BenchmarkCriterion[] criteria,
                                  BenchmarkAlgorithm algorithm) throws IOException {

        // Build implementation
        AbstractBenchmarkAlgorithm implementation = getImplementation(dataset, criteria, algorithm);

        // Execute
        implementation.traverse();
        
        // Collect
        Node optimum = implementation.getGlobalOptimum();
        String loss = String.valueOf(optimum.getInformationLoss().getValue());
        int[] transformation = optimum.getTransformation();
        
        return new TestConfiguration(dataset, criteria, loss, transformation);
    }

    /**
     * @param dataset
     * @param criteria
     * @param algorithm
     * @return
     * @throws IOException
     */
    private AbstractBenchmarkAlgorithm getImplementation(BenchmarkDataset dataset,
                                                         BenchmarkCriterion[] criteria,
                                                         BenchmarkAlgorithm algorithm) throws IOException {
        // Prepare
        Data data = BenchmarkSetup.getData(dataset, criteria);
        ARXConfiguration config = BenchmarkSetup.getConfiguration(dataset, criteria);
        DataHandle handle = data.getHandle();

        // Encode
        final String[] header = ((DataHandleInput) handle).header;
        final int[][] dataArray = ((DataHandleInput) handle).data;
        final Dictionary dictionary = ((DataHandleInput) handle).dictionary;
        final DataManager manager = new DataManager(header,
                                                    dataArray,
                                                    dictionary,
                                                    data.getDefinition(),
                                                    config.getCriteria());

        // Initialize
        config.initialize(manager);

        // Build or clean the lattice
        Lattice lattice = new LatticeBuilder(manager.getMaxLevels(),
                                             manager.getMinLevels(),
                                             manager.getHierachyHeights()).build();

        // Build a node checker, for all algorithms but Incognito
        INodeChecker checker = null;
        if (algorithm != BenchmarkAlgorithm.INCOGNITO){
            checker = new NodeChecker(  manager,
                                        config.getMetric(),
                                        config.getInternalConfiguration(),
                                        historySize,
                                        snapshotSizeDataset,
                                        snapshotSizeSnapshot);
        }

        // Initialize the metric
        config.getMetric().initialize(handle.getDefinition(),
                                      manager.getDataQI(),
                                      manager.getHierarchies(),
                                      config);

        // Create an algorithm instance
        AbstractBenchmarkAlgorithm implementation;
        switch (algorithm) {
        case BFS:
            implementation = new AlgorithmBFS(lattice, checker);
            break;
        case DFS:
            implementation = new AlgorithmDFS(lattice, checker);
            break;
        case FLASH:
            implementation = new AlgorithmFlash(lattice, checker, manager.getHierarchies());
            break;
        case INCOGNITO:
            implementation = new AlgorithmIncognito(lattice, manager,
                                                             config.getMetric(),
                                                             config.getInternalConfiguration(),
                                                             historySize,
                                                             snapshotSizeDataset,
                                                             snapshotSizeSnapshot);
            break;
        case OLA:
            implementation = new AlgorithmOLA(lattice, checker);
            break;
        default:
            throw new RuntimeException("Invalid algorithm");
        }
        return implementation;
    }
}
