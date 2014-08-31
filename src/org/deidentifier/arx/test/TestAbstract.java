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

package org.deidentifier.arx.test;

import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.deidentifier.arx.BenchmarkDriver;
import org.deidentifier.arx.BenchmarkSetup;
import org.deidentifier.arx.BenchmarkSetup.BenchmarkAlgorithm;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract test case for anonbench
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public abstract class TestAbstract extends TestCase {

    /** The test case */
    private final TestConfiguration config;

    /**
     * Creates a new instance
     * 
     * @param config
     */
    public TestAbstract(final TestConfiguration config) {
        this.config = config;
    }

    @Override
    @Before
    public void setUp() {
        // We don't want to call super.setUp()
    }

    @Test
    public void test() throws IOException {
        
        // Initialize
        BenchmarkDriver driver = new BenchmarkDriver(null);

        // For each algorithm
        for (BenchmarkAlgorithm algorithm : BenchmarkSetup.getAlgorithms()) {
            
            // Skip BFS, as it simply takes too long
            if (algorithm == BenchmarkAlgorithm.BFS) {
                continue;
            }
                
            // Collect
            TestConfiguration result = driver.test(config.dataset, 
                                                   config.criteria, 
                                                   algorithm);
            // Check
            assertEquals(algorithm + ": Information loss doesn't match", config.informationLoss, result.informationLoss);
            assertTrue(algorithm + ": Transformation doesn't match", Arrays.equals(result.transformation, config.transformation));
        }
    }
}
