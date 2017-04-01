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

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.marid.typedmap.identity.benchmark.ThreadState.SIZE;
import static org.marid.typedmap.identity.benchmark.TypedMapGetBenchmark.KEY_ARRAY_COUNT;

/**
 * @author Dmitry Ovchinnikov
 */
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@OperationsPerInvocation(KEY_ARRAY_COUNT * SIZE)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Threads(Threads.MAX)
@Fork(value = 3, jvmArgs = {"-XX:+UseG1GC"})
public class TypedMapGetBenchmark {

    static final int KEY_ARRAY_COUNT = 1_000;

    @Benchmark
    public int get(GetState getState) {
        int s = 0;
        final TypedMutableMap<TestKeyDomain, TestKey, Integer> map = getState.map;
        for (final TestKey[] testKeys : getState.keys) {
            for (final TestKey testKey : testKeys) {
                s ^= Objects.hashCode(map.get(testKey));
            }
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

        final TestKey[][] keys = new TestKey[KEY_ARRAY_COUNT][SIZE];

        @Param({"linked", "i255", "chash", "fus"})
        private String type;

        TypedMutableMap<TestKeyDomain, TestKey, Integer> map;

        @Setup(Level.Trial)
        public void init(ThreadState state) {
            for (int i = 0; i < KEY_ARRAY_COUNT; i++) {
                System.arraycopy(state.keys, 0, keys[i], 0, keys[i].length);
                ObjectArrays.shuffle(keys[i], ThreadLocalRandom.current());
            }
        }

        @Setup(Level.Invocation)
        public void initMap(ThreadState state) {
            final TestKey[] keys = Arrays.copyOf(state.keys, SIZE);
            map = TypedMapFactory.byType(type).get();
            ObjectArrays.shuffle(keys, ThreadLocalRandom.current());
            IntStream.range(0, keys.length).forEach(i -> map.put(state.keys[i], state.values[i]));
        }
    }
}
