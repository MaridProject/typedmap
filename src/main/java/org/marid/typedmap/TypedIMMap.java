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

/**
 * @author Dmitry Ovchinnikov
 */
public interface TypedIMMap<K extends Key<K, V>, V> extends TypedIIMap<K, V> {

    @Override
    Collection<? extends TypedIMMap.Entry<K, V>> entries();

    interface Entry<K extends Key<K, V>, V> extends TypedIIMap.Entry<K, V> {

        V setValue(V value);
    }
}
