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

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

/**
 * @author Dmitry Ovchinnikov
 */
public interface TypedIIMap<K extends Key<K, V>, V> {

    @Nonnull
    Set<? extends K> keySet();

    @Nonnull
    Collection<? extends V> values();

    boolean containsKey(K key);

    boolean containsValue(V value);

    int size();

    boolean isEmpty();

    <KEY extends Key<KEY, VAL>, VAL extends V> VAL get(@Nonnull KEY key);

    Collection<? extends Entry<K, V>> entries();

    interface Entry<K extends Key<K, V>, V> {

        @Nonnull
        K getKey();

        @Nonnull
        V getValue();
    }
}
