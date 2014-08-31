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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.framework.check.INodeChecker;
import org.deidentifier.arx.framework.check.INodeChecker.Result;
import org.deidentifier.arx.framework.check.NodeChecker;
import org.deidentifier.arx.framework.data.DataManager;
import org.deidentifier.arx.framework.lattice.Lattice;
import org.deidentifier.arx.framework.lattice.LatticeBuilder;
import org.deidentifier.arx.framework.lattice.Node;
import org.deidentifier.arx.metric.Metric;

/**
 * This class implements the Incognito algorithm proposed in:<br>
 * <br>
 * K. LeFevre et al. "Incognito: efficient full-domain K-anonymity".
 * Proceedings of the 2005 ACM SIGMOD international Conference on Management of Data, 49-60. 
 * 
 * @author Prasser, Kohlmayer
 */
public class AlgorithmIncognito extends AbstractBenchmarkAlgorithm {

    /** The metric to be used by this algorithm*/
    private Metric<?> metric;
    
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
        this(lattice, metric, new IncognitoNodeChecker(manager, Metric.createHeightMetric(), config, historySize, snapshotSizeDataset, snapshotSizeSnapshot));
    }

    /**
     * Instantiates a new incognito algorithm.
     * 
     * @param lattice the lattice
     * @param checker the checker
     */
    private AlgorithmIncognito(Lattice lattice, Metric<?> metric, INodeChecker checker) {
        super(lattice, checker);
        this.metric = metric;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.algorithm.AbstractAlgorithm#traverse()
     */
    @Override
    public void traverse() {

        // Prepare
        IncognitoLattice globalLattice = new IncognitoLattice(super.lattice);
        IncognitoNodeChecker checker = (IncognitoNodeChecker) super.checker;
        int numQIs = globalLattice.getLattice().getMaximumGeneralizationLevels().length;
        Set<Set<Integer>>[] combinations = getCombinations(numQIs);
        IncognitoContext context = new IncognitoContext();
        
        // For subset of QIs of any possible size
        for (int i = 0; i < numQIs; i++) {

            // Obtain all combinations for this size
            Set<Set<Integer>> combination = combinations[i];

            // For each combination
            for (Set<Integer> _combination : combination) {

                // Create ordered subset
                int[] subset = getOrderedArray(_combination);

                // Use a special lattice for any subset of the QIs and the main lattice for all QIs
                if (i == numQIs - 1) {
                    context.setLattice(globalLattice);
                } else {
                    context.setLattice(getLattice(subset));
                }

                // Reset previous node for counting roll-ups correctly
                previous = null;

                // Tell the node checker about the subset
                checker.setActiveColumns(subset);

                // Prune nodes that can not be anonymous due to results from previous runs
                if (i > 0) {
                    prune(context, subset);
                }

                // During the last iteration use the actually specified metric
                if (i == numQIs - 1) {
                    checker.setMetric(this.metric);
                }

                // Perform a breath first search over current sub-lattice
                bfs(globalLattice, checker, context, subset);
            }
        }
    }

    /**
     * Performs a breath first search over current sub-lattice
     * 
     * @param globalLattice
     * @param checker
     * @param context
     * @param subset
     * @param representative
     */
    private void bfs(IncognitoLattice globalLattice,
                     IncognitoNodeChecker checker,
                     IncognitoContext context,
                     int[] subset) {

        // For each level
        for (Node[] localLevels : context.getLevels()) {
            
            // For each transformation
            for (Node localNode : localLevels) {
                
                // If it is not tagged already
                if (!isTagged(localNode)) {

                    // Expand local representation to global representation
                    Node globalNode = getGlobalNode(globalLattice, subset, localNode);

                    // Check
                    context.getLocalLattice().setChecked(localNode, check(checker, globalNode));
                    tag(context.getLocalLattice(), localNode);
                    
                    // And tag
                    if (!isAnonymous(localNode)) {
                        context.getNonAnonymousNodes().add(localNode);
                        context.getNonAnonymousTransformations().add(subset);
                    }
                    
                    // Track optimum
                    if (context.getLattice() == globalLattice) {
                        trackOptimum(globalNode);
                    }
                }
            }
        }
    }

    /**
     * Checks a node
     * 
     * @param checker
     * @param node
     */
    private Result check(NodeChecker checker, Node node) {

        Result result = checker.check(node);
        checks++;

        // Store
        if (previous == null) {
            previous = node;
            return result;
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
        
        // Return
        return result;
    }

    /**
     * Returns all possible combinations of numbers between 0 and maximum-1
     * 
     * @param maximum the maximum
     * @return all possible combinations of numbers between 0 and maximum-1
     */
    private Set<Set<Integer>>[] getCombinations(int maximum) {
        @SuppressWarnings("unchecked")
        Set<Set<Integer>>[] combinations = new HashSet[maximum];
        for (Set<Integer> set2 : getPowerSet(maximum)) {
            int size = set2.size();
            if (size > 0) {
                Set<Set<Integer>> list = combinations[size - 1];
                if (list == null) {
                    list = new HashSet<Set<Integer>>();
                }
                list.add(set2);
                combinations[size - 1] = list;
            }
        }
        return combinations;
    }

    /**
     * Returns a global representation of the given node in a local lattice for the
     * given subset
     * @param lattice
     * @param subset
     * @param node
     * @return
     */
    private Node getGlobalNode(IncognitoLattice lattice, int[] subset, Node node) {
        int[] representative = new int[lattice.getLattice().getMaximumGeneralizationLevels().length];
        for (int j = 0; j < subset.length; j++) {
            representative[subset[j]] = node.getTransformation()[j];
        }
        return lattice.getMap().get(representative);
    }

    /**
     * Builds the lattice for a given subset.
     * 
     * @param subset the subset
     * @return the lattice
     */
    private IncognitoLattice getLattice(int[] subset) {
        int[] max = new int[subset.length];
        int[] min = new int[subset.length];
        int[] height = new int[subset.length];
        for (int i = 0; i < max.length; i++) {
            height[i] = lattice.getMaximumGeneralizationLevels()[subset[i]];
            max[i] = lattice.getMaximumGeneralizationLevels()[subset[i]] - 1;
        }
        return new IncognitoLattice(new LatticeBuilder(max, min, height).build());
    }

    /**
     * Returns an array containing the given elements in ascending order
     * @param combination
     * @return
     */
    private int[] getOrderedArray(Set<Integer> combination) {
      int[] subset = new int[combination.size()];
      int pos = 0;
      for (int k : combination) {
          subset[pos++] = k;
      }
      Arrays.sort(subset);
      return subset;
    }
    
    /**
     * Returns the power set of all numbers between 0 and maximum-1
     * 
     * @param maximum the maximum
     * @return the power set of all numbers between 0 and maximum-1
     */
    private Set<Set<Integer>> getPowerSet(int maximum) {
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < maximum; i++) {
            set.add(i);
        }
        return getPowerSet(set);
    }
    
    /**
     * Returns the power set of the given set
     * 
     * @param <T> the generic type
     * @param set the set
     * @return the power set
     */
    private <T> Set<Set<T>> getPowerSet(Set<T> set) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (set.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(set);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> tset : getPowerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(tset);
            sets.add(newSet);
            sets.add(tset);
        }
        return sets;
    }

    /**
     * Checks if the array is a superset of the given array of indices
     * 
     * @param subset the subset
     * @param superset the potential superset
     * @return true, if is super set
     */
    private boolean isSuperSet(int[] subset, int[] superset) {
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

    /**
     * Prunes parts of the new search space, based on results from previous iterations
     * @param context
     * @param currentSubset
     */
    private void prune(IncognitoContext context, int[] currentSubset) {
        
        // Prepare
        ArrayList<Node> nodes = context.getNonAnonymousNodes();
        ArrayList<int[]> transformations = context.getNonAnonymousTransformations();
        
        // For each transformation that was determined to be non-anonymous previously
        for (int i = 0; i < nodes.size(); i++) {

            // Obtain data about previous non-anonymous transformation
            int[] previousTransformation = nodes.get(i).getTransformation();
            int[] previousSubset = transformations.get(i);

            // Check if current subset is superset of previous non-anonymous transformation
            if (isSuperSet(currentSubset, previousSubset)) {

                // Obtain clone of top-node in current lattice
                Node[][] localLevels = context.getLevels();
                int[] localTransformation = localLevels[localLevels.length - 1][0].getTransformation().clone();

                // Adjust current transformation to match current lattice
                for (int j = 0; j < currentSubset.length; j++) {
                    for (int j2 = 0; j2 < previousSubset.length; j2++) {
                        if (currentSubset[j] == previousSubset[j2]) {
                            localTransformation[j] = previousTransformation[j2];
                        }
                    }
                }

                // Tag
                Node localNode = context.getLocalMap().get(localTransformation);
                setAnonymous(context.getLocalLattice(), localNode, false);
                tag(context.getLocalLattice(), localNode);
            }
        }
    }
}
