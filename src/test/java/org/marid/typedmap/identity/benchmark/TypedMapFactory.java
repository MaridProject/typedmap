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

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.marid.typedmap.TestKeyDomain;
import org.marid.typedmap.TypedMutableMap;
import org.marid.typedmap.identity.linked.TypedLinkedSyncMap;
import org.marid.typedmap.identity.wrapped.TypedWrappedMap;
import org.marid.typedmap.indexed.TypedIndexed16KeySyncMap;
import org.marid.typedmap.indexed.TypedIndexed8KeySyncMap;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author Dmitry Ovchinnikov
 */
interface TypedMapFactory {

    static Supplier<TypedMutableMap<TestKeyDomain, Integer>> byType(String type) {
        switch (type) {
            case "linked":
                return TypedLinkedSyncMap::new;
            case "chash":
                return () -> new TypedWrappedMap<>(new ConcurrentHashMap<>());
            case "fus":
                return (() -> new TypedWrappedMap<>(Collections.synchronizedMap(new Object2ObjectOpenHashMap<>())));
            case "i8":
                return TypedIndexed8KeySyncMap::new;
            case "i16":
                return TypedIndexed16KeySyncMap::new;
            default:
                throw new IllegalArgumentException(type);
        }
    }
}
