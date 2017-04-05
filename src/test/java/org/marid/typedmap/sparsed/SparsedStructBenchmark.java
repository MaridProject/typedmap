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

package org.marid.typedmap.sparsed;

import org.marid.typedmap.identity.linked.TypedLinkedMap;
import org.marid.typedmap.limited.TypedByte16KeyMap;
import org.marid.typedmap.limited.TypedByte8KeyMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static org.marid.typedmap.sparsed.RandomSuppliers.RANDOM_SUPPLIERS;

/**
 * @author Dmitry Ovchinnikov
 */
@Warmup(iterations = 5, batchSize = 10_000)
@Measurement(iterations = 5, batchSize = 10_000)
@OperationsPerInvocation(10_000)
@Threads(Threads.MAX)
@Fork(value = 1, jvmArgs = {"-XX:+UseG1GC"})
public class SparsedStructBenchmark {

    private static final float FILL = 0.125f;

    @Benchmark
    public SparsedStruct pojo(ThreadState state) throws Exception {
        final SparsedStruct pojo = new SparsedStruct();
        final int k = state.random.nextInt(state.keys.length);
        for (int i = 0; i < state.size; i++) {
            final Field field = state.keys[k][i].field;
            field.set(pojo, state.values[k][i]);
        }
        return pojo;
    }

    @Benchmark
    public TypedLinkedMap<SparsedKeyDomain, Object> linked(ThreadState state) {
        final TypedLinkedMap<SparsedKeyDomain, Object> map = new TypedLinkedMap<>();
        final int k = state.random.nextInt(state.keys.length);
        for (int i = 0; i < state.size; i++) {
            map.put(state.keys[k][i], state.values[k][i]);
        }
        return map;
    }

    @Benchmark
    public TypedByte8KeyMap<SparsedKeyDomain, Object> byte8(ThreadState state) {
        final TypedByte8KeyMap<SparsedKeyDomain, Object> map = new TypedByte8KeyMap<>();
        final int k = state.random.nextInt(state.keys.length);
        for (int i = 0; i < state.size; i++) {
            map.put(state.keys[k][i], state.values[k][i]);
        }
        return map;
    }

    @Benchmark
    public TypedByte16KeyMap<SparsedKeyDomain, Object> byte16(ThreadState state) {
        final TypedByte16KeyMap<SparsedKeyDomain, Object> map = new TypedByte16KeyMap<>();
        final int k = state.random.nextInt(state.keys.length);
        for (int i = 0; i < state.size; i++) {
            map.put(state.keys[k][i], state.values[k][i]);
        }
        return map;
    }

    @State(Scope.Thread)
    public static class ThreadState {

        private final ThreadLocalRandom random = ThreadLocalRandom.current();
        private final int size = (int) (SparsedKeyDomain.KEYS.size() * FILL);
        private final SparsedKey[][] keys = new SparsedKey[10_000][size];
        private final Object[][] values = new Object[keys.length][size];

        @Setup
        public void init() {
            final List<SparsedKey> allKeys = new ArrayList<>(SparsedKeyDomain.KEYS);
            for (int k = 0; k < keys.length; k++) {
                Collections.shuffle(allKeys, random);
                for (int i = 0; i < size; i++) {
                    keys[k][i] = allKeys.get(i);
                    final Function<ThreadLocalRandom, ?> supplier = RANDOM_SUPPLIERS.get(keys[k][i].field.getType());
                    values[k][i] = supplier.apply(random);
                }
            }
        }
    }

    public static void main(String... args) throws Exception {
        new Runner(new OptionsBuilder()
                .include(SparsedStructBenchmark.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .shouldDoGC(true)
                .build()
        ).run();
    }
}
