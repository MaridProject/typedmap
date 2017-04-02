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

package org.marid.typedmap.identity;

import it.unimi.dsi.fastutil.objects.ObjectArrays;
import org.marid.typedmap.TestKey;
import org.marid.typedmap.TestKeyDomain;
import org.marid.typedmap.identity.linked.TypedLinkedMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static org.marid.typedmap.TestKeyDomain.TEST_KEYS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedLinkedMapTest {

    @DataProvider
    public Object[][] orderData() {
        final Random random = new Random(0L);
        return IntStream.range(0, 1000)
                .mapToObj(i -> Arrays.copyOf(TEST_KEYS, random.nextInt(TEST_KEYS.length + 1)))
                .map(a -> ObjectArrays.shuffle(a, random))
                .map(a -> new Object[] {random, a})
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "orderData")
    public void testOrder(Random random, TestKey[] testKeys) {
        final TypedLinkedMap<TestKeyDomain, TestKey, Integer> map = new TypedLinkedMap<>();
        for (final TestKey testKey : testKeys) {
            assertNull(map.put(testKey, random.nextInt()));
        }

        final List<TestKey> keys = new ArrayList<>();
        map.forEach(TestKeyDomain.class, (k, v) -> keys.add(k));

        final TestKey[] actual = keys.toArray(new TestKey[keys.size()]);
        final TestKey[] expected = Stream.of(testKeys).sorted(comparingInt(TestKey::getOrder)).toArray(TestKey[]::new);

        assertEquals(actual, expected);
    }
}
