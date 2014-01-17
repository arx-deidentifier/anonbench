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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.framework.check.INodeChecker;
import org.deidentifier.arx.framework.check.NodeChecker;
import org.deidentifier.arx.framework.check.history.History.PruningStrategy;
import org.deidentifier.arx.framework.data.DataManager;
import org.deidentifier.arx.framework.lattice.Lattice;
import org.deidentifier.arx.framework.lattice.LatticeBuilder;
import org.deidentifier.arx.framework.lattice.Node;
import org.deidentifier.arx.metric.Metric;

/**
 * This class implements the Incognito algorithm.
 * 
 * K. LeFevre et al. "Incognito: efficient full-domain K-anonymity"
 * Proceedings of the 2005 ACM SIGMOD international conference on Management of data, 49-60 
 * 
 * @author Prasser, Kohlmayer
 */
public class AlgorithmIncognito extends AbstractBenchmarkAlgorithm {

    /**
     * Instantiates a new incognito algorithm.
     * 
     * @param lattice the lattice
     * @param checker the checker
     */
    private AlgorithmIncognito(final Lattice lattice, final INodeChecker checker) {
        super(lattice, checker);
        checker.getHistory().setPruningStrategy(PruningStrategy.ANONYMOUS);

    }

    /**
     * Instantiates a new incognito algorithm.
     * @param lattice
     * @param manager
     * @param metric
     * @param config
     * @param historySize
     * @param snapshotSizeDataset
     * @param snapshotSizeSnapshot
     */
    public AlgorithmIncognito(Lattice lattice, DataManager manager, Metric<?> metric, ARXConfiguration config, int historySize, double snapshotSizeDataset, double snapshotSizeSnapshot) {

        this(lattice, new IncognitoNodeChecker(manager, metric, Metric.createHeightMetric(), config, historySize, snapshotSizeDataset, snapshotSizeSnapshot));
    }

    /**
     * Checks a node
     * 
     * @param checker
     * @param node
     */
    private void check(NodeChecker checker, Node node) {

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

    /**
     * Checks if the array is a superset of the given array of indices
     * 
     * @param subset the subset
     * @param superset the potential superset
     * @return true, if is super set
     */
    public boolean isSuperSet(final int[] subset, final int[] superset) {
        boolean isSuperset = true;
        int g = 0;
        for (int j = 0; j < subset.length; j++) {
            if (subset[j] == superset[g]) {
                g++;
                if (g == superset.length) {
                    break;
                }
            } else if (subset[j] > superset[g]) {
                isSuperset = false;
                break;
            }
        }
        if (isSuperset && (g < superset.length)) {
            isSuperset = false;
        }
        return isSuperset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.algorithm.AbstractAlgorithm#traverse()
     */
    @Override
    @SuppressWarnings("unchecked")
    public void traverse() {

        // Prepare
        final IncognitoLattice lattice = new IncognitoLattice(super.lattice);
        final IncognitoNodeChecker checker = (IncognitoNodeChecker) super.checker;
        final int width = lattice.getLattice().getMaximumGeneralizationLevels().length;

        // Compute powerset
        final HashSet<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < width; i++) {
            set.add(i);
        }
        final Set<Set<Integer>> powerSet = getPowerSet(set);

        // Compute all required combinations of quasi-identifiers
        final Set<Set<Integer>>[] combinations = new HashSet[width];
        for (final Set<Integer> set2 : powerSet) {
            final int size = set2.size();
            if (size > 0) {
                Set<Set<Integer>> list = combinations[size - 1];
                if (list == null) {
                    list = new HashSet<Set<Integer>>();
                }
                list.add(set2);
                combinations[size - 1] = list;
            }
        }

        // Current metadata
        ArrayList<Node> currentNonAnonymousNodes = new ArrayList<Node>();
        ArrayList<int[]> currentNonAnonyousTransformations = new ArrayList<int[]>();
        IncognitoLattice currentLattice = null;

        // For subset of QIs of any possible size
        for (int i = 0; i < width; i++) {

            // Obtain all combinations for this size
            final Set<Set<Integer>> combination = combinations[i];

            // For each combination
            for (final Set<Integer> set2 : combination) {

                // Create ordered subset
                final TreeSet<Integer> orderedSubsetSet = new TreeSet<Integer>();
                orderedSubsetSet.addAll(set2);
                final int[] subset = new int[set2.size()];
                int pos = 0;
                for (final int k : set2) {
                    subset[pos] = k;
                    pos++;
                }

                // Build lattice for subset, use main lattice if subset consists of all QIs
                if (i == (lattice.getLattice().getMaximumGeneralizationLevels().length - 1)) {
                    currentLattice = lattice;
                } else {
                    currentLattice = new IncognitoLattice(getLattice(subset));
                }

                // Reset previous node for counting rollups correctly
                previous = null;

                // Tell the node checker about the subset
                checker.setActiveColumns(subset);

                final Node[][] currentLevels = currentLattice.getLattice().getLevels();
                final int[] currentRepresentative = new int[width];

                // Prune nodes that can not be anonymous due to results from the previous runs
                if (i > 0) {
                    for (int k = 0; k < currentNonAnonymousNodes.size(); k++) {

                        // Obtain data
                        final int[] prevState = currentNonAnonymousNodes.get(k).getTransformation();
                        final int[] prevSubSet = currentNonAnonyousTransformations.get(k);

                        // check if current subset is superset of previous false Node
                        final boolean isSuperset = isSuperSet(subset, prevSubSet);

                        // If all nodes from previous subset are in current subset, pruning possible
                        if (isSuperset) {

                            // Clone
                            final int[] repesentativeStateInLocalLattice = currentLattice.getLattice().getLevels()[currentLattice.getLattice().getLevels().length - 1][0].getTransformation().clone();

                            // Change values of previous transformation in current lattice
                            for (int j = 0; j < subset.length; j++) {
                                for (int j2 = 0; j2 < prevSubSet.length; j2++) {
                                    if (subset[j] == prevSubSet[j2]) {
                                        repesentativeStateInLocalLattice[j] = prevState[j2];
                                    }
                                }
                            }

                            // Tag node accordingly
                            final Node pruneNode = currentLattice.getMap().get(repesentativeStateInLocalLattice);
                            currentLattice.getLattice().tagAnonymous(pruneNode, false);
                        }
                    }
                }

                // During the last iteration use the original metric
                if (i == (lattice.getLattice().getMaximumGeneralizationLevels().length - 1)) {
                    checker.changeMetric();
                }

                // Perform breath first search over current sub-lattice, backed up by overall lattice
                for (final Node[] levels : currentLevels) {
                    for (final Node localnode : levels) {
                        if (!localnode.isTagged()) {

                            // Expand local representation to global representation
                            for (int j = 0; j < subset.length; j++) {
                                currentRepresentative[subset[j]] = localnode.getTransformation()[j];
                            }
                            final Node repNode = lattice.getMap().get(currentRepresentative);

                            // Check
                            check(checker, repNode);

                            // And tag
                            if (repNode.getInformationLoss() != null) {
                                currentLattice.getLattice().tagAnonymous(localnode, true);
                                localnode.setInformationLoss(repNode.getInformationLoss());
                            } else {
                                currentLattice.getLattice().tagAnonymous(localnode, false);
                                currentNonAnonymousNodes.add(localnode);
                                currentNonAnonyousTransformations.add(subset);
                            }
                            localnode.setChecked();
                        }
                    }
                }
            }
        }
    }

    /**
     * Builds the lattice for a given subset.
     * 
     * @param subset the subset
     * @return the lattice
     */
    private Lattice getLattice(final int[] subset) {
        final int[] max = new int[subset.length];
        final int[] min = new int[subset.length];
        final int[] height = new int[subset.length];
        for (int i = 0; i < max.length; i++) {
            height[i] = lattice.getMaximumGeneralizationLevels()[subset[i]];
            max[i] = lattice.getMaximumGeneralizationLevels()[subset[i]] - 1;
        }
        final Lattice lattice = new LatticeBuilder(max, min, height).build();
        return lattice;
    }

    /**
     * Returns the powerset of the given set
     * 
     * @param <T> the generic type
     * @param set the set
     * @return the powerset
     */
    private <T> Set<Set<T>> getPowerSet(final Set<T> set) {
        final Set<Set<T>> sets = new HashSet<Set<T>>();
        if (set.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        final List<T> list = new ArrayList<T>(set);
        final T head = list.get(0);
        final Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (final Set<T> tset : getPowerSet(rest)) {
            final Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(tset);
            sets.add(newSet);
            sets.add(tset);
        }
        return sets;
    }
}
