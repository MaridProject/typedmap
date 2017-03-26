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
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Dmitry Ovchinnikov
 */
final class LinkedEntry<K extends Key<K, V>, V> implements TypedIIMap.Entry<K, V> {

    @Nonnull
    final K key;

    @Nonnull
    final V value;

    @Nullable
    LinkedEntry<K, V> next;

    LinkedEntry(@Nonnull K key, @Nonnull V value, @Nullable LinkedEntry<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    @Nonnull
    @Override
    public K getKey() {
        return key;
    }

    @Nonnull
    @Override
    public V getValue() {
        return value;
    }

    Iterator<LinkedEntry<K, V>> itr() {
        return new Iterator<LinkedEntry<K, V>>() {

            private LinkedEntry<K, V> e = LinkedEntry.this;

            @Override
            public boolean hasNext() {
                return e != null;
            }

            @Override
            public LinkedEntry<K, V> next() {
                final LinkedEntry<K, V> prev = e;
                if (prev == null) {
                    throw new NoSuchElementException();
                } else {
                    e = e.next;
                    return prev;
                }
            }
        };
    }
}
