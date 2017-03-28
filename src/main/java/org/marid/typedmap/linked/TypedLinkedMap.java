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
import org.marid.typedmap.TypedMutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedLinkedMap<K extends Key<K, V>, V> implements TypedMutableMap<K, V> {

    private TypedLinkedMap<K, V> next;
    private K key;
    private V value;

    @Override
    public boolean containsKey(@Nonnull K key) {
        return key == this.key || next != null && next.containsKey(key);
    }

    @Override
    public boolean containsValue(@Nonnull V value) {
        return this.value != null && this.value.equals(value) || next != null && next.containsValue(value);
    }

    @Override
    public int size() {
        return key == null ? 0 : 1 + (next == null ? 0 : next.size());
    }

    @Override
    public boolean isEmpty() {
        return key == null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <KEY extends Key<KEY, VAL>, VAL extends V> VAL get(@Nonnull KEY key) {
        for (TypedLinkedMap<K, V> m = this; m != null; m = m.next) {
            if (m.key == key) {
                return (VAL) m.value;
            }
        }
        return null;
    }

    @Override
    public void forEach(@Nonnull BiConsumer<K, V> consumer) {
        for (TypedLinkedMap<K, V> m = this; m != null; m = m.next) {
            if (m.key != null) {
                consumer.accept(m.key, m.value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <KEY extends Key<KEY, VAL>, VAL extends V> VAL put(@Nonnull KEY key, @Nullable VAL value) {
        if (this.key == null) {
            this.key = (K) key;
            this.value = value;
        } else {
            for (TypedLinkedMap<K, V> m = this; m != null; m = m.next) {
                if (m.key == key) {
                    final V old = m.value;
                    m.value = value;
                    return (VAL) old;
                }
            }
            final TypedLinkedMap<K, V> map = new TypedLinkedMap<>();
            map.next = next;
            map.key = this.key;
            map.value = this.value;
            this.key = (K) key;
            this.value = value;
            this.next = map;
        }
        return null;
    }
}
