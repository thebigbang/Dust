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
 * That abstract class is useful if you want to add your own processing method at the moment of finalizing the object for drawing.
 * For example adding custom colors or shape to the point.
 * Each point to process will be called in that method.
 * @author thebigbang
 */
public abstract class CustomProcessor {
	abstract public ExPath ProcessPoint(Int2 point,int quantity);
	//abstract List<ExPath> ProcessPoints(List<Int2> points);
}
