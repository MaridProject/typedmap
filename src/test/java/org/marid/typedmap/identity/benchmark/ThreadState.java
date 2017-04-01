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
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Dmitry Ovchinnikov
 */
@State(Scope.Thread)
public class ThreadState {

    static final int SIZE = 99;

    final TestKey[] keys = Arrays.copyOf(TestKeyDomain.TEST_KEYS, SIZE);
    final Integer[] values = ThreadLocalRandom.current().ints().limit(SIZE).boxed().toArray(Integer[]::new);

    public ThreadState() {
        ObjectArrays.shuffle(keys, ThreadLocalRandom.current());
    }
}
