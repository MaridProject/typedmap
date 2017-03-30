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

package org.marid.typedmap.identity.benchmark;

import it.unimi.dsi.fastutil.objects.ObjectArrays;
import org.marid.typedmap.TestKey;
import org.marid.typedmap.TestKeyDomain;
import org.marid.typedmap.TypedMutableMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.marid.typedmap.identity.benchmark.TypedMapGetBenchmark.SIZE;

/**
 * @author Dmitry Ovchinnikov
 */
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@OperationsPerInvocation(100_000 * SIZE)
@Warmup(iterations = 5, batchSize = 100_000)
@Measurement(iterations = 5, batchSize = 100_000)
@Threads(Threads.MAX)
@Fork(value = 1, jvmArgs = {"-XX:+UseG1GC"})
public class TypedMapGetBenchmark {

    static final int SIZE = 50;

    @Benchmark
    public int get(GetState getState) {
        int s = 0;
        for (int i = 0; i < SIZE; i++) {
            s ^= Objects.hashCode(getState.map.get(getState.keys[i]));
        }
        return s;
    }

    public static void main(String... args) throws Exception {
        new Runner(new OptionsBuilder()
                .include(TypedMapGetBenchmark.class.getSimpleName())
                .build()
        ).run();
    }

    @State(Scope.Thread)
    public static class GetState {

        final TestKey[] keys = new TestKey[SIZE];

        @Param({"linked", "array", "fu", "linkeds", "chash", "fus"})
        private String type;

        TypedMutableMap<TestKeyDomain, TestKey, Integer> map;

        @Setup
        public void init(ThreadState state) {
            System.arraycopy(state.keys, 0, keys, 0, keys.length);
            map = TypedMapFactory.byType(type).get();
            IntStream.range(0, keys.length).forEach(i -> map.put(state.keys[i], state.values[i]));
            ObjectArrays.shuffle(keys, state.random);
        }
    }
}
