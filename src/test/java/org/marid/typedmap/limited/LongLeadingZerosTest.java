/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.marid.typedmap.limited;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;

/**
 * @author Dmitry Ovchinnikov
 */
public class LongLeadingZerosTest {

    private static int index(long value) {
        return Long.numberOfLeadingZeros(value) / 8;
    }

    @Test
    public void testIndexedFull() {
        final long value = ByteBuffer.allocate(8)
                .putChar(0, '\u0101')
                .putChar(2, '\u0101')
                .putChar(4, '\u0101')
                .putChar(6, '\u0101')
                .getLong();
        assertEquals(index(value), 0);
    }

    @Test
    public void testIndexedEmpty() {
        final long value = ByteBuffer.allocate(8)
                .putChar(0, '\u0000')
                .putChar(2, '\u0000')
                .putChar(4, '\u0000')
                .putChar(6, '\u0000')
                .getLong();
        assertEquals(index(value), 8);
    }

    @Test
    public void testIndexedOne() {
        final long value = ByteBuffer.allocate(8)
                .putChar(0, '\u0001')
                .putChar(2, '\u0101')
                .putChar(4, '\u0101')
                .putChar(6, '\u0101')
                .getLong();
        assertEquals(index(value), 1);
    }

    @DataProvider
    public Object[][] indexTwoData() {
        return Stream.of("0000010101011010", "0000FF0010011000")
                .mapToLong(s -> Long.parseUnsignedLong(s, 16))
                .mapToObj(l -> new Object[] {l})
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "indexTwoData")
    public void testIndexedTwo(long value) {
        assertEquals(index(value), 2);
    }

}
