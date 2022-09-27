package com.project.gestureappv2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class SingleImageActivity extends Activity implements OnTouchListener {

	private static final String TAG = "Touch";
	@SuppressWarnings("unused")
	private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;

	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// The 3 states (events) which the user is trying to perform
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// these PointF objects are used to record the point(s) the user is touching
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Bundle b=getIntent().getExtras();
		// Bitmap bmp = (Bitmap)b.getParcelable("image");
		setContentView(R.layout.activity_single_image);
		ImageView view = (ImageView) findViewById(R.id.imageHolder);
		view.setImageBitmap(ParameterPasser.image);
		view.setOnTouchListener(this);
		
	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

		
		float scale;
		
		// Handle touch events here...

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: // first finger down only
			//savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			Log.d(TAG, "mode=DRAG"); // write to LogCat
			mode = DRAG;
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:

			mode = NONE;
			Log.d(TAG, "mode=NONE");
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 5f) {
				//savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}
			break;

		case MotionEvent.ACTION_MOVE:

			if (mode == DRAG) {
				//matrix.set(savedMatrix);
				//matrix.postTranslate(event.getX() - start.x, event.getY()
				//		- start.y);
				/*
				 * create the transformation in the matrix of points
				 */
				
				view.setTranslationX(event.getX() - start.x);
				view.setTranslationY(event.getY()- start.y);
				
			} else if (mode == ZOOM) {
				// pinch zooming
				float newDist = spacing(event);
				//Log.d(TAG, "newDist=" + newDist);
				if (newDist > 5f) {
					//matrix.set(savedMatrix);
					scale = newDist / oldDist;
					/*
					 * setting the scaling of the matrix...if scale > 1 means
					 * zoom in...if scale < 1 means zoom out
					 */
					//matrix.postScale(scale, scale, mid.x, mid.y);
					view.setScaleX(scale);
					view.setScaleY(scale);
				}
			}
			break;
		}

		
		//view.setImageMatrix(matrix); // display the transformation on screen

		return true;
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);

		return (float) Math.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

}
