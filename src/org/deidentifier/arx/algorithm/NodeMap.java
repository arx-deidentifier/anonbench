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
 * This class implements a map from arrays representing transformations to the actual node objects
 * 
 * @author Prasser, Kohlmayer
 */
public class NodeMap {

    /** The array. */
    public final Node[] array;

    /** The max indices. */
    private final int[] max;

    /** The offsets. */
    private final int[] offsets;

    /**
     * Instantiates a new lattice node map.
     * 
     * @param _max the max
     */
    public NodeMap(final int[] _max) {
        max = new int[_max.length];
        offsets = new int[_max.length];

        int size = 1;
        for (int i = 0; i < _max.length; i++) {
            offsets[i] = size;
            size *= _max[i];
            max[i] = _max[i] - 1;
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
    public NodeMap(final Node[] array, final int[] offsets, final int[] max) {
        this.offsets = offsets;
        this.array = array;
        this.max = max;
    }

    /**
     * Gets the.
     * 
     * @param key the key
     * @return the node
     */
    public Node get(final int[] key) {
        final int index = getIndex(key);
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
        final int index = getIndex(key);
        array[index] = value;
    }

    /**
     * Calculate index from array.
     * 
     * @param indices the indices
     * @return the int
     */
    private final int getIndex(final int[] indices) {
        int index = 0;
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] > max[i]) { return -1; }
            index += offsets[i] * indices[i];
        }
        return index;
    }
}
