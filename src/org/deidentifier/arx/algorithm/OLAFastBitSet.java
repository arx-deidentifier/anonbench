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

/**
 * This class implements a bitset
 * 
 * @author Fabian Prasser
 */
public class OLAFastBitSet {

    /** The Constant ADDRESS_BITS_PER_UNIT. */
    private static final int ADDRESS_BITS_PER_UNIT = 3;

    /** The Constant BIT_INDEX_MASK. */
    private static final int BIT_INDEX_MASK        = 7;

    /** The Constant NUM_CHUNKS. */
    private static final int NUM_CHUNKS            = 10;

    /** The chunks. */
    private final byte[][]   chunks;

    /** The chunk size in bytes. */
    private final int        chunkSizeInBytes;

    /** The current chunk index. */
    private int              currentChunkIndex;

    /** The modulo mask. */
    private final int        moduloMask;

    /** The precalculated mask. */
    private final int        mask;

    /** The shift value. */
    private final int        shiftValue;

    /**
     * Instantiates a new bit set
     * 
     * @param size the size
     */
    public OLAFastBitSet(final int size) {
        
        currentChunkIndex = -1;

        // Calculate next power of 2
        final int sizeinBits = ((size + NUM_CHUNKS) - 1) / NUM_CHUNKS;
        int chunkSizeTemp = 1;
        while (chunkSizeTemp < sizeinBits) {
            chunkSizeTemp <<= 1;
        }
        
        // Initialize structures
        final int chunkSizeInBits = chunkSizeTemp;
        chunkSizeInBytes = Math.max(chunkSizeTemp / 8, 1);
        shiftValue = (32 - Integer.numberOfLeadingZeros(chunkSizeInBits - 1));
        final int newNumChunks = ((size + chunkSizeInBits) - 1) / chunkSizeInBits;
        chunks = new byte[newNumChunks][];
        moduloMask = chunkSizeInBits - 1;
        mask = BIT_INDEX_MASK & moduloMask;
    }

    /**
     * Gets the bit
     * 
     * @param bit the bit
     * @return true, if successful
     */
    public final boolean get(final int bit) {

        currentChunkIndex = bit >> shiftValue;
        if (chunks[currentChunkIndex] == null) { return false; }
        return ((chunks[currentChunkIndex][(bit & moduloMask) >> ADDRESS_BITS_PER_UNIT] & (1 << (bit & mask))) != 0);
    }

    /**
     * Sets a bit
     * 
     * @param bit the bit
     */
    public final void set(final int bit) {

        currentChunkIndex = bit >> shiftValue;
        if (chunks[currentChunkIndex] == null) chunks[currentChunkIndex] = new byte[chunkSizeInBytes];
        chunks[currentChunkIndex][(bit & moduloMask) >> ADDRESS_BITS_PER_UNIT] |= 1 << (bit & mask);
    }
}
