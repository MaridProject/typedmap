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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Dmitry Ovchinnikov
 */
public class IndexedKey<K extends IndexedKey<K, ?, ?>, D extends KeyDomain, T> implements Key<K, D, T> {

    private static final ClassValue<DomainKeyDescriptor> DESCRIPTORS = new ClassValue<DomainKeyDescriptor>() {
        @SuppressWarnings("unchecked")
        @Override
        protected DomainKeyDescriptor computeValue(Class<?> type) {
            return new DomainKeyDescriptor((Class<? extends KeyDomain>) type);
        }
    };

    private final Class<D> domain;
    private final int index;
    private final Supplier<? extends T> defaultValueSupplier;

    public IndexedKey(Class<D> domain, Supplier<? extends T> defaultValueSupplier) {
        this.domain = domain;
        this.index = DESCRIPTORS.get(domain).add(this);
        this.defaultValueSupplier = defaultValueSupplier;
    }

    public int getIndex() {
        return index;
    }

    public Class<D> getDomain() {
        return domain;
    }

    public Supplier<? extends T> getDefaultValueSupplier() {
        return defaultValueSupplier;
    }

    @Override
    public String toString() {
        return "IndexedKey(" + index + ")";
    }

    @SuppressWarnings("unchecked")
    public static <D extends KeyDomain, K extends IndexedKey<K, ? super D, ?>> Set<K> getKeys(Class<D> domain) {
        final LinkedHashSet<K> keys = new LinkedHashSet<>();
        final DomainKeyDescriptor descriptor = DESCRIPTORS.get(domain);
        synchronized (descriptor.type) {
            for (final DomainKeyDescriptor d : descriptor) {
                synchronized (d.type) {
                    keys.addAll((Set) d.keys.keySet());
                }
            }
        }
        return keys;
    }

    @SuppressWarnings("unchecked")
    static <K extends IndexedKey<K, ? super D, ?>, D extends KeyDomain> K getKey(Class<D> domain, int index) {
        final DomainKeyDescriptor descriptor = DESCRIPTORS.get(domain);
        synchronized (descriptor.type) {
            for (final DomainKeyDescriptor d : descriptor) {
                synchronized (d.type) {
                    final Object key = d.byIndex.get(index);
                    if (key != null) {
                        return (K) key;
                    }
                }
            }
        }
        throw new NoSuchElementException("No such key: " + domain.getName() + "(" + index + ")");
    }

    @Nullable
    @Override
    public T getDefault() {
        return defaultValueSupplier.get();
    }

    private static class DomainKeyDescriptor implements Iterable<DomainKeyDescriptor> {

        private final Class<? extends KeyDomain> type;
        private final Map<Object, Class<?>> keys = new IdentityHashMap<>();
        private final Map<Integer, Object> byIndex = new HashMap<>();

        private DomainKeyDescriptor(Class<? extends KeyDomain> type) {
            this.type = type;
        }

        private int add(IndexedKey key) {
            synchronized (type) {
                final int id = getKeys(type).size();
                final Class<?> oldClass = keys.put(key, key.domain);
                assert oldClass == null : "Duplicated key for " + oldClass;
                byIndex.put(id, key);
                return id;
            }
        }

        @Nonnull
        @Override
        public Iterator<DomainKeyDescriptor> iterator() {
            return new Iterator<DomainKeyDescriptor>() {

                private Class<?> superclass = type;
                private final Iterator<Class<?>> interfaces = Arrays.asList(type.getInterfaces()).iterator();

                @Override
                public boolean hasNext() {
                    if (superclass == type) {
                        return true;
                    } else if (superclass != Object.class && superclass != null) {
                        return superclass.getSuperclass() != Object.class && superclass.getSuperclass() != null;
                    } else {
                        return interfaces.hasNext();
                    }
                }

                @Override
                public DomainKeyDescriptor next() {
                    if (superclass == type) {
                        superclass = superclass.getSuperclass();
                        return DomainKeyDescriptor.this;
                    } else if (superclass != Object.class && superclass != null) {
                        superclass = superclass.getSuperclass();
                        return DESCRIPTORS.get(superclass);
                    } else {
                        return DESCRIPTORS.get(interfaces.next());
                    }
                }
            };
        }
    }
}
