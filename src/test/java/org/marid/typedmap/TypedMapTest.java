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

import org.apache.commons.math3.util.Pair;
import org.marid.typedmap.TypedIIMap.Entry;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;
import static org.testng.Assert.assertEquals;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedMapTest {

    private static final int ENTRY_COUNT = 10;
    private static final int ENTRY_SET_COUNT = 10;
    private static final List<Supplier<TypedMIMap<TestKey<Integer>, Integer>>> MAP_SUPPLIERS = Arrays.asList(
            LinkedTypedMIMap::new,
            TypedForwardingMMMap::new
    );

    @DataProvider
    public Object[][] setsOfEntries() {
        final List<TestKey<Integer>> keys = IntStream.range(0, ENTRY_SET_COUNT * ENTRY_COUNT)
                .mapToObj(TestKey::new)
                .collect(Collectors.toList());
        return MAP_SUPPLIERS.stream()
                .flatMap(s -> IntStream.range(0, ENTRY_SET_COUNT)
                        .mapToObj(i -> {
                            final Random random = new Random(1000L * i);
                            return IntStream.range(0, ENTRY_COUNT)
                                    .mapToObj(k -> {
                                        final TestKey<Integer> key = keys.get(random.nextInt(keys.size()));
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
    public void test(TypedMIMap<TestKey<Integer>, Integer> map, List<Pair<TestKey<Integer>, Integer>> pairs) {
        final Map<TestKey<Integer>, Integer> expectedMap = pairs.stream()
                .collect(toMap(Pair::getKey, Pair::getValue, (v1, v2) -> v2));

        pairs.forEach(p -> map.put(p.getKey(), p.getValue()));

        final Map<TestKey<Integer>, Integer> actualMap = map.entries().stream()
                .collect(toMap(Entry::getKey, Entry::getValue));

        assertEquals(actualMap, expectedMap);
    }
}
