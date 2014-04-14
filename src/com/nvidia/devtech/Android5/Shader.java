package com.nvidia.devtech.Android5;

//-----------------------------
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

//-----------------------------
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
 
 //----------------------------
import android.util.Log;
import android.content.Context;

//-----------------------------
import java.io.InputStream;
import java.io.IOException;

//-----------------------------
import android.opengl.GLU;
import android.opengl.GLUtils;

//-----------------------------
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//--------------------------------
import javax.microedition.khronos.egl.EGLConfig;

public class Shader {
	private int program_Handle;

	public Shader(String vertexShaderCode, String fragmentShaderCode){
		  createProgram(vertexShaderCode, fragmentShaderCode);
	}

	private void createProgram(String vertexShaderCode, String fragmentShaderCode){
		  int vertexShader_Handle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
     
		  GLES20.glShaderSource(vertexShader_Handle, vertexShaderCode);
		  GLES20.glCompileShader(vertexShader_Handle);

	      int[] compiled = new int[1];
          GLES20.glGetShaderiv(vertexShader_Handle, GLES20.GL_COMPILE_STATUS, compiled, 0);

		  if(compiled[0] == 0)
		  {
		    Log.e("", "Can not compile ! :" + GLES20.glGetShaderInfoLog(vertexShader_Handle));
		  }

		  int fragmentShader_Handle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		  GLES20.glShaderSource(fragmentShader_Handle, fragmentShaderCode);
		  GLES20.glCompileShader(fragmentShader_Handle);

		  GLES20.glGetShaderiv(fragmentShader_Handle, GLES20.GL_COMPILE_STATUS, compiled, 0);

		  if(compiled[0] == 0)
		  {
		     Log.e("", "Can not compile ! :" + GLES20.glGetShaderInfoLog(fragmentShader_Handle));
		  }
     
		  program_Handle = GLES20.glCreateProgram();
     
		  GLES20.glAttachShader(program_Handle, vertexShader_Handle);
		  GLES20.glAttachShader(program_Handle, fragmentShader_Handle);
		  GLES20.glLinkProgram(program_Handle);
	}


	public void linkVertexBuffer(FloatBuffer vertexBuffer){
		  GLES20.glUseProgram(program_Handle);
		  int a_vertex_Handle = GLES20.glGetAttribLocation(program_Handle, "a_Position");

		  GLES20.glEnableVertexAttribArray(a_vertex_Handle);
		  GLES20.glVertexAttribPointer(a_vertex_Handle, 3, GLES20.GL_FLOAT, false, 0,vertexBuffer);
	}

	public void linkTexCoordBuffer(FloatBuffer texCoordBuffer){
		  GLES20.glUseProgram(program_Handle);
		  int a_texCoord_Handle = GLES20.glGetAttribLocation(program_Handle, "a_TexCoord");

		  GLES20.glEnableVertexAttribArray(a_texCoord_Handle);
		  GLES20.glVertexAttribPointer(a_texCoord_Handle, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer);
	}

	public void linkNormalBuffer(FloatBuffer normalBuffer){
		 GLES20.glUseProgram(program_Handle);
		 int a_normal_Handle = GLES20.glGetAttribLocation(program_Handle, "a_normal");

		 GLES20.glEnableVertexAttribArray(a_normal_Handle);
		 GLES20.glVertexAttribPointer(a_normal_Handle, 3, GLES20.GL_FLOAT, false, 0,normalBuffer);
	}

	public void linkModelViewProjectionMatrix(float [] modelViewProjectionMatrix){
		  GLES20.glUseProgram(program_Handle);

		  int u_modelViewProjectionMatrix_Handle = GLES20.glGetUniformLocation(program_Handle, "u_modelViewProjectionMatrix");
		  GLES20.glUniformMatrix4fv(u_modelViewProjectionMatrix_Handle, 1, false, modelViewProjectionMatrix, 0);
	}

	public void useProgram(){
		  GLES20.glUseProgram(program_Handle);
	}
}