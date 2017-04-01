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
import javax.annotation.Nullable;

/**
 * @author Dmitry Ovchinnikov
 */
public interface Key<K extends Key<K, ?, ?>, D extends KeyDomain, T> extends Comparable<Key<K, ?, ?>> {

    @Nullable
    T getDefault();

    @SuppressWarnings("unchecked")
    default K getKey() {
        return (K) this;
    }

    @Override
    default int compareTo(@Nonnull Key<K, ?, ?> o) {
        return System.identityHashCode(this) - System.identityHashCode(o);
    }

    default int getOrder() {
        return System.identityHashCode(this);
    }
}
