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

package org.marid.typedmap.examples;

import org.marid.typedmap.limited.TypedByte8KeyMap;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static org.testng.Assert.assertEquals;

/**
 * @author Dmitry Ovchinnikov
 */
public class SampleKeyMapTest {

    private final TypedByte8KeyMap<AggregatedDomain, Number> map = new TypedByte8KeyMap<>();

    @Test
    public void test1() {
        map.put(Domain1.KEY1, 1);
        map.put(Domain1.KEY3, 2L);
        map.put(Domain2.KEY4, BigInteger.TEN);

        // map.put(Domain3.KEY5, BigInteger.TEN); // must be a compilation error

        assertEquals(map.get(Domain1.KEY1), (Integer) 1);
        assertEquals(map.get(Domain1.KEY3), (Long) 2L);
        assertEquals(map.get(Domain2.KEY4), BigInteger.TEN);
    }
}
