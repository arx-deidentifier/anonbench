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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import org.deidentifier.arx.framework.check.INodeChecker;
import org.deidentifier.arx.framework.check.history.History;
import org.deidentifier.arx.framework.check.history.History.PruningStrategy;
import org.deidentifier.arx.framework.data.GeneralizationHierarchy;
import org.deidentifier.arx.framework.lattice.Lattice;
import org.deidentifier.arx.framework.lattice.Node;

/**
 * This class provides a reference implementation of the Binary FLASH Algorithm.
 * 
 * @author Prasser, Kohlmayer
 */
public class AlgorithmFlash extends AbstractBenchmarkAlgorithm {

    /** The stack. */
    private final Stack<Node>         stack;

    /** The heap. */
    private final PriorityQueue<Node> pqueue;

    /** The current path. */
    private final ArrayList<Node>     path;

    /** Are the pointers for a node with id 'index' already sorted?. */
    private final boolean[]           sorted;

    /** The strategy. */
    private final FLASHStrategy       strategy;

    /** The history */
    private History                   history;

    /**
     * Creates a new instance of the FLASH algorithm.
     * 
     * @param lattice
     *            The lattice
     * @param history
     *            The history
     * @param checker
     *            The checker
     * @param new FLASHStrategy(lattice, manager.getHierarchies() The strategy
     */
    public AlgorithmFlash(final Lattice lattice,
                          final INodeChecker checker,
                          final GeneralizationHierarchy[] hierarchies) {

        super(lattice, checker);
        this.strategy = new FLASHStrategy(lattice, hierarchies);
        this.pqueue = new PriorityQueue<Node>(11, strategy);
        this.sorted = new boolean[lattice.getSize()];
        this.path = new ArrayList<Node>();
        this.stack = new Stack<Node>();
        this.history = checker.getHistory();
        this.history.setPruningStrategy(PruningStrategy.ANONYMOUS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.ARX.algorithm.AbstractAlgorithm#traverse()
     */
    @Override
    public void traverse() {

        // Init
        pqueue.clear();
        stack.clear();

        // For each node
        final int length = lattice.getLevels().length;
        for (int i = 0; i < length; i++) {
            Node[] level;
            level = this.sort(i);
            for (final Node node : level) {
                if (!node.isTagged()) {
                    pqueue.add(node);
                    while (!pqueue.isEmpty()) {
                        Node head = pqueue.poll();
                        // if anonymity is unknown
                        if (!head.isTagged()) {
                            findPath(head);
                            head = checkPathBinary(path);
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks a path binary.
     * 
     * @param path
     *            The path
     */
    private final Node checkPathBinary(final List<Node> path) {
        int low = 0;
        int high = path.size() - 1;
        Node lastAnonymousNode = null;

        while (low <= high) {

            final int mid = (low + high) >>> 1;
            final Node node = path.get(mid);

            if (!node.isTagged()) {
                check(node);
                lattice.tagAnonymous(node, node.isAnonymous());
                if (!node.isAnonymous()) {
                    for (final Node up : node.getSuccessors()) {
                        if (!up.isTagged()) {
                            pqueue.add(up);
                        }
                    }
                }
            }

            if (node.isAnonymous()) {
                lastAnonymousNode = node;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return lastAnonymousNode;
    }

    /**
     * Greedily find a path.
     * 
     * @param current
     *            The current
     * @return the list
     */
    private final List<Node> findPath(Node current) {
        path.clear();
        path.add(current);
        boolean found = true;
        while (found) {
            found = false;
            this.sort(current);
            for (final Node candidate : current.getSuccessors()) {
                if (!candidate.isTagged()) {
                    current = candidate;
                    path.add(candidate);
                    found = true;
                    break;
                }
            }
        }
        return path;
    }

    /**
     * Sorts a level.
     * 
     * @param level
     *            The level
     * @return the node[]
     */

    private final Node[] sort(final int level) {
        final Node[] result = new Node[lattice.getUntaggedCount(level)];
        if (result.length == 0) { return result; }
        int index = 0;
        final Node[] nlevel = lattice.getLevels()[level];
        for (final Node n : nlevel) {
            if (!n.isTagged()) {
                result[index++] = n;
            }
        }
        Arrays.sort(result, strategy);
        return result;
    }

    /**
     * Sorts upwards pointers of a node.
     * 
     * @param current
     *            The current
     */
    private final void sort(final Node current) {
        if (!sorted[current.id]) {
            Arrays.sort(current.getSuccessors(), strategy);
            sorted[current.id] = true;
        }
    }
}
