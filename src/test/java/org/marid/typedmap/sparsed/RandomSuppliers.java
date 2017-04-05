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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.util.stream.IntStream.range;

/**
 * @author Dmitry Ovchinnikov
 */
public class RandomSuppliers {

    static final Map<Class<?>, Function<ThreadLocalRandom, ?>> RANDOM_SUPPLIERS = new IdentityHashMap<>();

    static {
        putSupplier(BigDecimal.class, r -> BigDecimal.valueOf(r.nextDouble()));
        putSupplier(BigInteger.class, r -> BigInteger.valueOf(r.nextLong()));
        putSupplier(Integer.class, ThreadLocalRandom::nextInt);
        putSupplier(Long.class, ThreadLocalRandom::nextLong);
        putSupplier(String.class, r -> Long.toOctalString(r.nextLong()));
        putSupplier(byte[].class, r -> Long.toOctalString(r.nextLong()).getBytes(ISO_8859_1));
        putSupplier(int[].class, r -> r.ints(r.nextInt(10)).toArray());
        putSupplier(Character.class, r -> (char) r.nextInt(65536));
        putSupplier(char[].class, r -> Long.toOctalString(r.nextLong()).toCharArray());
        putSupplier(String[].class, r -> range(0, r.nextInt(10)).mapToObj(Integer::toString).toArray(String[]::new));
        putSupplier(Integer[].class, r -> r.ints(r.nextInt(10)).boxed().toArray(Integer[]::new));
        putSupplier(long[].class, r -> r.longs(r.nextInt(10)).toArray());
        putSupplier(Byte.class, r -> (byte) r.nextInt());
    }

    private static <T> void putSupplier(Class<T> type, Function<ThreadLocalRandom, T> supplier) {
        RANDOM_SUPPLIERS.put(type, supplier);
    }
}
