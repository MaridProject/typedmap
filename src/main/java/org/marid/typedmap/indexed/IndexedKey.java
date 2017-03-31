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

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Dmitry Ovchinnikov
 */
public class IndexedKey<K extends IndexedKey<K, ?, ?>, D extends KeyDomain, T> implements Key<K, D, T> {

    private static final ClassValue<DomainKeyDescriptor> DESCRIPTORS = new ClassValue<DomainKeyDescriptor>() {
        @Override
        protected DomainKeyDescriptor computeValue(Class<?> type) {
            return new DomainKeyDescriptor();
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

    static int getKeyCount(Class<? extends KeyDomain> domain) {
        final AtomicInteger c = new AtomicInteger();
        walk(domain, d -> c.addAndGet(d.keys.size()));
        return c.get();
    }

    @SuppressWarnings("unchecked")
    static <K extends IndexedKey<K, ? super D, ?>, D extends KeyDomain> K getKey(Class<D> domain, int index) {
        try {
            walk(domain, d -> {
                final Object o = d.byIndex.get(index);
                if (o != null) {
                    throw new Return(o);
                }
            });
        } catch (Return r) {
            return (K) r.key;
        }
        return null;
    }

    @Nullable
    @Override
    public T getDefault() {
        return defaultValueSupplier.get();
    }

    private static void walk(Class<?> type, Consumer<DomainKeyDescriptor> consumer) {
        for (Class<?> c = type; c != Object.class && c != null; c = c.getSuperclass()) {
            consumer.accept(DESCRIPTORS.get(c));
        }
        for (final Class<?> c : type.getInterfaces()) {
            consumer.accept(DESCRIPTORS.get(c));
        }
    }

    private static class DomainKeyDescriptor {

        private final Map<Object, Class<?>> keys = new IdentityHashMap<>();
        private final Map<Integer, Object> byIndex = new HashMap<>();

        private synchronized int add(IndexedKey key) {
            final int size = keys.size();
            final Class<?> oldClass = keys.put(key, key.domain);
            assert oldClass == null : "Duplicated key for " + oldClass;
            byIndex.put(key.getIndex(), key);
            return size;
        }
    }

    private static class Return extends RuntimeException {

        private final Object key;

        private Return(Object key) {
            super(null, null, false, false);
            this.key = key;
        }
    }
}
