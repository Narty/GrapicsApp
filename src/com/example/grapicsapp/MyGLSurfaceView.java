package com.example.grapicsapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

	class MyGLSurfaceView extends GLSurfaceView {
		
		private final MyRenderer mRenderer;
		
		public MyGLSurfaceView(Context context) {
			super(context);
			setEGLContextClientVersion(2);
			mRenderer = new MyRenderer();
			setRenderer(mRenderer);
			setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
			System.out.println("View created");
		}
		
		public MyRenderer getRenderer() {
			return mRenderer;
		}
		
	    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	    private float mPreviousX;
	    private float mPreviousY;

		@Override
		public boolean onTouchEvent(MotionEvent e) {
			
			System.out.println("Can't touch this!");
			float x = e.getX();
			float y = e.getY();
			
			switch(e.getAction()) {
			case MotionEvent.ACTION_MOVE:
				float dx = x - mPreviousX;
				float dy = y - mPreviousY;
				
				if (y > getHeight() / 2) {
		              dx = dx * -1 ;
		            }
				
				if (x < getWidth() / 2) {
		              dy = dy * -1 ;
		            }
				mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;  // = 180.0f / 320
				
				mRenderer.normalisedX = (e.getX() / (float) this.getWidth()) * 2 - 1;
				mRenderer.normalisedY = -((e.getY() / (float) this.getHeight()) * 2 - 1);
				
				if(mRenderer.normalisedX < 0.1 && mRenderer.normalisedX > -0.1)
					mRenderer.normalisedX = 0.1f;
				if(mRenderer.normalisedY < 0.1 && mRenderer.normalisedY > -0.1)
					mRenderer.normalisedY = 0.1f;
				
	            requestRender();
	            System.out.println("x: " + mRenderer.normalisedX + " Y: " + mRenderer.normalisedY);
			}
			
			mPreviousX = x;
		    mPreviousY = y;
		    return true;
		}
		
	}