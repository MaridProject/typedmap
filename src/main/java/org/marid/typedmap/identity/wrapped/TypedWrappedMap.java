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

package org.marid.typedmap.identity.wrapped;

import org.marid.typedmap.Key;
import org.marid.typedmap.KeyDomain;
import org.marid.typedmap.TypedMutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedWrappedMap<D extends KeyDomain, V> implements TypedMutableMap<D, V> {

    private final Map<Key<? super D, ? extends V>, V> delegate;

    public TypedWrappedMap(Map<Key<? super D, ? extends V>, V> delegate) {
        this.delegate = delegate;
    }

    public TypedWrappedMap() {
        this(new HashMap<>());
    }

    @Override
    public boolean containsKey(@Nonnull Key<? super D, V> key) {
        return delegate.containsKey(key);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <VAL extends V> VAL get(@Nonnull Key<? super D, VAL> key) {
        return (VAL) delegate.getOrDefault(key, key.getDefault());
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <VAL extends V> VAL put(@Nonnull Key<? super D, VAL> key, @Nullable VAL value) {
        return value == null
                ? (VAL) delegate.remove(key)
                : (VAL) delegate.put(key, value);
    }
}
