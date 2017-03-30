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

import org.marid.typedmap.KeyDomain;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Dmitry Ovchinnikov
 */
public class Key<D extends KeyDomain, T> {

    private static final ClassValue<DomainKeyDescriptor> DESCRIPTORS = new ClassValue<DomainKeyDescriptor>() {
        @Override
        protected DomainKeyDescriptor computeValue(Class<?> type) {
            return new DomainKeyDescriptor();
        }
    };

    private final Class<D> domain;
    private final int index;
    private final Supplier<? extends T> defaultValueSupplier;

    public Key(Class<D> domain, Supplier<? extends T> defaultValueSupplier) {
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

    public T getDefaultValue() {
        return defaultValueSupplier.get();
    }

    static int getKeyCount(Class<? extends KeyDomain> domain) {
        return DESCRIPTORS.get(domain).keys.size() + Stream.of(domain.getInterfaces())
                .mapToInt(c -> DESCRIPTORS.get(c).keys.size())
                .sum();
    }

    private static class DomainKeyDescriptor {

        private final Map<Object, Class<?>> keys = new IdentityHashMap<>();

        private synchronized int add(Key<? extends KeyDomain, ?> key) {
            final int size = keys.size();
            final Class<?> oldClass = keys.put(key, key.domain);
            assert oldClass == null : "Duplicated key for " + oldClass;
            return size;
        }
    }
}
