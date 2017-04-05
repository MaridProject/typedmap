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

import org.marid.typedmap.examples.SampleKey;

import java.lang.reflect.Field;

/**
 * @author Dmitry Ovchinnikov
 */
public class SparsedKey extends SampleKey<SparsedKeyDomain, Object> {

    public final Field field;

    public SparsedKey(Field field) {
        super(SparsedKeyDomain.class);
        this.field = field;
        field.setAccessible(true);
    }
}
