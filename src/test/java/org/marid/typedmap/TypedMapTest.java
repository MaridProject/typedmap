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

package org.marid.typedmap;

import it.unimi.dsi.fastutil.objects.ObjectArrays;
import org.apache.commons.math3.util.Pair;
import org.marid.typedmap.identity.linked.TypedLinkedMap;
import org.marid.typedmap.identity.wrapped.TypedWrappedMap;
import org.marid.typedmap.limited.TypedByte16KeyMap;
import org.marid.typedmap.limited.TypedByte8KeyMap;
import org.marid.typedmap.limited.TypedShort32KeyMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedMapTest {

    private static final int ENTRY_COUNT = 20;
    private static final int ENTRY_SET_COUNT = 10;
    private static final List<Supplier<TypedMutableMap<TestKeyDomain, Integer>>> MAP_SUPPLIERS = Arrays.asList(
            TypedWrappedMap::new,
            TypedLinkedMap::new,
            TypedByte8KeyMap::new,
            TypedByte16KeyMap::new,
            TypedShort32KeyMap::new
    );

    @DataProvider
    public Object[][] setsOfEntries() {
        final TestKey[] keys = Arrays.copyOf(TestKeyDomain.TEST_KEYS, ENTRY_COUNT * ENTRY_SET_COUNT);
        return MAP_SUPPLIERS.stream()
                .flatMap(s -> IntStream.range(0, ENTRY_SET_COUNT)
                        .mapToObj(i -> {
                            final Random random = new Random(1000L * i);
                            return IntStream.range(0, ENTRY_COUNT)
                                    .mapToObj(k -> {
                                        final TestKey key = keys[random.nextInt(keys.length)];
                                        final Integer value = random.nextInt();
                                        return new Pair<>(key, value);
                                    })
                                    .collect(Collectors.toList());
                        })
                        .map(l -> new Object[] {s.get(), l})
                )
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "setsOfEntries")
    public void testPut(TypedMutableMap<TestKeyDomain, Integer> map, List<Pair<TestKey, Integer>> pairs) {
        final Map<TestKey, Integer> expectedMap = new TreeMap<>(Comparator.comparingInt(TestKey::getOrder));

        pairs.forEach(p -> {
            expectedMap.put(p.getKey(), p.getValue());
            map.put(p.getKey(), p.getValue());
        });

        final Map<TestKey, Integer> actualMap = Stream.of(TestKeyDomain.TEST_KEYS)
                .filter(map::containsKey)
                .map(k -> new Pair<>(k, map.get(k)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        assertEquals(actualMap, expectedMap);
    }

    @Test(dataProvider = "setsOfEntries")
    public void testRemove(TypedMutableMap<TestKeyDomain, Integer> map, List<Pair<TestKey, Integer>> pairs) {
        final Map<TestKey, Integer> expectedMap = new TreeMap<>(Comparator.comparingInt(TestKey::getOrder));

        pairs.forEach(p -> {
            expectedMap.put(p.getKey(), p.getValue());
            map.put(p.getKey(), p.getValue());
        });

        final TestKey[] keys = TestKeyDomain.TEST_KEYS.clone();
        ObjectArrays.shuffle(keys, new Random(0L));

        for (final TestKey key : keys) {
            try {
                expectedMap.remove(key);
                map.put(key, null);

                final TreeMap<TestKey, Integer> actualMap = new TreeMap<>(Comparator.comparingInt(TestKey::getOrder));
                for (final TestKey k : TestKeyDomain.TEST_KEYS) {
                    if (map.containsKey(k) && map.get(k) != null) {
                        actualMap.put(k, map.get(k));
                    }
                }

                assertEquals(actualMap, expectedMap);
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }
}
