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

import org.marid.typedmap.TestKey;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Random;

/**
 * @author Dmitry Ovchinnikov
 */
@State(Scope.Thread)
public class ThreadState {

    final Random random = new Random(0);
    final TestKey[] keys = random.ints().limit(TypedMapGetBenchmark.SIZE).mapToObj(TestKey::new).toArray(TestKey[]::new);
    final Integer[] values = random.ints().limit(TypedMapGetBenchmark.SIZE).boxed().toArray(Integer[]::new);
}
