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

package org.deidentifier.arx.test;

import org.deidentifier.arx.BenchmarkSetup.BenchmarkCriterion;
import org.deidentifier.arx.BenchmarkSetup.BenchmarkDataset;

/**
 * Represents a test case
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class TestConfiguration {

    public BenchmarkDataset     dataset;
    public BenchmarkCriterion[] criteria;
    public String               informationLoss;
    public int[]                transformation;

    
    /**
     * Creates a new instance
     * 
     * @param dataset
     * @param criteria
     * @param algorithm
     * @param informationLoss
     * @param transformation
     */
    public TestConfiguration(BenchmarkDataset dataset,
                             BenchmarkCriterion[] criteria,
                             String informationLoss,
                             int[] transformation) {
        this.dataset = dataset;
        this.criteria = criteria;
        this.informationLoss = informationLoss;
        this.transformation = transformation;
    }

    @Override
    public String toString() {
        
        StringBuilder builder = new StringBuilder();
        builder.append("TestConfiguration(");
        builder.append("BenchmarkDataset.").append(dataset.name());
        builder.append(", new BenchmarkCriterion[]{");
        for (int i=0; i<criteria.length; i++) {
            builder.append("BenchmarkCriterion.").append(criteria[i].name());
            if (i<criteria.length-1) builder.append(", ");
        }
        builder.append("}");
        builder.append(", \"").append(informationLoss).append("\"");
        builder.append(", new int[]{");
        for (int i=0; i<transformation.length; i++) {
            builder.append(transformation[i]);
            if (i<transformation.length-1) builder.append(", ");
        }
        builder.append("})");
        return builder.toString();
    }
}