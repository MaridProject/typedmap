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

package org.marid.typedmap.examples;

import org.marid.typedmap.KeyDomain;
import org.marid.typedmap.indexed.IndexedKey;

import java.util.function.Supplier;

/**
 * @author Dmitry Ovchinnikov
 */
public class SampleKey<D extends KeyDomain, V> extends IndexedKey<D, V> {

    public SampleKey(Class<D> domain, Supplier<? extends V> defaultValueSupplier) {
        super(domain, defaultValueSupplier);
    }
}
