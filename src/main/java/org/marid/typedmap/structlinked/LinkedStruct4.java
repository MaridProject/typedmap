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

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * @author Dmitry Ovchinnikov
 */
class LinkedStruct4<K extends Key<K, V>, V> implements LinkedStruct<K, V> {

    private final LinkedStruct4<K, V> next;

    K k0;
    K k1;
    K k2;
    K k3;

    V v0;
    V v1;
    V v2;
    V v3;

    LinkedStruct4(LinkedStruct4<K, V> next) {
        this.next = next;
    }

    @Override
    public LinkedStruct<K, V> getNext() {
        return next;
    }

    @Override
    public synchronized boolean containsKey(K key) {
        return key == k0 || key == k1 || key == k2 || key == k3;
    }

    @Override
    public synchronized boolean containsValue(V value) {
        return
                v0 != null && (v0.equals(value) ||
                        v1 != null && (v1.equals(value) ||
                                v2 != null && (v2.equals(value) ||
                                        v3 != null && v3.equals(value)
                                )
                        )
                );
    }

    @Nonnull
    @Override
    public Iterator<TypedIMMap.Entry<K, V>> iterator() {
        final K k0, k1, k2, k3;
        final V v0, v1, v2, v3;
        synchronized (this) {
            k0 = this.k0;
            k1 = this.k1;
            k2 = this.k2;
            k3 = this.k3;

            v0 = this.v0;
            v1 = this.v1;
            v2 = this.v2;
            v3 = this.v3;
        }
        return new Iterator<TypedIMMap.Entry<K, V>>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public TypedIMMap.Entry<K, V> next() {
                return null;
            }
        };
    }
}
