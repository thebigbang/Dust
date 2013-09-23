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

import android.graphics.Canvas;
import android.view.SurfaceHolder;
/**
 * Specific thread used for drawing purpose. Automatically managed, nothing much to do in here when implementing the library.
 * @author thebigbang
 */
class DrawingThread extends Thread {
	private SurfaceHolder _surfaceHolder;
	private DrawingPanel _panel;
	private boolean _run = false;

	public DrawingThread(SurfaceHolder surfaceHolder, DrawingPanel panel) {
		_surfaceHolder = surfaceHolder;
		_panel = panel;
	}

	public void setRunning(boolean run) {
		_run = run;
	}

	public SurfaceHolder getSurfaceHolder() {
		return _surfaceHolder;
	}

	@Override
	public void run() {
		Canvas c;
		while (_run) {
			c = null;
			try {
				c = _surfaceHolder.lockCanvas(null);
				synchronized (_surfaceHolder) {
					_panel.draw(c);
					}
			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) {
					_surfaceHolder.unlockCanvasAndPost(c);
					 _run=false;
				}
			}
		}
	}
}