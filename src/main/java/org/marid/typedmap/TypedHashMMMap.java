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

package org.marid.typedmap;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedHashMMMap<K extends Key<K, V>, V> extends HashMap<K, V> implements TypedMMMap<K,V> {

    public TypedHashMMMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TypedHashMMMap(TypedIIMap<K, V> map, float loadFactor) {
        super(map.size(), loadFactor);
        map.entries().forEach(e -> super.put(e.getKey(), e.getValue()));
    }

    @Override
    public boolean containsKey(K key) {
        return super.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <KEY extends Key<KEY, VAL>, VAL extends V> VAL get(@Nonnull KEY key) {
        return (VAL) super.getOrDefault(key, key.getDefault());
    }

    @Override
    public Collection<? extends TypedIMMap.Entry<K, V>> entries() {
        return new AbstractCollection<TypedIMMap.Entry<K, V>>() {
            @Nonnull
            @Override
            public Iterator<TypedIMMap.Entry<K, V>> iterator() {
                final Iterator<Map.Entry<K, V>> mapEntries = entrySet().iterator();
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
                return TypedHashMMMap.this.size();
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public <KEY extends Key<KEY, VAL>, VAL extends V> VAL put(KEY key, VAL value) {
        return (VAL) super.put((K) key, value);
    }
}
