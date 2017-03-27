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

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import javafx.util.Pair;
import org.marid.typedmap.linked.TypedLinkedMMMap;
import org.marid.typedmap.wrapped.TypedWrappedMMMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.synchronizedMap;
import static java.util.Comparator.reverseOrder;

/**
 * @author Dmitry Ovchinnikov
 */
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@OperationsPerInvocation(100_000)
@Warmup(iterations = 5, batchSize = 100_000)
@Measurement(iterations = 5, batchSize = 100_000)
@Threads(Threads.MAX)
@Fork(value = 1, jvmArgs = {"-XX:+UseG1GC"})
public class TypedMapBenchmark {

    private static final float LOSS_RATIO = 0.1f;

    @Benchmark
    public TypedMIMap<TestKey<Integer>, Integer> put(ThreadState state) {
        final TypedMIMap<TestKey<Integer>, Integer> map = state.supplier.get();
        for (final Pair<TestKey<Integer>, Integer> pair : state.pairs) {
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

    @Benchmark
    public int get(ThreadState state) {
        final TypedMIMap<TestKey<Integer>, Integer> map = state.map;
        int result = 0;
        for (final TestKey<Integer> key : state.allKeys) {
            result ^= Objects.hashCode(map.get(key));
        }
        return result;
    }

    @State(Scope.Thread)
    public static class ThreadState {

        private final List<TestKey<Integer>> allKeys = new ArrayList<>();
        private final List<TestKey<Integer>> keys = new ArrayList<>();
        private final List<Pair<TestKey<Integer>, Integer>> pairs = new ArrayList<>();

        @Param({"5", "54", "180"})
        private int size;

        @Param({"linkedMI", "hash", "fu", "fuSync"})
        private String type;

        private Supplier<TypedMIMap<TestKey<Integer>, Integer>> supplier;
        private TypedMIMap<TestKey<Integer>, Integer> map;

        @Setup
        public void init() {
            final Random random = new Random(0L);

            for (int i = 0; i < size; i++) {
                final TestKey<Integer> key = new TestKey<>();
                allKeys.add(key);
                keys.add(key);
            }

            final List<Integer> indices = IntStream.range(0, size).boxed().collect(Collectors.toList());
            Collections.shuffle(indices, random);

            final int loss = (int) (LOSS_RATIO * size);
            indices.stream().limit(loss).sorted(reverseOrder()).forEachOrdered(i -> keys.remove(i.intValue()));

            keys.forEach(k -> pairs.add(new Pair<>(k, random.nextInt())));
            IntStream.generate(() -> random.nextInt(keys.size()))
                    .limit(loss)
                    .forEach(i -> pairs.add(new Pair<>(keys.get(i), random.nextInt())));
            Collections.shuffle(pairs, random);

            switch (type) {
                case "linkedMI":
                    supplier = TypedLinkedMMMap::new;
                    break;
                case "hash":
                    supplier = () -> new TypedWrappedMMMap<>(new HashMap<TestKey<Integer>, Integer>());
                    break;
                case "fu":
                    supplier = () -> new TypedWrappedMMMap<>(
                            new Object2ObjectOpenHashMap<TestKey<Integer>, Integer>()
                    );
                    break;
                case "fuSync":
                    supplier = () -> new TypedWrappedMMMap<>(
                            synchronizedMap(new Object2ObjectOpenHashMap<TestKey<Integer>, Integer>())
                    );
                    break;
                default:
                    throw new IllegalArgumentException(type);
            }
            map = supplier.get();
            keys.forEach(k -> map.put(k, random.nextInt()));
        }
    }

    public static void main(String... args) throws Exception {
        new Runner(new OptionsBuilder()
                .include(TypedMapBenchmark.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .build()
        ).run();
    }
}
