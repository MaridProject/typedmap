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

package org.marid.typedmap.structlinked;

import org.marid.typedmap.Key;
import org.marid.typedmap.TypedIMMap;

/**
 * @author Dmitry Ovchinnikov
 */
interface LinkedStruct<K extends Key<K, V>, V> extends Iterable<TypedIMMap.Entry<K, V>> {

    LinkedStruct<K, V> getNext();

    boolean containsKey(K key);

    boolean containsValue(V value);
}
