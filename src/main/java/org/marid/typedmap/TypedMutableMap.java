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
public interface TypedMutableMap<D extends KeyDomain, V> extends TypedMap<D, V> {

    @Nullable
    <VAL extends V> VAL put(@Nonnull Key<? extends D, VAL> key, @Nullable VAL value);
}
