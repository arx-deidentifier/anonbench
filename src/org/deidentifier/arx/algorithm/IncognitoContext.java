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

import org.deidentifier.arx.framework.lattice.Lattice;
import org.deidentifier.arx.framework.lattice.Node;

/**
 * A context for one iteration of the algorithm for a specific sub-set of the quasi-identifiers
 * @author Fabian Prasser
 */
public class IncognitoContext {

    /** Current meta data*/
    private ArrayList<Node> nonAnonymousNodes;
    /** Current meta data*/
    private ArrayList<int[]> nonAnonymousTransformations;
    /** Current meta data*/
    private IncognitoLattice lattice;

    /**
     * Creates a new instance
     */
    public IncognitoContext() {
        nonAnonymousNodes = new ArrayList<Node>();
        nonAnonymousTransformations = new ArrayList<int[]>();
        lattice = null;
    }

    /**
     * @return the lattice
     */
    public IncognitoLattice getLattice() {
        return lattice;
    }

    /**
     * Returns the levels of the current lattice
     * @return
     */
    public Node[][] getLevels() {
        return lattice.getLattice().getLevels();
    }
    
    /**
     * Returns the current local lattice
     * @return
     */
    public Lattice getLocalLattice() {
        return this.lattice.getLattice();
    }
    
    /**
     * Returns the map of the current local lattice
     * @return
     */
    public NodeMap getLocalMap(){
        return this.lattice.getMap();
    }

    /**
     * @return the nonAnonymousNodes
     */
    public ArrayList<Node> getNonAnonymousNodes() {
        return nonAnonymousNodes;
    }

    /**
     * @return the nonAnonymousTransformations
     */
    public ArrayList<int[]> getNonAnonymousTransformations() {
        return nonAnonymousTransformations;
    }
    
    /**
     * @param lattice the lattice to set
     */
    public void setLattice(IncognitoLattice lattice) {
        this.lattice = lattice;
    }
}
