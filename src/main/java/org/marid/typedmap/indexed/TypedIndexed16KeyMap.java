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

package org.marid.typedmap.indexed;

import org.marid.typedmap.Key;
import org.marid.typedmap.KeyDomain;
import org.marid.typedmap.TypedMutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedIndexed16KeyMap<D extends KeyDomain, V> implements TypedMutableMap<D, V> {

    private long s1;
    private long s2;

    private V v0;
    private V v1;
    private V v2;
    private V v3;
    private V v4;
    private V v5;
    private V v6;
    private V v7;

    private V v8;
    private V v9;
    private V v10;
    private V v11;
    private V v12;
    private V v13;
    private V v14;
    private V v15;

    TypedIndexed16KeyMap<D, V> next;

    public TypedIndexed16KeyMap() {
    }

    private TypedIndexed16KeyMap(Key<?, ?> key, V val) {
        s1 = key.getOrder();
        v0 = val;
    }

    @Override
    public boolean containsKey(@Nonnull Key<? extends D, V> key) {
        final int order = key.getOrder();
        for (TypedIndexed16KeyMap<D, V> m = this; m != null; m = m.next) {
            final int index = find(order, m.s1, m.s2, m.size());
            if (index >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return 16 - Long.numberOfLeadingZeros(s1) / 8 - Long.numberOfLeadingZeros(s2) / 8;
    }

    @Override
    public boolean isEmpty() {
        return s1 == 0 && s2 == 0;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <VAL extends V> VAL get(@Nonnull Key<? extends D, VAL> key) {
        final int order = key.getOrder();
        for (TypedIndexed16KeyMap<D, V> m = this; m != null; m = m.next) {
            final int index = find(order, m.s1, m.s2, m.size());
            if (index >= 0) {
                return (VAL) m.getValue(index);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public <VAL extends V> VAL put(@Nonnull Key<? extends D, VAL> key, @Nullable VAL value) {
        final int order = key.getOrder();
        for (TypedIndexed16KeyMap<D, V> m = this; ; m = m.next) {
            final int n = m.size();
            final long v1 = m.s1, v2 = m.s2;
            final int index = find(order, v1, v2, n);
            if (index >= 0) {
                return m.setValue(index, key, value);
            } else if (n < 16) {
                final int pos = -(index + 1);
                for (int i = n; i > pos; i--) {
                    m.setValue(i, key(v1, v2, i - 1), m.getValue(i - 1));
                }
                m.setValue(pos, order, value);
                return null;
            } else if (m.next == null) {
                m.next = new TypedIndexed16KeyMap<>(key, value);
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
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
            case 8: return v8;
            case 9: return v9;
            case 10: return v10;
            case 11: return v11;
            case 12: return v12;
            case 13: return v13;
            case 14: return v14;
            case 15: return v15;
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
            case 8: v8 = value; break;
            case 9: v9 = value; break;
            case 10: v10 = value; break;
            case 11: v11 = value; break;
            case 12: v12 = value; break;
            case 13: v13 = value; break;
            case 14: v14 = value; break;
            case 15: v15 = value; break;
            default: throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        updateState(index, key);
    }

    private void updateState(int index, int key) {
        if (index < 8) {
            final int offset = index * 8;
            s1 = (s1 & ~(0xFFL << offset)) | ((long) key << offset);
        } else {
            final int offset = (index - 8) * 8;
            s2 = (s2 & ~(0xFFL << offset)) | ((long) key << offset);
        }
    }

    @SuppressWarnings("unchecked")
    private <VAL extends V> VAL setValue(int index, Key<? extends D, VAL> key, VAL value) {
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
            case 8: old = v8; v8 = value; break;
            case 9: old = v9; v9 = value; break;
            case 10: old = v10; v10 = value; break;
            case 11: old = v11; v11 = value; break;
            case 12: old = v12; v12 = value; break;
            case 13: old = v13; v13 = value; break;
            case 14: old = v14; v14 = value; break;
            case 15: old = v15; v15 = value; break;
            default: throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        updateState(index, key.getOrder());
        return (VAL) old;
    }

    private static int key(long v1, long v2, int index) {
        if (index < 8) {
            final int offset = 8 * index;
            return (int) ((v1 & (0xFFL << offset)) >>> offset);
        } else {
            final int offset = 8 * (index - 8);
            return (int) ((v2 & (0xFFL << offset)) >>> offset);
        }
    }

    private static int find(int key, long v1, long v2, int size) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final int midVal = key(v1, v2, mid);

            if (midVal < key) low = mid + 1;
            else if (midVal > key) high = mid - 1;
            else return mid;
        }
        return -(low + 1);
    }
}
