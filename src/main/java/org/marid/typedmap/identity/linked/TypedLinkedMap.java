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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedLinkedMap<D extends KeyDomain, K extends Key<K, ? super D, ?>, V> implements TypedMutableMap<D, K, V> {

    private TypedLinkedMap<D, K, V> next;
    private K key;
    private V value;

    @Override
    public boolean containsKey(@Nonnull K key) {
        for (TypedLinkedMap<D, K, V> m = this; m != null; m = m.next) {
            if (m.key == key) {
                return true;
            } else if (m.key.getOrder() > key.getOrder()) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(@Nonnull V value) {
        for (TypedLinkedMap<D, K, V> m = this; m != null; m = m.next) {
            if (m.value != null && m.value.equals(value)) {
                return true;
            }
        }
        return false;
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
    public <VAL extends V> VAL get(@Nonnull Key<K, ? super D, VAL> key) {
        for (TypedLinkedMap<D, K, V> m = this; m != null; m = m.next) {
            if (m.key == key) {
                return (VAL) m.value;
            } else if (m.key.getOrder() > key.getOrder()) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void forEach(@Nonnull Class<D> domain, @Nonnull BiConsumer<K, V> consumer) {
        for (TypedLinkedMap<D, K, V> m = this; m != null; m = m.next) {
            if (m.key != null) {
                consumer.accept(m.key, m.value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <VAL extends V> VAL put(@Nonnull Key<K, ? super D, VAL> key, @Nullable VAL value) {
        if (this.key == null) {
            this.key = (K) key;
            this.value = value;
            return null;
        } else if (this.key.getOrder() > key.getOrder()) {
            final TypedLinkedMap<D, K, V> map = new TypedLinkedMap<>();
            map.key = this.key;
            map.value = this.value;
            map.next = this.next;
            this.key = key.getKey();
            this.value = value;
            this.next = map;
            return null;
        } else {
            for (TypedLinkedMap<D, K, V> m = this; ; m = m.next) {
                if (m.key == key) {
                    final V old = m.value;
                    m.value = value;
                    return (VAL) old;
                } else if (m.next == null) {
                    final TypedLinkedMap<D, K, V> map = new TypedLinkedMap<>();
                    map.key = (K) key;
                    map.value = value;
                    m.next = map;
                    return null;
                } else if (m.next.key.getOrder() > key.getOrder()) {
                    final TypedLinkedMap<D, K, V> map = new TypedLinkedMap<>();
                    map.key = (K) key;
                    map.value = value;
                    map.next = m.next;
                    m.next = map;
                    return null;
                }
            }
        }
    }

    @Override
    public String toString() {
        final Map<K, V> map = new LinkedHashMap<>();
        for (TypedLinkedMap<D, K, V> m = this; m != null; m = m.next) {
            if (m.key != null) {
                map.put(m.key, m.value);
            }
        }
        return TypedLinkedMap.class.getSimpleName() + map;
    }
}
