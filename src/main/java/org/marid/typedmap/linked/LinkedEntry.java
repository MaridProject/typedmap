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

package org.marid.typedmap.linked;

import org.marid.typedmap.Key;
import org.marid.typedmap.TypedIMMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author Dmitry Ovchinnikov
 */
final class LinkedEntry<K extends Key<K, V>, V> implements TypedIMMap.Entry<K, V> {

    private static final AtomicReferenceFieldUpdater<LinkedEntry, Object> F =
            AtomicReferenceFieldUpdater.newUpdater(LinkedEntry.class, Object.class, "value");

    @Nullable
    volatile LinkedEntry<K, V> next;

    @Nonnull
    final K key;

    @Nonnull
    volatile V value;

    LinkedEntry(@Nullable LinkedEntry<K, V> next, @Nonnull K key, @Nonnull V value) {
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

    @SuppressWarnings("unchecked")
    @Override
    public V setValue(V value) {
        return (V) F.getAndSet(this, value);
    }
}
