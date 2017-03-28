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
import java.util.function.BiConsumer;

/**
 * @author Dmitry Ovchinnikov
 */
public interface TypedMap<K extends Key<K, V>, V> {

    boolean containsKey(@Nonnull K key);

    boolean containsValue(@Nonnull V value);

    int size();

    boolean isEmpty();

    @Nullable
    <KEY extends Key<KEY, VAL>, VAL extends V> VAL get(@Nonnull KEY key);

    void forEach(@Nonnull BiConsumer<K, V> consumer);
}
