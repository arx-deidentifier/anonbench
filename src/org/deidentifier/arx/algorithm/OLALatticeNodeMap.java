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

import org.deidentifier.arx.framework.lattice.Node;

/**
 * This class implements a map from arrays representing transformations to the actual node object
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class OLALatticeNodeMap {

    /** The array. */
    public final Node[] array;

    /** The max indices. */
    private final int[] maxIndices;

    /** The offsets. */
    private final int[] offsets;

    /**
     * Instantiates a new lattice node map.
     * 
     * @param max the max
     */
    public OLALatticeNodeMap(final int[] max) {
        maxIndices = new int[max.length];
        offsets = new int[max.length];

        int size = 1;
        for (int i = 0; i < max.length; i++) {
            offsets[i] = size;
            size *= max[i];
            maxIndices[i] = max[i] - 1;
        }
        array = new Node[size];
    }

    /**
     * Instantiates a new lattice node map.
     * 
     * @param array the array
     * @param offsets the offsets
     * @param max the max indices
     */
    public OLALatticeNodeMap(final Node[] array,
                             final int[] offsets,
                             final int[] max) {
        this.offsets = offsets;
        this.array = array;
        this.maxIndices = max;
    }

    /**
     * Calculate array from index.
     * 
     * @param n the n
     * @return the int[]
     */
    public final int[] calculateArrayFromIndex(int n) {
        final int[] state = new int[maxIndices.length];
        for (int i = state.length - 1; i >= 0; i--) {
            state[i] = n / offsets[i];
            n -= state[i] * offsets[i];
        }
        return state;
    }

    /**
     * Calculate index from array.
     * 
     * @param indices the indices
     * @return the int
     */
    public final int calculateIndexFromArray(final int[] indices) {
        int index = 0;
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] > maxIndices[i]) { return -1; }
            index += offsets[i] * indices[i];
        }
        return index;
    }

    /**
     * Gets the.
     * 
     * @param key the key
     * @return the node
     */
    public Node get(final int[] key) {
        final int index = calculateIndexFromArray(key);
        if (index == -1) {
            return null;
        } else {
            return array[index];
        }
    }

    /**
     * Put.
     * 
     * @param key the key
     * @param value the value
     */
    public void put(final int[] key, final Node value) {
        final int index = calculateIndexFromArray(key);
        array[index] = value;
    }
}
