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

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedIndexed16KeySyncMap<D extends KeyDomain, V> extends TypedIndexed16KeyMap<D, V> {

    @Override
    public synchronized boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public synchronized int size() {
        return super.size();
    }

    @Nullable
    @Override
    public synchronized <VAL extends V> VAL get(@Nonnull Key<? extends D, VAL> key) {
        return super.get(key);
    }
    @Nullable
    @Override
    public synchronized <VAL extends V> VAL put(@Nonnull Key<? extends D, VAL> key, @Nullable VAL value) {
        return super.put(key, value);
    }

    @Override
    public synchronized boolean containsKey(@Nonnull Key<? extends D, V> key) {
        return super.containsKey(key);
    }
}
