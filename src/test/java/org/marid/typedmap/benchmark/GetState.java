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

import it.unimi.dsi.fastutil.objects.ObjectArrays;
import org.marid.typedmap.TestKey;
import org.marid.typedmap.TypedMutableMap;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.stream.IntStream;

/**
 * @author Dmitry Ovchinnikov
 */
@State(Scope.Thread)
public class GetState {

    final TestKey[] keys = new TestKey[TypedMapGetBenchmark.SIZE];

    @Param({"linked", "fu", "linkeds", "chash", "fus"})
    private String type;

    TypedMutableMap<TestKey, Integer> map;

    @Setup
    public void init(ThreadState state) {
        System.arraycopy(state.keys, 0, keys, 0, keys.length);
        map = TypedMapFactory.byType(type).get();
        IntStream.range(0, keys.length).forEach(i -> map.put(state.keys[i], state.values[i]));
        ObjectArrays.shuffle(keys, state.random);
    }
}
