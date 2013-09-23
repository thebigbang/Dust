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

import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;

/**
 * Improves Path class by adding some small params value we might need.
 * 
 * @author Jeremy.Mei-Garino
 * 
 */
public class ExPath extends Path {
	public int color;
	public PathEffect effect;
	public Style paintStyle;

	/**
	 * generate a horizontal line
	 * 
	 * @param heightFromTop
	 * @param leftStart
	 * @param lenght
	 * @param color
	 * @return
	 */
	public static ExPath Horizontal(float heightFromTop, float leftStart,
			float lenght, int color) {
		return Horizontal(heightFromTop, leftStart, lenght, color, null);
	}

	/**
	 * generate a horizontal line
	 * 
	 * @param heightFromTop
	 * @param leftStart
	 * @param lenght
	 * @param color
	 * @param effect
	 * @return
	 */
	public static ExPath Horizontal(float heightFromTop, float leftStart,
			float lenght, int color, PathEffect effect) {
		ExPath p = new ExPath();
		p.moveTo(leftStart, heightFromTop);
		p.lineTo(leftStart, heightFromTop);
		p.lineTo(lenght, heightFromTop);
		p.color = color;
		p.effect = effect;
		return p;
	}

	/**
	 * generate a vertical line
	 * 
	 * @param heightFromTop
	 * @param leftStart
	 * @param lenght
	 * @param color
	 * @return
	 */
	public static ExPath Vertical(float topStart, float leftStart,
			float height, int color) {
		return Vertical(topStart, leftStart, height, color, null);
	}

	/**
	 * generate a vertical line
	 * 
	 * @param topStart
	 * @param leftStart
	 * @param height
	 * @param color
	 * @param effect
	 * @return
	 */
	public static ExPath Vertical(float topStart, float leftStart,
			float height, int color, PathEffect effect) {
		ExPath p = new ExPath();
		p.moveTo(leftStart, topStart);
		p.lineTo(leftStart, topStart);
		p.lineTo(leftStart, height + topStart);
		p.color = color;
		p.effect = effect;
		return p;
	}

	public void setPaintStyle(Style fill) {
		paintStyle=fill;
	}
}
