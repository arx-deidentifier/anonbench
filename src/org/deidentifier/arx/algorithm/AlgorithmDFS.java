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
import org.deidentifier.arx.framework.check.history.History.PruningStrategy;
import org.deidentifier.arx.framework.lattice.Lattice;
import org.deidentifier.arx.framework.lattice.Node;

/**
 * This class provides a implementation of a DFS algorithm.
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class AlgorithmDFS extends AbstractBenchmarkAlgorithm {

    /**
     * Creates a new instance of the dfs algorithm.
     * 
     * @param lattice The lattice
     * @param checker The checker
     * @param metric The metric
     */
    public AlgorithmDFS(final Lattice lattice, final INodeChecker checker) {
        super(lattice, checker);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.algorithm.AbstractAlgorithm#traverse()
     */
    @Override
    public void traverse() {

        checker.getHistory().setPruningStrategy(PruningStrategy.CHECKED);
        for (final Node[] level : lattice.getLevels()) {
            for (final Node node : level) {
                if (!node.isTagged()) {
                    dfs(node);
                }
            }
        }
    }

    /**
     * DFS search
     */
    private void dfs(final Node node) {

        // Check and tag
        if (!node.isTagged()) {
            check(node);
            lattice.tagAnonymous(node, node.isAnonymous());
        }
        
        // DFS
        for (final Node child : node.getSuccessors()) {
            if (!child.isTagged()) {
                dfs(child);
            }
        }
    }
}
