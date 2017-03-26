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

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Dmitry Ovchinnikov
 */
public interface TypedMap<K extends Key<?>> {

    Set<K> keySet();

    Collection<?> values();

    boolean containsKey(K key);

    boolean containsValue(Object value);

    int size();

    boolean isEmpty();

    <V> V get(K key);

    <V> V getOrDefault(K key, V defaultValue);

    <V> V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction);
}
