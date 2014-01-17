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

import org.deidentifier.arx.framework.lattice.Lattice;
import org.deidentifier.arx.framework.lattice.Node;

/**
 * This class implements a lattice for Incognito
 * 
 * @author Prasser, Kohlmayer
 */
public class IncognitoLattice {

    /** The lattice. */
    private final Lattice           lattice;

    /** The map. */
    private final OLALatticeNodeMap map;

    /**
     * Instantiates a new lattice incognito.
     * 
     * @param lattice the lattice
     */
    public IncognitoLattice(final Lattice lattice) {
        this.lattice = lattice;
        map = new OLALatticeNodeMap(lattice.getMaximumGeneralizationLevels());
        for (int i = 0; i < lattice.getLevels().length; i++) {
            final Node[] nodes = lattice.getLevels()[i];
            for (int j = 0; j < nodes.length; j++) {
                getMap().put(nodes[j].getTransformation(), nodes[j]);
            }
        }
    }

    /**
     * Gets the lattice.
     * 
     * @return the lattice
     */
    public Lattice getLattice() {
        return lattice;
    }

    /**
     * Gets the map.
     * 
     * @return the map
     */
    public OLALatticeNodeMap getMap() {
        return map;
    }

}
