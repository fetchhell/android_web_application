package com.nvidia.devtech.Android5;

//---------------------------
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

//---------------------------
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

//--------------------------
import android.util.Log;

//--------------------------
import android.content.Context;

//--------------------------
import java.io.InputStream;
import java.io.IOException;

//--------------------------
import android.opengl.GLU;
import android.opengl.GLUtils;

//--------------------------
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//--------------------------
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class OpenGLRenderer implements GLSurfaceView.Renderer {

        /* Model TODO: change cube */  
        public Model m_model = new Model();

		/* Context */
		private Context context; 

		public OpenGLRenderer(Context context)
		{
		  this.context = context;
		}

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
		{
		    /* Clear depth, color */
		    GLES20.glClearDepthf(1.0f);
            GLES20.glClearColor(0.3f, 0.4f, 0.7f, 0.0f);    

			/* Enable depth test */
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            GLES20.glDepthFunc(GLES20.GL_LEQUAL);

			/* Load shader */
			m_model.loadShader();

			/* Load model */
			m_model.loadModel();

			/* Load texture */
		    m_model.loadTextureAPK(context);
			
			/* Download Content */
		   m_model.checkDownloads();               
		}

        @Override
        public void onDrawFrame(GL10 gl) 
		{            						                
            m_model.draw(this.context);                                                                
        }


        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);

			m_model.makeProjectionMatrix(width, height);
        }		
}