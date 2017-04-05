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

package org.marid.typedmap.sparsed;

import org.marid.typedmap.KeyDomain;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author Dmitry Ovchinnikov
 */
public interface SparsedKeyDomain extends KeyDomain {

    Map<Class<?>, Supplier<?>> RANDOM_SUPPLIER_MAP = new HashMap<>();

    List<Field> FIELDS = Stream.of(SparsedStruct.class.getFields())
            .peek(f -> f.setAccessible(true))
            .collect(toList());

    List<SparsedKey> KEYS = FIELDS.stream().map(SparsedKey::new).collect(toList());
}
