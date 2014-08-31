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

package org.deidentifier.arx.algorithm;

import org.deidentifier.arx.framework.check.INodeChecker;
import org.deidentifier.arx.framework.lattice.Lattice;
import org.deidentifier.arx.framework.lattice.Node;

/**
 * This class implements a simple breadth-first-search with an outer loop.
 * 
 * @author Prasser, Kohlmayer
 */
public class AlgorithmBFS extends AbstractBenchmarkAlgorithm {

    /**
     * Creates a new instance of the breadth-first algorithm.
     * 
     * @param lattice The lattice
     * @param checker The checker
     */
    public AlgorithmBFS(final Lattice lattice, final INodeChecker checker) {
        super(lattice, checker);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.algorithm.AbstractAlgorithm#traverse()
     */
    @Override
    public void traverse() {

        // For each level
        final int length = lattice.getLevels().length;
        for (int i = 0; i < length; i++) {
            final Node[] level = lattice.getLevels()[i];
            
            // For each node
            for (final Node node : level) {
                
                // Check and tag
                if (!isTagged(node)) {
                    check(node);
                    tag(node);
                }
            }
        }
    }
}
