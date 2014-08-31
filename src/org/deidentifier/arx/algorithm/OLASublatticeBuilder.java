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
import java.util.List;

import org.deidentifier.arx.framework.lattice.Node;

/**
 * This class is used for building sub-lattices
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class OLASublatticeBuilder {

    /** The map. */
    private NodeMap    map   = null;

    /** The nodes. */
    private List<Node> nodes = null;

    /**
     * Instantiates a new sublattice builder.
     * 
     * @param map
     *            the map
     */
    public OLASublatticeBuilder(final NodeMap map) {
        this.map = map;
    }

    /**
     * Builds a sub-lattice
     * 
     * @param bottom the bottom
     * @param top the top
     * @param midlevel the mid-level
     * @return the list
     */
    public List<Node> build(final int[] bottom,
                            final int[] top,
                            final int midlevel) {
        nodes = new ArrayList<Node>();
        final int[] max = new int[bottom.length];
        int bottomLevel = 0;
        for (int i = 0; i < max.length; i++) {
            max[i] = top[i] - bottom[i];
            bottomLevel += bottom[i];
        }
        enumerateMidNodes(bottom.clone(), max, 0, bottomLevel, midlevel);
        return nodes;
    }

    /**
     * Enumerates all mid-nodes of the lattice.
     * 
     * @param state the state
     * @param max the max
     * @param index the index
     * @param currentLevel the current level
     * @param targetLevel the target level
     */
    private void enumerateMidNodes(final int[] state,
                                   final int[] max,
                                   final int index,
                                   int currentLevel,
                                   final int targetLevel) {

        // Recursion determination
        if (index == state.length) {
            if (currentLevel == targetLevel) {
                nodes.add(map.get(state));
            }
            return;
        }

        // Store
        final int tempState = state[index];
        final int tempLevel = currentLevel;

        // Iterate
        for (int diff = 0; diff <= max[index]; diff++) {

            // Recurse
            if (currentLevel <= targetLevel) {
                enumerateMidNodes(state,
                                  max,
                                  index + 1,
                                  currentLevel,
                                  targetLevel);
            }
            // Or break
            else {
                break;
            }

            // Next
            state[index]++;
            currentLevel++;
        }

        // Restore
        state[index] = tempState;
        currentLevel = tempLevel;
    }
}
