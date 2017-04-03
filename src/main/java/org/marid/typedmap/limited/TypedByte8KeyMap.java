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

package org.marid.typedmap.limited;

import org.marid.typedmap.Key;
import org.marid.typedmap.KeyDomain;
import org.marid.typedmap.TypedMutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedByte8KeyMap<D extends KeyDomain, V> implements TypedMutableMap<D, V> {

    private long state;

    private V v0;
    private V v1;
    private V v2;
    private V v3;
    private V v4;
    private V v5;
    private V v6;
    private V v7;

    TypedByte8KeyMap<D, V> next;

    public TypedByte8KeyMap() {
    }

    private TypedByte8KeyMap(int key, V val) {
        state = key;
        v0 = val;
    }

    @Override
    public boolean containsKey(@Nonnull Key<? extends D, V> key) {
        final int order = key.getOrder();
        for (TypedByte8KeyMap<D, V> m = this; m != null; m = m.next) {
            final int index = m.find(order, m.size());
            if (index >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return 8 - Long.numberOfLeadingZeros(state) / 8;
    }

    @Override
    public boolean isEmpty() {
        return state == 0;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <VAL extends V> VAL get(@Nonnull Key<? extends D, VAL> key) {
        final int order = key.getOrder();
        for (TypedByte8KeyMap<D, V> m = this; m != null; m = m.next) {
            final int index = m.find(order, m.size());
            if (index >= 0) {
                return (VAL) m.getValue(index);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <VAL extends V> VAL put(@Nonnull Key<? extends D, VAL> key, @Nullable VAL value) {
        return value == null ? (VAL) remove(key.getOrder()) : (VAL) put0(key.getOrder(), value);
    }

    private V put0(int key, @Nonnull V value) {
        for (TypedByte8KeyMap<D, V> m = this; ; m = m.next) {
            final int n = m.size();
            final int index = m.find(key, n);
            if (index >= 0) {
                return m.getAndSet(index, key, value);
            } else if (n < 8) {
                final int pos = -(index + 1);
                for (int i = n; i > pos; i--) {
                    m.setValue(i, m.key(i - 1), m.getValue(i - 1));
                }
                m.setValue(pos, key, value);
                return null;
            } else if (m.next == null) {
                m.next = new TypedByte8KeyMap<>(key, value);
                return null;
            }
        }
    }

    private V remove(int key) {
        for (TypedByte8KeyMap<D, V> p = null, m = this; m != null; p = m, m = m.next) {
            final int n = m.size();
            final int index = m.find(key, n);
            if (index >= 0) {
                final V old = m.getValue(index);
                for (int i = index + 1; i < n; i++) {
                    m.setValue(i - 1, m.key(i), m.getValue(i));
                }
                m.setValue(n - 1, 0, null);
                final TypedByte8KeyMap<D, V> found = m;
                while (m.next != null) {
                    p = m;
                    m = m.next;
                }
                final int lastSize = m.size();
                if (lastSize == 0) {
                    if (p != null) {
                        p.next = null;
                    }
                    return old;
                }
                final int lastKey = m.key(lastSize - 1);
                final V lastValue = m.getValue(lastSize - 1);
                m.setValue(m.size() - 1, 0, null);
                found.put0(lastKey, lastValue);
                if (m.isEmpty() && p != null) {
                    p.next = null;
                }
                return old;
            }
        }
        return null;
    }

    private V getValue(int index) {
        switch (index) {
            case 0: return v0;
            case 1: return v1;
            case 2: return v2;
            case 3: return v3;
            case 4: return v4;
            case 5: return v5;
            case 6: return v6;
            case 7: return v7;
            default: throw new IndexOutOfBoundsException(Integer.toString(index));
        }
    }

    private void setValue(int index, int key, V value) {
        switch (index) {
            case 0: v0 = value; break;
            case 1: v1 = value; break;
            case 2: v2 = value; break;
            case 3: v3 = value; break;
            case 4: v4 = value; break;
            case 5: v5 = value; break;
            case 6: v6 = value; break;
            case 7: v7 = value; break;
            default: throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        updateState(index, key);
    }

    private void updateState(int index, int key) {
        final int offset = index * 8;
        state = (state & ~(0xFFL << offset)) | ((long) key << offset);
    }

    private V getAndSet(int index, int key, V value) {
        final V old;
        switch (index) {
            case 0: old = v0; v0 = value; break;
            case 1: old = v1; v1 = value; break;
            case 2: old = v2; v2 = value; break;
            case 3: old = v3; v3 = value; break;
            case 4: old = v4; v4 = value; break;
            case 5: old = v5; v5 = value; break;
            case 6: old = v6; v6 = value; break;
            case 7: old = v7; v7 = value; break;
            default: throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        updateState(index, key);
        return old;
    }

    private int key(int index) {
        final int offset = 8 * index;
        return (int) ((state & (0xFFL << offset)) >>> offset);
    }

    private int find(int key, int size) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final int midVal = key(mid);

            if (midVal < key) low = mid + 1;
            else if (midVal > key) high = mid - 1;
            else return mid;
        }
        return -(low + 1);
    }
}
