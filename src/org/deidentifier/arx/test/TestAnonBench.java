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

import java.util.Arrays;
import java.util.Collection;

import org.deidentifier.arx.BenchmarkSetup.BenchmarkCriterion;
import org.deidentifier.arx.BenchmarkSetup.BenchmarkDataset;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for anonbench
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
@RunWith(Parameterized.class)
public class TestAnonBench extends TestAbstract {

    @Parameters(name = "{index}:[{0}]")
    public static Collection<Object[]> cases() {
        return Arrays.asList(new Object[][] {
            {new TestConfiguration(BenchmarkDataset.ADULT, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY}, "5.5170356E7", new int[]{1, 1, 1, 1, 3, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ADULT, new BenchmarkCriterion[]{BenchmarkCriterion.L_DIVERSITY}, "5.5170356E7", new int[]{1, 1, 1, 1, 3, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ADULT, new BenchmarkCriterion[]{BenchmarkCriterion.T_CLOSENESS}, "2.90180796E8", new int[]{0, 4, 1, 1, 3, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ADULT, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE}, "1613190.0", new int[]{1, 4, 1, 1, 1, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ADULT, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.L_DIVERSITY}, "5.5170356E7", new int[]{1, 1, 1, 1, 3, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ADULT, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.T_CLOSENESS}, "2.90180796E8", new int[]{0, 4, 1, 1, 3, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ADULT, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE}, "1104390.0", new int[]{0, 2, 1, 2, 3, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ADULT, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.L_DIVERSITY}, "1104390.0", new int[]{0, 2, 1, 2, 3, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ADULT, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.T_CLOSENESS}, "4565056.0", new int[]{1, 4, 1, 1, 3, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ADULT, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.L_DIVERSITY}, "1104390.0", new int[]{0, 2, 1, 2, 3, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ADULT, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.T_CLOSENESS}, "4565056.0", new int[]{1, 4, 1, 1, 3, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.CUP, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY}, "3.01506905E8", new int[]{4, 4, 1, 1, 1, 4, 4})},
            {new TestConfiguration(BenchmarkDataset.CUP, new BenchmarkCriterion[]{BenchmarkCriterion.L_DIVERSITY}, "9.2264547E7", new int[]{4, 4, 1, 0, 1, 4, 4})},
            {new TestConfiguration(BenchmarkDataset.CUP, new BenchmarkCriterion[]{BenchmarkCriterion.T_CLOSENESS}, "5.88189923E8", new int[]{4, 4, 1, 2, 1, 4, 4})},
            {new TestConfiguration(BenchmarkDataset.CUP, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE}, "6145304.0", new int[]{5, 4, 1, 0, 1, 4, 4})},
            {new TestConfiguration(BenchmarkDataset.CUP, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.L_DIVERSITY}, "3.01506905E8", new int[]{4, 4, 1, 1, 1, 4, 4})},
            {new TestConfiguration(BenchmarkDataset.CUP, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.T_CLOSENESS}, "5.88189923E8", new int[]{4, 4, 1, 2, 1, 4, 4})},
            {new TestConfiguration(BenchmarkDataset.CUP, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE}, "6145304.0", new int[]{5, 4, 1, 0, 1, 3, 4})},
            {new TestConfiguration(BenchmarkDataset.CUP, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.L_DIVERSITY}, "6025154.0", new int[]{5, 4, 1, 0, 1, 2, 4})},
            {new TestConfiguration(BenchmarkDataset.CUP, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.T_CLOSENESS}, "6145304.0", new int[]{5, 4, 1, 0, 1, 4, 4})},
            {new TestConfiguration(BenchmarkDataset.CUP, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.L_DIVERSITY}, "6145304.0", new int[]{5, 4, 1, 0, 1, 3, 4})},
            {new TestConfiguration(BenchmarkDataset.CUP, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.T_CLOSENESS}, "6145304.0", new int[]{5, 4, 1, 0, 1, 3, 4})},
            {new TestConfiguration(BenchmarkDataset.FARS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY}, "1.63184963E8", new int[]{0, 2, 3, 3, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.FARS, new BenchmarkCriterion[]{BenchmarkCriterion.L_DIVERSITY}, "1.63184963E8", new int[]{0, 2, 3, 3, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.FARS, new BenchmarkCriterion[]{BenchmarkCriterion.T_CLOSENESS}, "7.80794309E8", new int[]{1, 2, 3, 3, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.FARS, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE}, "7819409.0", new int[]{1, 2, 3, 3, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.FARS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.L_DIVERSITY}, "1.63184963E8", new int[]{0, 2, 3, 3, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.FARS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.T_CLOSENESS}, "7.80794309E8", new int[]{1, 2, 3, 3, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.FARS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE}, "7819409.0", new int[]{1, 2, 3, 3, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.FARS, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.L_DIVERSITY}, "7819409.0", new int[]{1, 2, 3, 3, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.FARS, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.T_CLOSENESS}, "1.4456183E7", new int[]{2, 2, 3, 3, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.FARS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.L_DIVERSITY}, "7819409.0", new int[]{1, 2, 3, 3, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.FARS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.T_CLOSENESS}, "1.4456183E7", new int[]{2, 2, 3, 3, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.ATUS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY}, "5.695103509E9", new int[]{0, 5, 0, 2, 1, 1, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ATUS, new BenchmarkCriterion[]{BenchmarkCriterion.L_DIVERSITY}, "2.1209722019E10", new int[]{0, 4, 0, 2, 2, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ATUS, new BenchmarkCriterion[]{BenchmarkCriterion.T_CLOSENESS}, "2.8735900831E10", new int[]{0, 5, 0, 2, 2, 2, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.ATUS, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE}, "6.8886459E7", new int[]{0, 5, 0, 2, 1, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.ATUS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.L_DIVERSITY}, "2.1209722019E10", new int[]{0, 4, 0, 2, 2, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ATUS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.T_CLOSENESS}, "2.8735900831E10", new int[]{0, 5, 0, 2, 2, 2, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.ATUS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE}, "6.8886459E7", new int[]{0, 5, 0, 2, 1, 1, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.ATUS, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.L_DIVERSITY}, "2.36822429E8", new int[]{0, 5, 0, 2, 2, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ATUS, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.T_CLOSENESS}, "2.89696211E8", new int[]{0, 5, 0, 2, 2, 2, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.ATUS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.L_DIVERSITY}, "2.36822429E8", new int[]{0, 5, 0, 2, 2, 2, 2, 1})},
            {new TestConfiguration(BenchmarkDataset.ATUS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.T_CLOSENESS}, "2.89696211E8", new int[]{0, 5, 0, 2, 2, 2, 2, 2})},
            {new TestConfiguration(BenchmarkDataset.IHIS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY}, "1.88718942E8", new int[]{0, 0, 2, 3, 0, 2, 0, 1})},
            {new TestConfiguration(BenchmarkDataset.IHIS, new BenchmarkCriterion[]{BenchmarkCriterion.L_DIVERSITY}, "4.99428552E8", new int[]{0, 0, 0, 3, 2, 2, 0, 1})},
            {new TestConfiguration(BenchmarkDataset.IHIS, new BenchmarkCriterion[]{BenchmarkCriterion.T_CLOSENESS}, "3.823303854E9", new int[]{0, 0, 0, 3, 4, 2, 0, 1})},
            {new TestConfiguration(BenchmarkDataset.IHIS, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE}, "1.3618414E7", new int[]{0, 0, 0, 3, 4, 1, 0, 1})},
            {new TestConfiguration(BenchmarkDataset.IHIS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.L_DIVERSITY}, "4.99428552E8", new int[]{0, 0, 0, 3, 2, 2, 0, 1})},
            {new TestConfiguration(BenchmarkDataset.IHIS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.T_CLOSENESS}, "3.823303854E9", new int[]{0, 0, 0, 3, 4, 2, 0, 1})},
            {new TestConfiguration(BenchmarkDataset.IHIS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE}, "1.3618414E7", new int[]{0, 0, 0, 3, 4, 1, 0, 1})},
            {new TestConfiguration(BenchmarkDataset.IHIS, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.L_DIVERSITY}, "1.3618414E7", new int[]{0, 0, 0, 3, 4, 1, 0, 1})},
            {new TestConfiguration(BenchmarkDataset.IHIS, new BenchmarkCriterion[]{BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.T_CLOSENESS}, "7.4736838E7", new int[]{0, 0, 1, 3, 4, 2, 0, 1})},
            {new TestConfiguration(BenchmarkDataset.IHIS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.L_DIVERSITY}, "1.3618414E7", new int[]{0, 0, 0, 3, 4, 1, 0, 1})},
            {new TestConfiguration(BenchmarkDataset.IHIS, new BenchmarkCriterion[]{BenchmarkCriterion.K_ANONYMITY, BenchmarkCriterion.D_PRESENCE, BenchmarkCriterion.T_CLOSENESS}, "7.4736838E7", new int[]{0, 0, 1, 3, 4, 2, 0, 1})},
        });
    }

    public TestAnonBench(final TestConfiguration testCase) {
        super(testCase);
    }

}
