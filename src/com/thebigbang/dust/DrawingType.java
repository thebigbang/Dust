/*This file is part of Dust.
 * 
 * Dust is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * CustomPages is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *  
 * Copyright (c) Meï-Garino Jérémy 
*/
package com.thebigbang.dust;
/**
 * That enum is a helper to know how we want to draw the elements inside the graph we are filling.
 * @author thebigbang
 */
public enum DrawingType {
    /**
     * Points of elements, like a cloud or some dust.
     * Points size can be specified later.
     * like: * *   * * **
     */
	Dust,
        /**
         * Single simple line connected by all points passed as parameters
         * like: ______
         */
	Line,
        /**
         * Generate some random data inside our graph, very useful when we just want some quick debug, or just design purpose and not pure developper point of view.
         */
	Random,
        /**
         * Same as the line: each point passed as parameter is connected together, but the pattern is then closed from 0,beginning to end,0 and 0,0.
         * like: _________
         *      |::::::::/
         */
	FillLine
}
