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
import java.util.function.BiConsumer;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedIndexedKeyMap255<D extends KeyDomain, K extends IndexedKey<K, ? super D, ?>, V> implements TypedMutableMap<D, K, V> {

    private volatile long state;

    private V v0;
    private V v1;
    private V v2;
    private V v3;
    private V v4;
    private V v5;
    private V v6;
    private V v7;

    private volatile TypedIndexedKeyMap255<D, K, V> next;

    @Override
    public boolean containsKey(@Nonnull K key) {
        for (TypedIndexedKeyMap255<D, K, V> m = this; m != null; m = m.next) {
            synchronized (m) {
                final int index = find(key.getIndex() + 1, m.state, m.size());
                if (index >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(@Nonnull V value) {
        for (TypedIndexedKeyMap255<D, K, V> m = this; m != null; m = m.next) {
            synchronized (m) {
                final int n = m.size();
                for (int i = 0; i < n; i++) {
                    final V val = getValue(i);
                    if (val.equals(value)) {
                        return true;
                    }
                }
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
    public <VAL extends V> VAL get(@Nonnull Key<K, ? super D, VAL> key) {
        for (TypedIndexedKeyMap255<D, K, V> m = this; m != null; m = m.next) {
            synchronized (m) {
                final int index = find(key.getKey().getIndex() + 1, m.state, m.size());
                if (index >= 0) {
                    return (VAL) getValue(index);
                }
            }
        }
        return null;
    }

    @Override
    public void forEach(@Nonnull Class<D> domain, @Nonnull BiConsumer<K, V> consumer) {
        for (TypedIndexedKeyMap255<D, K, V> m = this; m != null; m = m.next) {
            synchronized (m) {
                final int n = m.size();
                final long v = m.state;
                for (int i = 0; i < n; i++) {
                    final int keyIndex = key(v, i) - 1;
                    final K key = IndexedKey.getKey(domain, keyIndex);
                    if (key != null) {
                        final V val = getValue(i);
                        consumer.accept(key, val);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <VAL extends V> VAL put(@Nonnull Key<K, ? super D, VAL> key, @Nullable VAL value) {
        for (TypedIndexedKeyMap255<D, K, V> m = this; m != null; m = m.next) {
            synchronized (m) {
                final int n = m.size();
                final int index = find(key.getKey().getIndex(), m.state, m.size());
                if (index >= 0) {
                    return (VAL) setValue(index, key.getKey(), value);
                } else if (m.next == null) {
                    if (n < 8) {
                        return (VAL) setValue(n, key.getKey(), value);
                    } else {
                        final TypedIndexedKeyMap255<D, K, V> next = new TypedIndexedKeyMap255<>();
                        next.v0 = value;
                        next.state = key.getKey().getIndex() + 1;
                        m.next = next;
                        return null;
                    }
                }
            }
        }
        return null; // unreachable
    }

    private V getValue(int index) {
        switch (index) {
            case 0:
                return v0;
            case 1:
                return v1;
            case 2:
                return v2;
            case 3:
                return v3;
            case 4:
                return v4;
            case 5:
                return v5;
            case 6:
                return v6;
            case 7:
                return v7;
            default:
                throw new IndexOutOfBoundsException(Integer.toString(index));
        }
    }

    private V setValue(int index, K key, V value) {
        final V old = getValue(index);
        switch (index) {
            case 0:
                v0 = value;
                break;
            case 1:
                v1 = value;
                break;
            case 2:
                v2 = value;
                break;
            case 3:
                v3 = value;
                break;
            case 4:
                v4 = value;
                break;
            case 5:
                v5 = value;
                break;
            case 6:
                v6 = value;
                break;
            case 7:
                v7 = value;
                break;
            default:
                throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        state |= (long) (key.getIndex() + 1) << (index * 8);
        return old;
    }

    private static int key(long v, int index) {
        final int offset = 8 * index;
        return (int) ((v & (0xFF << offset)) >>> offset);
    }

    private static int find(int key, long v, int size) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final int midVal = key(v, mid);

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid;
        }
        return -(low + 1);
    }
}
