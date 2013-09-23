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

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * Main class of the library.
 * Represent the panel in which the points will be drawn and the UI object.
 * @author thebigbang
 */
public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback {
	private ArrayList<ExPath> _graphics = new ArrayList<ExPath>();
	private Paint mPaint;
	private DottedDashPath dottedDash = new DottedDashPath();

	private DrawingThread _thread;
	private ExPath path;
	private boolean DisplayAsSquare = true;
	/**
	 * used as 0 replacement.
	 */
	private int minXPoint, minYPoint;
	/**
	 * used to know the real number of pixel from top of screen, to find from to
	 * where to draw, the parents values let us know the size in were to draw.
         * (Height axis: top to bottom)
	 */
	private int offsetHeight;
        /**
	 * used to know the real number of pixel from top of screen, to find from to
	 * where to draw, the parents values let us know the size in were to draw.
         * (Width axis: left to right)
	 */
        private int offsetWidth;
	/**
	 * used to calculate and dispose every points and lines in our drawer.
	 */
	private int parentHeight;
	/**
	 * Let us know how to scale. Default is 128, new value can be computed if
	 * default kept.
	 */
	public int ScalingY = 128;
	/**
	 * Let us know how to scale. Default is 128, new value can be computed if
	 * default kept.
	 */
	public int ScalingX = 128;
	/**
	 * Let us know if we must keep the default scaling value. (128) Default if
	 * false
	 */
	public boolean KeepScaling = false;
	/**
	 * Let us know how to draw, line, dust etc... Default value is Dust.
	 */
	public DrawingType drawingType = DrawingType.Dust;

	/**
	 * Used to override default color to paint the points in panel. Default is
	 * 0xFFFFFF00 (Color.Yellow as -256)
	 */
	public int MainPaintColor = 0xFFFFFF00;
	/**
	 * Used to reduce from that amount the calculated parentHeight
	 */
	public int parentHeightCorr = 0;
	/**
	 * Used to reduce from that amount the calculated parentWidth
	 */
	public int parentWidthCorr = 0;
	/**
	 * used to calculate and dispose every points and lines in our drawer. if
	 * DisplayAsSquare then == parentHeight;
	 */
	private int parentWidth;
	/**
	 * Used to calculate and dispose every points. Helps us getting the actual
	 * parentHeight.
	 */
	private float parentWeight;

	/**
	 * List of points to draw. We use Int2 because it is bundled in Android
	 * since 1.0 and is bipolar... Add points to it BEFORE adding the panel to a
	 * view. If you have really no other choice, please call the update
	 * method... {not yet existing method}
	 **/
	public List<Int2> PointsToDraw;
	/**
	 * Used by Dust to know the size of the points. Default is 2;
	 */
	public float PointSizeToDraw=0.8f;
	/**
	 * List of points correlating with PointsTodraw. Define for each PointToDraw
	 * their Z axis count.
	 */
	public int[] PointQuantity;
	/**
	 * If set: permit us to do some more calculation on our points just before
	 * drawing them. Must inherit from CustomProcessor SuperClass we have.
	 */
	public CustomProcessor customProcessor;

	/**
	 * if set to true, add some noise to the Dust generator
	 */
	public boolean bringSomeNoise;
	/**
	 * Generate a new drawingPanel with random values in it. Display the panel
	 * as a square.
	 * 
	 * @param c
	 *            context
	 */
	public DrawingPanel(Context c) {
		super(c);
	}

	/**
	 * Generate a new drawingPanel with random values in it.
	 * 
	 * @param c
	 *            context
	 * @param isSquarred
	 *            let us know if we should draw as a simple square or fill the
	 *            screen width. true=square|false=screenWidth
	 */
	public DrawingPanel(Context c, boolean isSquared) {
		super(c);
		DisplayAsSquare = isSquared;
	}

	/**
	 * Generate a new drawingPanel with random values in it. Add a {link
	 * {@link CustomProcessor} object to use on points calculation.
	 * 
	 * @param c
	 * @param isSquared
	 * @param cus
	 */
	public DrawingPanel(Context c, boolean isSquared, CustomProcessor cus) {
		super(c);
		DisplayAsSquare = isSquared;
		customProcessor = cus;
	}

	/**
	 * Generate a new drawingPanel with random values in it.
	 * 
	 * @param c
	 *            context
	 * @param isSquared
	 *            let us know if we should draw as a simple square or fill the
	 *            screen width. true=square|false=screenWidth
	 * @param type
	 *            let us know how to draw that panel content default is dust if
	 *            undefined.
	 */
	public DrawingPanel(Context c, boolean isSquared, DrawingType type) {
		super(c);
		DisplayAsSquare = isSquared;
		drawingType = type;
	}

	/**
	 * Generate a new drawingPanel with random values in it. Add a
	 * customProcessor to the instance.
	 * 
	 * @param c
	 *            context
	 * @param isSquared
	 *            let us know if we should draw as a simple square or fill the
	 *            screen width. true=square|false=screenWidth
	 * @param type
	 *            let us know how to draw that panel content default is dust if
	 *            undefined.
	 */
	public DrawingPanel(Context c, boolean isSquared, DrawingType type,
			CustomProcessor cu) {
		super(c);
		DisplayAsSquare = isSquared;
		drawingType = type;
		customProcessor = cu;
	}

	/**
	 * Generate a new DrawingPanel drawing our points in it Display the panel as
	 * a square.
	 * 
	 * @param c
	 *            context
	 * @param points
	 *            bipolar points to draw
	 */
	public DrawingPanel(Context c, List<Int2> points) {
		super(c);
		PointsToDraw = points;
	}

	/**
	 * Generate a new DrawingPanel drawing our points in it
	 * 
	 * @param c
	 *            context
	 * @param points
	 *            bipolar points to draw
	 * @param isSquarred
	 *            let us know if we should draw as a simple square or fill the
	 *            screen width. true=square|false=screenWidth
	 */
	public DrawingPanel(Context c, List<Int2> points, boolean isSquared) {
		super(c);
		PointsToDraw = points;
		DisplayAsSquare = isSquared;
	}

	/**
	 * We will generate all our data and view in here, since in case of random
	 * we need the parent not to be null.
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Init();
		if (PointsToDraw == null) {
			RandomGenerate();
		} else {
			if (ScalingY == 128 && ScalingX == 128) {
				MeasureNewScale();
			}
			if (drawingType == DrawingType.Dust) {
				ListGenerate();
			} else if (drawingType == DrawingType.Line) {
				LineGenerate();
			} else if (drawingType == DrawingType.Random) {
				RandomGenerate();
			} else if (drawingType == DrawingType.FillLine) {
				FillLineGenerate();
			}
		}
		_graphics.add(ExPath.Horizontal(offsetHeight, parentWidthCorr,
				parentWidth, Color.RED, dottedDash));
		_graphics.add(ExPath
				.Vertical(0, 0, offsetHeight, Color.RED, dottedDash));
	}

	/**
	 * We should only use LinearLayout.LayoutParams in that class!!
	 */
	@Override
	public void setLayoutParams(LayoutParams params) {
		parentWeight = ((LinearLayout.LayoutParams) params).weight - 0.05f;
		super.setLayoutParams(params);
	}

	private void InitPaint() {
		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(MainPaintColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(3);
	}

	private void Init() {
		InitPaint();
		getHolder().addCallback(this);

		_thread = new DrawingThread(getHolder(), this);

		path = new ExPath();
		path.color = Color.GREEN;
		// le plus en bas � gauche = 0,hauteurDuContainer ici)
		// todo: r�cup�rer les parentWeight en boucle pour d�terminer le poids
		// r�el sur notre �cran?
		// ex: <root><linear w:0.6><us w:1></linear></root> : 0.6
		// ex: <root><linear w:0.6><us w:1></linear></root> : 0.6
		LayoutParams parentParams = ((View) getParent()).getLayoutParams();
		int p = parentParams.height;// if -1 them
									// match_parent
		int t = parentParams.width;// if 0 then the
									// weight is on
									// this one.
		ViewGroup vgParent = (ViewGroup) getParent().getParent();
		int parentDivider = vgParent.getChildCount();
		// do check if divider is equal for everyone... easier one!
		// we do take as principle that we use linearLayout...
		boolean divideEqualy = true;
		for (int i = 0; i < parentDivider; i++) {
			LayoutParams par = vgParent.getChildAt(i).getLayoutParams();
			if (LinearLayout.LayoutParams.class.isInstance(par.getClass())) {
				if (((LinearLayout.LayoutParams) par).weight != parentWeight) {
					divideEqualy = false;
					break;
				}
			}
		}
		boolean isPortrait = getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT;
		int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels
				- parentHeightCorr;
		int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels
				- parentWidthCorr;
		// if we are HORIZONTAL we CUT Width, else we cut Height:
		if (((LinearLayout) vgParent).getOrientation() == LinearLayout.HORIZONTAL) {
			screenWidth = screenWidth / parentDivider;
		} else {
			screenHeight = screenHeight / parentDivider;
		}
		// if p=0 && t=-1: in PORTRAIT
		if (isPortrait) {
			if ((p == 0 && t == -1)) {
				parentHeight = (int) (screenHeight * parentWeight);
				parentWidth = DisplayAsSquare ? parentHeight : screenWidth;
			}// end of if
				// else if t=0 && p=-1: in PORTRAIT
			if (p == -1 && t == 0) {
				parentWidth = (int) (screenWidth * parentWeight);
				parentHeight = DisplayAsSquare ? parentWidth : screenHeight;
			}
		} else {
			// landscape invert H and W...
			if ((p == -1 && t == 0)) {
				parentHeight = (int) (screenHeight * parentWeight);
				parentWidth = DisplayAsSquare ? parentHeight : screenWidth;
			}// end of if
				// else if t=0 && p=-1: in PORTRAIT
			if (p == 0 && t == -1) {
				parentWidth = (int) (screenWidth * parentWeight);
				parentHeight = DisplayAsSquare ? parentWidth : screenHeight;
			}
		}
		offsetHeight = (parentHeight + parentHeightCorr) - 8;
		offsetWidth = parentWidth + parentWidthCorr;
		minXPoint = 2;
		minYPoint = offsetHeight - 2;
		path.moveTo(minXPoint, minYPoint);
		path.addCircle(minXPoint, minYPoint, 1, Direction.CW);
		_graphics.add(path);
		ExPath pHautDroite = new ExPath();
		pHautDroite.color = Color.GREEN;
		pHautDroite.moveTo(parentWidth - 2, 2);
		path.addCircle(parentWidth - 2, 2, 1, Direction.CW);
		_graphics.add(pHautDroite);
	}

	private void MeasureNewScale() {
		for (Int2 i : PointsToDraw) {
			if (i.x > ScalingX) {
				ScalingX = i.x + 1;
			} else if (i.y > ScalingY) {
				ScalingY = i.y + 1;
			}
		}
	}

	private void RandomGenerate() {
		final int pointsCnt = 20000;
		for (int i = 1; i <= pointsCnt; i++) {
			ExPath ppp = new ExPath();
			ppp.color = MainPaintColor;
			Random r = new Random();
			int yrnd = r.nextInt(minYPoint - (i * (minYPoint - 1)) / pointsCnt);
			int xrnd = r.nextInt(parentWidth - (i * (parentWidth - 1))
					/ pointsCnt);
			if (yrnd == 0 || xrnd == 0) {
				Log.i("DrawingPanelAwesomeness",
						"Bipolar random number was 0 hurray!!");
			}
			ppp.moveTo(xrnd, yrnd);
			ppp.addCircle(xrnd, yrnd, 1, Direction.CW);
			_graphics.add(ppp);
		}
	}

	/**
	 * do scale points /!\scaling basic constant is 128 /!\invert
	 */
	private void ListGenerate() {
		// todo: ajouter support Object perso permettant le calcul des
		// couleurs/familles.
		// for (Int2 i : PointsToDraw) {
		Random rnd = new Random(Calendar.getInstance().getTimeInMillis());
		for (int i = 0; i < PointsToDraw.size(); i++) {
			int scaledX = (PointsToDraw.get(i).x * parentWidth) / ScalingX;
			int scaledY = ((ScalingY - PointsToDraw.get(i).y) * minYPoint)
					/ ScalingY;
			ExPath ppp;
			if (customProcessor != null) {
				ppp = customProcessor.ProcessPoint(PointsToDraw.get(i),PointQuantity[i]);
			} else {
				ppp = new ExPath();
				ppp.color = MainPaintColor;
			}
			ppp.moveTo(scaledX, scaledY);
			ppp.addCircle(scaledX, scaledY, /*(PointQuantity == null ? 1
					: PointQuantity[i])*/PointSizeToDraw, Direction.CW);
			ppp.paintStyle = Paint.Style.FILL;
			// randomize around that point:
			if(bringSomeNoise){
				for (int j = 0; j < 2; j++) {
					int rndX = 0;
					while (rndX < scaledX - 4) {
						rndX = rnd.nextInt(scaledX + 4);
					}
					int rndY = 0;
					while (rndY < scaledY - 4) {
						rndY = rnd.nextInt(scaledY + 4);
					}
					/*if(PointSizeToDraw<1)
					{
						ppp.addRect(2, 2, 2, 2, Direction.CW);
					}*/
					ppp.addCircle(rndX, rndY, (PointSizeToDraw), Direction.CW);
				}}
			// end of randomize
			_graphics.add(ppp);
		}
	}

	private void LineGenerate() {
		// le plus en bas � gauche = 0,800 ici)
		ExPath line = new ExPath();
		line.moveTo(minXPoint, minYPoint);
		for (int i = 0; i < PointsToDraw.size(); i++) {
			int scaledX = (PointsToDraw.get(i).x * parentWidth) / ScalingX;
			int scaledY = ((ScalingY - PointsToDraw.get(i).y) * minYPoint)
					/ ScalingY;
			line.lineTo(scaledX, scaledY);
		}
		line.color = MainPaintColor;
		_graphics.add(line);
	}

	private void FillLineGenerate() {
		// le plus en bas � gauche = 0,800 ici)
		ExPath line = new ExPath();
		line.moveTo(minXPoint, minYPoint);
		int scaledX = minXPoint, scaledY = minYPoint;
		for (int i = 0; i < PointsToDraw.size(); i++) {
			scaledX = (PointsToDraw.get(i).x * parentWidth) / ScalingX;
			scaledY = ((ScalingY - PointsToDraw.get(i).y) * minYPoint)
					/ ScalingY;
			line.lineTo(scaledX, scaledY);
		}
		// scaled are now last assigned values, we will go down to minYpoint on
		// scaleX then to minXpoint.
		line.lineTo(scaledX, minYPoint);
		line.lineTo(minXPoint, minYPoint);
		line.paintStyle = Paint.Style.FILL;
		line.color = MainPaintColor;
		_graphics.add(line);
	}

        /**
         * used by double touch event.
         */
	boolean mHasDoubleClicked = false;
        /**
         * used by our double touch event implementation.
         */
	long lastPressTime;
        /**
         * interval for the double touch event.
         */
	private final static long DOUBLE_PRESS_INTERVAL = 1000;
        /**
         * List of potential listeners for the double touch input.
         */
	protected List<DrawingPanelDoubleTouch> doubleTouchListeners;
/**
 * add a double touch listener to the listeners.
 * @param listener 
 */
	public void addDoubleTouchListener(DrawingPanelDoubleTouch listener) {
		if (doubleTouchListeners == null)
			doubleTouchListeners = new ArrayList<DrawingPanelDoubleTouch>();
		doubleTouchListeners.add(listener);
	}
/**
 * remove the double touch listener from the list.
 * @param listener 
 */
	public void removeDoubleTouchListener(DrawingPanelDoubleTouch listener) {
		if (doubleTouchListeners == null || doubleTouchListeners.isEmpty())
			return;
		doubleTouchListeners.remove(listener);
	}
/**
 * triggered by a double touch event.
 */
	private void doubleTouchTrigger() {
		if (doubleTouchListeners == null || doubleTouchListeners.isEmpty())
			return;
		for (DrawingPanelDoubleTouch d : doubleTouchListeners) {
			d.DoubleTouch(this);
		}
	}
/**
 * used for our double touch implementation. Will tells us if single touch, double or else.
 */
	private final static String PINCH = "Pinch", TOUCH = "Touch",
			IDLE = "idle";
        /**
         * used to know the actual state of the double touch event.
         */
	String touchState = IDLE;
        /**
         * points and distance of touch event.
         */
	float touchDistx, touchDisty, touchDist0, touchDistCurrent;

	/**
	 * zoom not working yet! impl in progress.
         * todo.
         * note: work if you handle manually the event and open your own full screen window containing only the panel.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			touchState = TOUCH;
		}
		if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
			touchState = PINCH;
			touchDistx = event.getX(0) - event.getX(1);
			touchDisty = event.getY(0) - event.getY(1);
			touchDist0 = (float) Math.sqrt(touchDistx * touchDistx + touchDisty
					* touchDisty);
			Log.i("DrawingPanel", "it was scrolled (zoom?)");
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE && touchState == PINCH) {
			// Get the current distance
			touchDistx = event.getX(0) - event.getX(1);
			touchDisty = event.getY(0) - event.getY(1);
			touchDistCurrent = (float) Math.sqrt(touchDistx * touchDistx
					+ touchDisty * touchDisty);
			doZoom();
		}
		if (event.getAction() != MotionEvent.ACTION_POINTER_UP) {
			touchState = TOUCH;
			// return true;
		}
		if (event.getAction() != MotionEvent.ACTION_UP) {
			touchState = IDLE;
			return true;
		}// Get current time in nano seconds.
		long pressTime = System.currentTimeMillis();
		// If double click...
		if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
			doubleTouchTrigger();
			mHasDoubleClicked = true;
		} else { // If not double click....
			mHasDoubleClicked = false;
			Handler myHandler = new Handler() {
				public void handleMessage(Message m) {
					if (!mHasDoubleClicked) {
						Log.i("DrawingPanel", "single clicked");
					}
				}
			};
			Message m = new Message();
			myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
		}
		// record the last time the menu button was pressed.
		lastPressTime = pressTime;
		return true;
	}
/**
 * called to do a zoom
 */
	private void doZoom() {
		float curScale = touchDistCurrent / touchDist0;
		if (curScale < 0.1) {
			curScale = 0.1f;
		}
		Bitmap surfaceBitmap = this.getDrawingCache();
		Bitmap resizedBitmap;
		int newHeight = (int) (surfaceBitmap.getHeight() * curScale);
		int newWidth = (int) (surfaceBitmap.getWidth() * curScale);
		resizedBitmap = Bitmap.createScaledBitmap(surfaceBitmap, newWidth,
				newHeight, false);
		ImageView iv = new ImageView(getContext());
		iv.setImageBitmap(resizedBitmap);
		((ViewGroup) getParent()).addView(iv);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (canvas == null) {
			_thread.setRunning(false);
			return;
		}
		for (ExPath path : _graphics) {

			mPaint.setColor(path.color);
			mPaint.setPathEffect(path.effect);
			canvas.drawPath(path, mPaint);
		}
	}

	public void draw(Canvas canvas) {
		if (canvas == null) {
			_thread.setRunning(false);
			return;
		}
		if (mPaint == null) {
			// we have no window in here... lets default Paint obj
			InitPaint();
		}
		Style base = mPaint.getStyle();
		for (ExPath path : _graphics) {
			mPaint.setStyle(path.paintStyle == null ? base : path.paintStyle);
			mPaint.setColor(path.color); 
			mPaint.setPathEffect(path.effect);
			canvas.drawPath(path, mPaint);
		}
	}

	/*
	 * public void superDraw(Canvas canvas) { super.draw(canvas); }
	 */
	/**
	 * @deprecated
	 * @param _surfaceHolder
	 */
	public void draw(SurfaceHolder _surfaceHolder) {
		for (ExPath p : _graphics) {
			mPaint.setColor(p.color);
			mPaint.setPathEffect(p.effect);
			Canvas canvas = _surfaceHolder.lockCanvas(null);
			if (canvas == null) {
				_thread.setRunning(false);
				return;
			}
			canvas.drawPath(p, mPaint);
			_surfaceHolder.unlockCanvasAndPost(canvas);
		}

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		renewThread();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		renewThread();
	}

	private void renewThread() {
		if (_thread.isAlive())
			return;
		if (_thread.getState() == State.NEW) {
			_thread.setRunning(true);
			_thread.start();
		} else {
			_thread = new DrawingThread(getHolder(), this);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		_thread.setRunning(false);
		while (retry) {
			try {
				_thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// we will try it again and again...
			}
		}
	}

	/**
	 * Sets raw points to PointsToDraw, and PointSize to 1 for each PointsToDraw
	 * element.
	 * 
	 * @param points
	 */
	public void setPointsToDraw(List<Int2> points) {
		PointsToDraw = points;
	}

	/**
	 * Convert a list of points as int[].size for X and int[pos].element as Y.
	 * Set PointSize to 1 for each created element.
	 * 
	 * @param points
	 */
	public void setPointsToDraw(int[] points) {
		List<Int2> ii = new ArrayList<Int2>();
		int[] iii = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			ii.add(new Int2(i, points[i]));
			iii[i] = 1;
		}
		PointsToDraw = ii;
		PointQuantity = iii;
	}

	/**
	 * Convert 2 lists of points one for X axis the other one for Y axis. Set
	 * PointSize to 1 for each created element.
	 * 
	 * @param X
	 * @param Y
	 */
	public void setPointsToDraw(List<Integer> X, List<Integer> Y) {
		List<Int2> ii = new ArrayList<Int2>();
		PointQuantity = new int[X.size()];
		for (int i = 0; i < X.size(); i++) {
			ii.add(new Int2(X.get(i), Y.get(i)));
			PointQuantity[i] = 1;
		}
		PointsToDraw = ii;
	}
/**
 * is the display of the panel forced as a square on screen?
 * @return 
 */
	public boolean isSquared() {
		return DisplayAsSquare;
	}

}
