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

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;

/**
 * added 26-07-2013 for android API<11 compatibility
 * it is a basic Copy/Paste of android/renderscript.Int2
 * @param rendercriptObj
 * @return
 */
public class Int2 {
    public Int2() {
    }

    public Int2(int initX, int initY) {
        x = initX;
        y = initY;
    }

    public int x;
    public int y;

    @TargetApi(11)
    public static Int2 fromRendescript(android.renderscript.Int2 rendercriptObj)
    {
    	return new Int2(rendercriptObj.x, rendercriptObj.y);
    }

    @TargetApi(11)
    public static List<Int2> fromRenderScript(List<android.renderscript.Int2> rendercriptObj)
    {
    	List<Int2> ret=new ArrayList<Int2>(rendercriptObj.size());
    	for(android.renderscript.Int2 i:rendercriptObj)
    	{
    		ret.add(new Int2(i.x,i.y));
    	}
    	return ret;
    }
}