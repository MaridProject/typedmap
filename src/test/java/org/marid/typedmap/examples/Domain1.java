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

/**
 * @author Dmitry Ovchinnikov
 */
public interface Domain1 extends AggregatedDomain {

    SampleKey<Domain1, Integer> KEY1 = new SampleKey<>(Domain1.class, () -> 1);
    SampleKey<Domain1, Integer> KEY2 = new SampleKey<>(Domain1.class, () -> 2);
    SampleKey<Domain1, Long> KEY3 = new SampleKey<>(Domain1.class, () -> 3L);
}
