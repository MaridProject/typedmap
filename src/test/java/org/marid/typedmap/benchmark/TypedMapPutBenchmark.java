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

package org.marid.typedmap.benchmark;

import org.marid.typedmap.TestKey;
import org.marid.typedmap.TypedMutableMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import static org.marid.typedmap.benchmark.TypedMapPutBenchmark.SIZE;

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
public class TypedMapPutBenchmark {

    static final int SIZE = 50;

    @Benchmark
    public TypedMutableMap<TestKey, Integer> put(ThreadState state, PutState putState) {
        final TypedMutableMap<TestKey, Integer> map = putState.supplier.get();
        for (int i = 0; i < SIZE; i++) {
            map.put(state.keys[i], state.values[i]);
        }
        return map;
    }

    public static void main(String... args) throws Exception {
        new Runner(new OptionsBuilder()
                .include(TypedMapPutBenchmark.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .shouldDoGC(true)
                .build()
        ).run();
    }
}
