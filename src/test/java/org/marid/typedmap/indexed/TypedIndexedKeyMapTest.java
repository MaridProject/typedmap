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

package org.marid.typedmap.indexed;

import org.marid.typedmap.TestKeyDomain;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

import static org.testng.Assert.assertEquals;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedIndexedKeyMapTest {

    @DataProvider
    public Object[][] indexed8Data() {
        return new Object[][] {
                {0, 1},
                {1, 1},
                {8, 1},
                {9, 2},
                {16, 2},
                {24, 3},
                {32, 4},
                {33, 5}
        };
    }

    @Test(dataProvider = "indexed8Data")
    public void testBatch8Size(int entries, int expectedSize) {
        final TypedIndexed8KeyMap<TestKeyDomain, Integer> map = new TypedIndexed8KeyMap<>();

        IntStream.range(0, entries).forEach(i -> map.put(TestKeyDomain.TEST_KEYS[i], i));

        int actualSize = 0;
        for (TypedIndexed8KeyMap<TestKeyDomain, Integer> m = map; m != null; m = m.next) {
            actualSize++;
        }

        assertEquals(actualSize, expectedSize);
    }

    @DataProvider
    public Object[][] indexed16Data() {
        return new Object[][] {
                {0, 1},
                {1, 1},
                {15, 1},
                {16, 1},
                {24, 2},
                {32, 2},
                {33, 3}
        };
    }

    @Test(dataProvider = "indexed16Data")
    public void testBatch16Size(int entries, int expectedSize) {
        final TypedIndexed16KeyMap<TestKeyDomain, Integer> map = new TypedIndexed16KeyMap<>();

        IntStream.range(0, entries).forEach(i -> map.put(TestKeyDomain.TEST_KEYS[i], i));

        int actualSize = 0;
        for (TypedIndexed16KeyMap<TestKeyDomain, Integer> m = map; m != null; m = m.next) {
            actualSize++;
        }

        assertEquals(actualSize, expectedSize);
    }
}
