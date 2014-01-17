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

import java.util.Iterator;
import java.util.List;

import org.deidentifier.arx.framework.lattice.Node;

/**
 * This class implements an iterator, that enumerates nodes on mid-level of a given lattice
 * 
 * @author Prasser, Kohlmayer
 */
public class OLASublatticeNodeIterator implements Iterator<Node> {

    /** The builder. */
    private OLASublatticeBuilder builder  = null;

    /** The index. */
    private int                  index    = 0;

    /** The mid nodes. */
    private List<Node>           midNodes = null;

    /**
     * Instantiates a new sublattice based node iterator.
     * 
     * @param map the map
     * @param bottom the bottom
     * @param top the top
     * @param midLevel the mid level
     */
    public OLASublatticeNodeIterator(final OLALatticeNodeMap map,
                                     final Node bottom,
                                     final Node top,
                                     final int midLevel) {
        builder = new OLASublatticeBuilder(map);
        midNodes = builder.build(bottom.getTransformation(), top.getTransformation(), midLevel);
        index = midNodes.size() - 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return index > -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#next()
     */
    @Override
    public Node next() {
        return midNodes.get(index--);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        // Empty by design
    }
}
