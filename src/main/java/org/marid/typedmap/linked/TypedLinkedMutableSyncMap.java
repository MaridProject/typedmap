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
import org.marid.typedmap.TypedMap;
import org.marid.typedmap.TypedMutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

/**
 * @author Dmitry Ovchinnikov
 */
public final class TypedLinkedMutableSyncMap<K extends Key<K, V>, V> implements TypedMutableMap<K,V> {

    private LinkedEntry<K, V> entry;

    public TypedLinkedMutableSyncMap() {
    }

    public TypedLinkedMutableSyncMap(TypedMap<K, V> map) {
        map.forEach(this::put);
    }

    @Override
    public synchronized boolean containsKey(@Nonnull K key) {
        for (LinkedEntry<K, ?> en = entry; en != null; en = en.next) {
            if (en.key == key) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean containsValue(@Nonnull V value) {
        for (LinkedEntry<K, ?> en = entry; en != null; en = en.next) {
            if (en.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized int size() {
        int s = 0;
        for (LinkedEntry<K, ?> e = entry; e != null; e = e.next) {
            s++;
        }
        return s;
    }

    @Override
    public synchronized boolean isEmpty() {
        return entry == null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <KEY extends Key<KEY, VAL>, VAL extends V> VAL get(@Nonnull KEY key) {
        for (LinkedEntry<K, V> e = entry; e != null; e = e.next) {
            if (e.key == key) {
                return (VAL) e.value;
            }
        }
        return key.getDefault();
    }

    @Override
    public synchronized void forEach(BiConsumer<K, V> consumer) {
        for (LinkedEntry<K, V> e = entry; e != null; e = e.next) {
            consumer.accept(e.key, e.value);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public synchronized <KEY extends Key<KEY, VAL>, VAL extends V> VAL put(@Nonnull KEY key, @Nonnull VAL value) {
        for (LinkedEntry<K, V> e = entry; e != null; e = e.next) {
            if (e.key == key) {
                return (VAL) e.setValue(value);
            }
        }
        entry = new LinkedEntry<>(entry, (K) key, value);
        return null;
    }
}
