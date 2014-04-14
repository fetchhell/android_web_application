package com.nvidia.devtech.Android5;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
   public OpenGLRenderer renderer;    
   
   private final float TOUCH_SCALE_FACTOR = 180.0f / 320.0f;
   private float previousX;
   private float previousY;

   public MyGLSurfaceView(Context context) {
      super(context);
	  setEGLContextClientVersion(2);

      renderer = new OpenGLRenderer(context);
      this.setRenderer(renderer);

      this.requestFocus();  
      this.setFocusableInTouchMode(true);
   }
   
   @Override
   public boolean onTouchEvent(final MotionEvent evt) {
      float currentX = evt.getX();
      float currentY = evt.getY();
      float deltaX, deltaY;
      switch (evt.getAction()) {
         case MotionEvent.ACTION_MOVE:
            deltaX = currentX - previousX;
            deltaY = currentY - previousY;
            renderer.m_model.angleX += deltaY * TOUCH_SCALE_FACTOR;
            renderer.m_model.angleY += deltaX * TOUCH_SCALE_FACTOR;
      }

      previousX = currentX;
      previousY = currentY;
      return true;  
   }
}