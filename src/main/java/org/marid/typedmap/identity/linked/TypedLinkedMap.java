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

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedLinkedMap<D extends KeyDomain, V> implements TypedMutableMap<D, V> {

    TypedLinkedMap<D, V> next;
    Key<? extends D, ? extends V> key;
    V value;

    @Override
    public boolean containsKey(@Nonnull Key<? extends D, V> key) {
        for (TypedLinkedMap<D, V> m = this; m != null; m = m.next) {
            if (m.key == key) {
                return true;
            } else if (m.key.getOrder() > key.getOrder()) {
                return false;
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
    public <VAL extends V> VAL get(@Nonnull Key<? extends D, VAL> key) {
        for (TypedLinkedMap<D, V> m = this; m != null; m = m.next) {
            if (m.key == key) {
                return (VAL) m.value;
            } else if (m.key.getOrder() > key.getOrder()) {
                return null;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <VAL extends V> VAL put(@Nonnull Key<? extends D, VAL> key, @Nullable VAL value) {
        if (this.key == null) {
            this.key = key;
            this.value = value;
            return null;
        } else if (this.key.getOrder() > key.getOrder()) {
            final TypedLinkedMap<D, V> map = new TypedLinkedMap<>();
            map.key = this.key;
            map.value = this.value;
            map.next = this.next;
            this.key = key;
            this.value = value;
            this.next = map;
            return null;
        } else {
            for (TypedLinkedMap<D, V> m = this; ; m = m.next) {
                if (m.key == key) {
                    final V old = m.value;
                    m.value = value;
                    return (VAL) old;
                } else if (m.next == null) {
                    final TypedLinkedMap<D, V> map = new TypedLinkedMap<>();
                    map.key = key;
                    map.value = value;
                    m.next = map;
                    return null;
                } else if (m.next.key.getOrder() > key.getOrder()) {
                    final TypedLinkedMap<D, V> map = new TypedLinkedMap<>();
                    map.key = key;
                    map.value = value;
                    map.next = m.next;
                    m.next = map;
                    return null;
                }
            }
        }
    }
}
