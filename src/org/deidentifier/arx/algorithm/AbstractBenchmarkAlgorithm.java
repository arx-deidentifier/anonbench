/*
 * Source code of our CBMS 2014 paper "A benchmark of globally-optimal
 * methods for the de-identification of biomedical data"
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

package org.deidentifier.arx.algorithm;

import org.deidentifier.arx.framework.check.INodeChecker;
import org.deidentifier.arx.framework.lattice.Lattice;
import org.deidentifier.arx.framework.lattice.Node;

/**
 * Abstract base class for algorithms used in the benchmark
 * @author Fabian Prasser
 */
public abstract class AbstractBenchmarkAlgorithm extends AbstractAlgorithm {

    /** The number of rollups that could have been performed*/
    protected int  rollups;
    /** The number of checks*/
    protected int  checks;
    /** The node checked previously*/
    protected Node previous;

    /**
     * Constructor
     * @param lattice
     * @param checker
     */
    protected AbstractBenchmarkAlgorithm(Lattice lattice, INodeChecker checker) {
        super(lattice, checker);
    }

    /**
     * Returns the number of potential rollups
     * @return
     */
    public int getNumRollups() {
        return rollups;
    }
    
    /**
     * Returns the number of checks
     * @return
     */
    public int getNumChecks() {
        return checks;
    }

    /**
     * Performs a check and keeps track of potential rollups
     * @param node
     */
    protected void check(Node node) {

        // Check
        checker.check(node);
        checks++;

        // Store
        if (previous == null) {
            previous = node;
            return;
        }

        // Check if successor
        boolean successor = true;
        for (int i = 0; i < node.getTransformation().length; i++) {
            if (node.getTransformation()[i] < previous.getTransformation()[i]) {
                successor = false;
            }
        }

        previous = node;

        // Count
        if (successor) {
            rollups++;
        }
    }
}
