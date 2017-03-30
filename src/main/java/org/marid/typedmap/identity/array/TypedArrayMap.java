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

package org.marid.typedmap.identity.array;

import org.marid.typedmap.Key;
import org.marid.typedmap.KeyDomain;
import org.marid.typedmap.TypedMutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedArrayMap<D extends KeyDomain, K extends Key<D, V>, V> implements TypedMutableMap<D, K, V> {

    private static final Object[] EMPTY = new Object[0];

    private Object[] keyAndValues = EMPTY;

    @Override
    public boolean containsKey(@Nonnull K key) {
        final Object[] kv = keyAndValues;
        final int n = kv.length;
        for (int i = 0; i < n; i += 2) {
            if (key == kv[i]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(@Nonnull V value) {
        final Object[] kv = keyAndValues;
        final int n = kv.length;
        for (int i = 0; i < n; i += 2) {
            if (kv[i + 1].equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return keyAndValues.length;
    }

    @Override
    public boolean isEmpty() {
        return keyAndValues.length == 0;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <VAL extends V> VAL get(@Nonnull Key<? super D, VAL> key) {
        final Object[] kv = keyAndValues;
        final int n = kv.length;
        for (int i = 0; i < n; i += 2) {
            if (kv[i] == key) {
                return (VAL) kv[i + 1];
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void forEach(@Nonnull BiConsumer<K, V> consumer) {
        final Object[] kv = keyAndValues;
        final int n = kv.length;
        for (int i = 0; i < n; i += 2) {
            consumer.accept((K) kv[i], (V) kv[i + 1]);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <VAL extends V> VAL put(@Nonnull Key<? super D, VAL> key, @Nullable VAL value) {
        final Object[] kv = keyAndValues;
        final int n = kv.length;
        for (int i = 0; i < n; i += 2) {
            if (kv[i] == key) {
                final Object old = kv[i + 1];
                kv[i + 1] = value;
                return (VAL) old;
            }
        }
        final Object[] nkv = new Object[kv.length + 2];
        System.arraycopy(kv, 0, nkv, 2, n);
        nkv[0] = key;
        nkv[1] = value;
        keyAndValues = nkv;
        return null;
    }
}
