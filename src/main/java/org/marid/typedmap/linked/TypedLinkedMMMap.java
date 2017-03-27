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
import org.marid.typedmap.TypedIIMap;
import org.marid.typedmap.TypedMMMap;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Dmitry Ovchinnikov
 */
public final class TypedLinkedMMMap<K extends Key<K, V>, V> implements TypedMMMap<K, V> {

    private static final AtomicReferenceFieldUpdater<TypedLinkedMMMap, LinkedEntry> F =
            AtomicReferenceFieldUpdater.newUpdater(TypedLinkedMMMap.class, LinkedEntry.class, "entry");

    private volatile LinkedEntry<K, V> entry;

    public TypedLinkedMMMap() {
    }

    public TypedLinkedMMMap(TypedIIMap<K, V> map) {
        map.forEach(this::put);
    }

    @Override
    public boolean containsKey(@Nonnull K key) {
        for (LinkedEntry<K, ?> en = entry; en != null; en = en.next) {
            if (en.key == key) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(@Nonnull V value) {
        for (LinkedEntry<K, ?> en = entry; en != null; en = en.next) {
            if (en.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        int s = 0;
        for (LinkedEntry<K, ?> e = entry; e != null; e = e.next) {
            s++;
        }
        return s;
    }

    @Override
    public boolean isEmpty() {
        return entry == null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <KEY extends Key<KEY, VAL>, VAL extends V> VAL get(@Nonnull KEY key) {
        for (LinkedEntry<K, V> e = entry; e != null; e = e.next) {
            if (e.key == key) {
                return (VAL) e.value;
            }
        }
        return key.getDefault();
    }

    @Override
    public void forEach(BiConsumer<K, V> consumer) {
        for (LinkedEntry<K, V> e = entry; e != null; e = e.next) {
            consumer.accept(e.key, e.value);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <KEY extends Key<KEY, VAL>, VAL extends V> VAL put(KEY key, VAL value) {
        for (LinkedEntry<K, V> e = entry; e != null; e = e.next) {
            if (e.key == key) {
                return (VAL) e.setValue(value);
            }
        }
        F.updateAndGet(this, e -> new LinkedEntry(e, key, value));
        return null;
    }

    @Override
    public void forEach(Consumer<Entry<K, V>> consumer) {
        for (LinkedEntry<K, V> e = entry; e != null; e = e.next) {
            consumer.accept(e);
        }
    }
}
