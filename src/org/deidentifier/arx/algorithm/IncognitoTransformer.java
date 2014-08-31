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

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.framework.check.StateMachine.TransitionType;
import org.deidentifier.arx.framework.check.Transformer;
import org.deidentifier.arx.framework.check.distribution.IntArrayDictionary;
import org.deidentifier.arx.framework.check.groupify.HashGroupifyEntry;
import org.deidentifier.arx.framework.check.groupify.IHashGroupify;
import org.deidentifier.arx.framework.check.transformer.AbstractTransformer;
import org.deidentifier.arx.framework.data.GeneralizationHierarchy;

/**
 * This class implements a transformer that can operate on the subset of
 * the quasi-identifiers
 * 
 * @author Prasser, Kohlmayer
 */
public class IncognitoTransformer extends Transformer {

    /** Indices of active columns */
    private int[]     activeColumns;

    /** The current buffer size */
    private int       bufferSize;

    /** The number of QIs. */
    private final int numQis;

    /**
     * Instantiates a new transformer for the Incognito algorithm
     * 
     * @param data the data
     * @param hierarchies the hierarchies
     * @param numQis the number of QIs
     * @param sensValues the buffer of sensitive values
     * @param config the configuration
     * @param dictVal dictionary
     * @param dictFreq dictionary
     */
    public IncognitoTransformer(final int[][] data,
                                final GeneralizationHierarchy[] hierarchies,
                                final int numQis,
                                final int[][] sensValues,
                                final ARXConfiguration config,
                                final IntArrayDictionary dictVal,
                                final IntArrayDictionary dictFreq) {
        super(data, hierarchies, sensValues, config, dictVal, dictFreq);
        this.numQis = numQis;
    }

    /**
     * Update buffer size.
     * 
     * @param activeColumns the active columns
     */
    public void updateBufferSize(final int[] activeColumns) {
        
        this.activeColumns = activeColumns;
        int numColumns = activeColumns.length;

        // resize the buffer
        if (numColumns != bufferSize) { 
            bufferSize = numColumns;
            buffer = new int[data.length][];
            for (int i = 0; i < data.length; i++) {
                buffer[i] = new int[numColumns];
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.check.Transformer#applyInternal(long,
     * int[], org.deidentifier.arx.framework.check.groupify.IHashGroupify,
     * org.deidentifier.arx.framework.check.groupify.IHashGroupify, int[],
     * org.deidentifier.arx.framework.check.StateMachine.TransitionType)
     */
    @Override
    protected IHashGroupify applyInternal(long projection,
                                          final int[] state,
                                          final IHashGroupify source,
                                          final IHashGroupify target,
                                          final int[] snapshot,
                                          final TransitionType transition) {

        int startIndex = 0;
        int stopIndex = 0;

        int counter = 0;
        for (int i = 0; i < numQis; i++) {
            if (counter >= activeColumns.length) {
                projection |= 1L << i;
            } else {
                if (activeColumns[counter] != i) {
                    projection |= 1L << i;
                } else {
                    counter++;
                }
            }
        }

        int bucket = 0;
        HashGroupifyEntry element = null;

        switch (transition) {
        case UNOPTIMIZED:
            startIndex = 0;
            stopIndex = data.length;
            break;
        case ROLLUP:
            startIndex = 0;
            stopIndex = source.size();
            bucket = 0;
            element = source.getFirstEntry();
            break;
        case SNAPSHOT:
            startIndex = 0;
            stopIndex = snapshot.length / config.getSnapshotLength();
            break;
        }

        AbstractTransformer app = null;

        app = getApplicator(projection);

        app.init(projection,
                 state,
                 target,
                 source,
                 snapshot,
                 transition,
                 startIndex,
                 stopIndex,
                 bucket,
                 element,
                 buffer);
        app.updateOutIndices(activeColumns, projection);

        return app.call();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.check.Transformer#getApplicator(long)
     */
    @Override
    protected AbstractTransformer getApplicator(final long projection) {
        int index = 0;
        for (final int i : activeColumns) {
            if ((projection & (1L << i)) == 0) {
                index++;
            }
        }
        if (index == 0) { throw new RuntimeException("kkk"); }
        if (index > (instances.length - 1)) {
            return instances[0];
        } else {
            return instances[index];
        }
    }
}
