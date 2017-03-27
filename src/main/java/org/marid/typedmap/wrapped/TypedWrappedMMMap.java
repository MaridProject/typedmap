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

package org.marid.typedmap.wrapped;

import org.marid.typedmap.Key;
import org.marid.typedmap.TypedIMMap;
import org.marid.typedmap.TypedMMMap;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedWrappedMMMap<K extends Key<K, V>, V> implements TypedMMMap<K,V> {

    private final Map<K, V> delegate;

    public TypedWrappedMMMap(Map<K, V> delegate) {
        this.delegate = delegate;
    }

    public TypedWrappedMMMap() {
        this(new HashMap<>());
    }

    @Nonnull
    @Override
    public Set<? extends K> keySet() {
        return delegate.keySet();
    }

    @Nonnull
    @Override
    public Collection<? extends V> values() {
        return delegate.values();
    }

    @Override
    public boolean containsKey(K key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return delegate.containsValue(value);
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
    @Override
    public <KEY extends Key<KEY, VAL>, VAL extends V> VAL get(@Nonnull KEY key) {
        return (VAL) delegate.getOrDefault(key, key.getDefault());
    }

    @Override
    public Collection<? extends TypedIMMap.Entry<K, V>> entries() {
        return new AbstractCollection<TypedIMMap.Entry<K, V>>() {
            @Nonnull
            @Override
            public Iterator<TypedIMMap.Entry<K, V>> iterator() {
                final Iterator<Map.Entry<K, V>> mapEntries = delegate.entrySet().iterator();
                return new Iterator<TypedIMMap.Entry<K, V>>() {
                    @Override
                    public boolean hasNext() {
                        return mapEntries.hasNext();
                    }

                    @Override
                    public TypedIMMap.Entry<K, V> next() {
                        final Map.Entry<K, V> e = mapEntries.next();
                        return new TypedIMMap.Entry<K, V>() {
                            @Override
                            public V setValue(V value) {
                                return e.setValue(value);
                            }

                            @Nonnull
                            @Override
                            public K getKey() {
                                return e.getKey();
                            }

                            @Nonnull
                            @Override
                            public V getValue() {
                                return e.getValue();
                            }
                        };
                    }
                };
            }

            @Override
            public int size() {
                return TypedWrappedMMMap.this.size();
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public <KEY extends Key<KEY, VAL>, VAL extends V> VAL put(KEY key, VAL value) {
        return (VAL) delegate.put((K) key, value);
    }
}
