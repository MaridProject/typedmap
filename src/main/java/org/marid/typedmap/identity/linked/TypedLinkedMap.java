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

package org.marid.typedmap.identity.linked;

import org.marid.typedmap.Key;
import org.marid.typedmap.KeyDomain;
import org.marid.typedmap.TypedMutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedLinkedMap<D extends KeyDomain, K extends Key<K, ? super D, ?>, V> implements TypedMutableMap<D, K, V> {

    private TypedLinkedMap<D, K, V> next;
    private K key;
    private V value;

    @Override
    public synchronized boolean containsKey(@Nonnull K key) {
        return key == this.key || next != null && next.containsKey(key);
    }

    @Override
    public synchronized boolean containsValue(@Nonnull V value) {
        return this.value != null && this.value.equals(value) || next != null && next.containsValue(value);
    }

    @Override
    public synchronized int size() {
        return key == null ? 0 : 1 + (next == null ? 0 : next.size());
    }

    @Override
    public synchronized boolean isEmpty() {
        return key == null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public synchronized <VAL extends V> VAL get(@Nonnull Key<K, ? super D, VAL> key) {
        for (TypedLinkedMap<D, K, V> m = this; m != null; m = m.next) {
            if (m.key == key) {
                return (VAL) m.value;
            }
        }
        return null;
    }

    @Override
    public synchronized void forEach(@Nonnull Class<D> domain, @Nonnull BiConsumer<K, V> consumer) {
        for (TypedLinkedMap<D, K, V> m = this; m != null; m = m.next) {
            if (m.key != null) {
                consumer.accept(m.key, m.value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public synchronized <VAL extends V> VAL put(@Nonnull Key<K, ? super D, VAL> key, @Nullable VAL value) {
        for (TypedLinkedMap<D, K, V> m = this; ; m = m.next) {
            if (m.key == null) {
                m.key = (K) key;
                m.value = value;
                return null;
            } else if (m.key == key) {
                final V old = m.value;
                m.value = value;
                return (VAL) old;
            } else if (m.next == null) {
                final TypedLinkedMap<D, K, V> map = new TypedLinkedMap<>();
                map.key = (K) key;
                map.value = value;
                m.next = map;
                return null;
            }
        }
    }
}
