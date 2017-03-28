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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

/**
 * @author Dmitry Ovchinnikov
 */
public class TypedLinkedSyncMap<K extends Key<K, V>, V> extends TypedLinkedMap<K, V> {

    @Override
    public synchronized boolean containsValue(@Nonnull V value) {
        return super.containsValue(value);
    }

    @Override
    public synchronized boolean containsKey(@Nonnull K key) {
        return super.containsKey(key);
    }

    @Nullable
    @Override
    public synchronized <KEY extends Key<KEY, VAL>, VAL extends V> VAL put(@Nonnull KEY key, @Nullable VAL value) {
        return super.put(key, value);
    }

    @Nullable
    @Override
    public synchronized <KEY extends Key<KEY, VAL>, VAL extends V> VAL get(@Nonnull KEY key) {
        return super.get(key);
    }

    @Override
    public synchronized void forEach(@Nonnull BiConsumer<K, V> consumer) {
        super.forEach(consumer);
    }

    @Override
    public synchronized boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public synchronized int size() {
        return super.size();
    }
}
