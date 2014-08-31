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
import org.deidentifier.arx.framework.data.DataManager;
import org.deidentifier.arx.metric.Metric;

/**
 * This class implements a node checker that can operate on the subset of
 * the quasi-identifiers
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class IncognitoNodeChecker extends org.deidentifier.arx.framework.check.NodeChecker {

    /**
     * Instantiates a new node checker incognito.
     * 
     * @param manager the manager
     * @param metric the metric
     * @param config the configuration
     * @param historyMaxSize the history max size
     * @param historyThreshold the history threshold
     */
    public IncognitoNodeChecker(final DataManager manager,
                                final Metric<?> metric,
                                final ARXConfiguration config,
                                final int historyMaxSize,
                                final double historyThreshold,
                                final double snapshotSizeSnapshot) {
        
        // Init super-class
        super(manager,
              metric,
              config,
              historyMaxSize,
              historyThreshold,
              snapshotSizeSnapshot);

        // Create a specialized transformer
        transformer = new IncognitoTransformer(manager.getDataQI().getArray(),
                                               manager.getHierarchies(),
                                               manager.getDataQI().getHeader().length,
                                               manager.getDataSE().getArray(),
                                               config,
                                               history.getDictionarySensValue(),
                                               history.getDictionarySensFreq());

    }

    /**
     * Sets the active columns.
     * 
     * @param subset the new active columns
     */
    public void setActiveColumns(final int[] subset) {
        // Reset
        history.reset();
        stateMachine.reset();
        ((IncognitoTransformer) transformer).updateBufferSize(subset);
    }

    /**
     * Changes the metric used for measuring information loss
     */
    public void setMetric(Metric<?> metric) {
        this.metric = metric;
    }
}
