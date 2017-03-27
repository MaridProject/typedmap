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

package org.marid.typedmap.linked;

import org.marid.typedmap.Key;
import org.marid.typedmap.TypedIIMap;
import org.marid.typedmap.TypedIMMap;
import org.marid.typedmap.TypedMMMap;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author Dmitry Ovchinnikov
 */
public final class TypedLinkedMMMap<K extends Key<K, V>, V> implements TypedMMMap<K, V> {

    private LinkedEntry<K, V> entry;

    public TypedLinkedMMMap() {
    }

    public TypedLinkedMMMap(TypedIIMap<K, V> map) {
        map.entries().forEach(e -> put(e.getKey(), e.getValue()));
    }

    @Nonnull
    @Override
    public Set<? extends K> keySet() {
        final LinkedEntry<K, V> e = entry;
        if (e == null) {
            return Collections.emptySet();
        } else {
            return new AbstractSet<K>() {
                @Nonnull
                @Override
                public Iterator<K> iterator() {
                    final Iterator<LinkedEntry<K, V>> iterator = e.iterator();
                    return new Iterator<K>() {
                        @Override
                        public boolean hasNext() {
                            return iterator.hasNext();
                        }

                        @Override
                        public K next() {
                            return iterator.next().key;
                        }
                    };
                }

                @Override
                public boolean contains(Object o) {
                    for (LinkedEntry<K, ?> en = e; en != null; en = en.next) {
                        if (en.key == o) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public int size() {
                    return TypedLinkedMMMap.this.size();
                }
            };
        }
    }

    @Nonnull
    @Override
    public Collection<? extends V> values() {
        final LinkedEntry<K, V> e = entry;
        if (e == null) {
            return Collections.emptySet();
        } else {
            return new AbstractSet<V>() {
                @Nonnull
                @Override
                public Iterator<V> iterator() {
                    final Iterator<LinkedEntry<K, V>> iterator = e.iterator();
                    return new Iterator<V>() {
                        @Override
                        public boolean hasNext() {
                            return iterator.hasNext();
                        }

                        @Override
                        public V next() {
                            return iterator.next().value;
                        }
                    };
                }

                @Override
                public boolean contains(Object o) {
                    for (LinkedEntry<K, ?> en = e; en != null; en = en.next) {
                        if (en.value.equals(o)) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public int size() {
                    return TypedLinkedMMMap.this.size();
                }
            };
        }
    }

    @Override
    public boolean containsKey(@Nonnull K key) {
        for (LinkedEntry<K, ?> en = entry; en != null; en = en.next) {
            if (en.key == key) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(@Nonnull V value) {
        for (LinkedEntry<K, ?> en = entry; en != null; en = en.next) {
            if (en.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        int s = 0;
        for (LinkedEntry<K, ?> e = entry; e != null; e = e.next) {
            s++;
        }
        return s;
    }

    @Override
    public boolean isEmpty() {
        return entry == null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <KEY extends Key<KEY, VAL>, VAL extends V> VAL get(@Nonnull KEY key) {
        for (LinkedEntry<K, V> e = entry; e != null; e = e.next) {
            if (e.key == key) {
                return (VAL) e.value;
            }
        }
        return key.getDefault();
    }

    @Override
    public Collection<? extends TypedIMMap.Entry<K, V>> entries() {
        final LinkedEntry<K, V> e = entry;
        if (e == null) {
            return Collections.emptySet();
        } else {
            return new AbstractCollection<LinkedEntry<K, V>>() {
                @Nonnull
                @Override
                public Iterator<LinkedEntry<K, V>> iterator() {
                    return e.iterator();
                }

                @Override
                public int size() {
                    return TypedLinkedMMMap.this.size();
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <KEY extends Key<KEY, VAL>, VAL extends V> VAL put(KEY key, VAL value) {
        for (LinkedEntry<K, V> e = entry; e != null; e = e.next) {
            if (e.key == key) {
                return (VAL) e.setValue(value);
            }
        }
        entry = new LinkedEntry<>((K) key, value, entry);
        return null;
    }
}
