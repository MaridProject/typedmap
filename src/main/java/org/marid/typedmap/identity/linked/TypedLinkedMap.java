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

    public TypedLinkedMap() {
    }

    public TypedLinkedMap(Key<? extends D, ? extends V> key, V value) {
        this.key = key;
        this.value = value;
    }

    private TypedLinkedMap(Key<? extends D, ? extends V> key, V value, TypedLinkedMap<D, V> next) {
        this(key, value);
        this.next = next;
    }

    @Override
    public boolean containsKey(@Nonnull Key<? extends D, V> key) {
        if (this.key == null) {
            return false;
        } else {
            for (TypedLinkedMap<D, V> m = this; m != null; m = m.next) {
                if (m.key == key) {
                    return true;
                } else if (m.key.getOrder() > key.getOrder()) {
                    return false;
                }
            }
            return false;
        }
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
        return value == null ? (VAL) remove(key) : (VAL) put0(key, value);
    }

    private V put0(@Nonnull Key<? extends D, ? extends V> key, @Nonnull V value) {
        if (this.key == null) {
            this.key = key;
            this.value = value;
            return null;
        } else if (this.key.getOrder() > key.getOrder()) {
            final TypedLinkedMap<D, V> map = new TypedLinkedMap<>(this.key, this.value, this.next);
            this.key = key;
            this.value = value;
            this.next = map;
            return null;
        } else {
            for (TypedLinkedMap<D, V> m = this; ; m = m.next) {
                if (m.key == key) {
                    final V old = m.value;
                    m.value = value;
                    return old;
                } else if (m.next == null) {
                    m.next = new TypedLinkedMap<>(key, value);
                    return null;
                } else if (m.next.key.getOrder() > key.getOrder()) {
                    m.next = new TypedLinkedMap<>(key, value, m.next);
                    return null;
                }
            }
        }
    }

    private V remove(@Nonnull Key<? extends D, ? extends V> key) {
        if (this.key == null) {
            return null;
        } else if (this.key == key) {
            final V old = value;
            if (next == null) {
                this.key = null;
                this.value = null;
            } else {
                this.key = next.key;
                this.value = next.value;
                this.next = next.next;
            }
            return old;
        } else {
            for (TypedLinkedMap<D, V> m = this; m.next != null; m = m.next) {
                if (m.next.key == key) {
                    final V old = m.next.value;
                    m.next = m.next.next;
                    return old;
                } else if (m.next.key.getOrder() > key.getOrder()) {
                    return null;
                }
            }
            return null;
        }
    }
}
