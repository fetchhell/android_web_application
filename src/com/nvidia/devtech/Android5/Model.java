package com.nvidia.devtech.Android5;

//---------------------------
import android.app.AlertDialog;
import android.app.Activity;

//---------------------------
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

//--------------------------
import java.util.ArrayList;
import java.util.List;

//---------------------------
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

//--------------------------
import android.util.Log;

//--------------------------
import java.util.Scanner;

//--------------------------
import android.content.Context;

//--------------------------
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedInputStream;

//--------------------------
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;

//--------------------------
import android.opengl.GLU;
import android.opengl.GLUtils;

//--------------------------
import android.os.Environment;

//--------------------------
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//--------------------------
import javax.microedition.khronos.egl.EGLConfig;

//--------------------------
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

//--------------------------
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

//--------------------------
import org.apache.http.impl.client.DefaultHttpClient;

class Model
{
   /* Matrix */
   public float[] m_projectionMatrix;
   public float[] m_modelMatrix;
   public float[] m_modelViewProjectionMatrix;

   /* Buffers */
   public FloatBuffer m_vertexBuffer;
   public FloatBuffer m_textureBuffer;

   /* Program handle */
   public int m_Program;

   /* Textures handle 
      TODO: change  */  
   int[] textures = new int[1];

   /* Textures names */
   public ArrayList<String> m_textureName 
             = new ArrayList<String>();

   public ArrayList<String> m_latestTexture
             = new ArrayList<String>(); 
   
   /* Textures size */
   public ArrayList<Integer> m_latestTextureSize
             = new ArrayList<Integer>(); 

   /* Current texture id */
   int currentTextureId = 0;

   /* Controlls */
   float angleX = 0, angleY = 0;   
   float speedX = 0, speedY = 0; 

   /* Shader */
   public Shader mShader;

   /* Update ? */
   public int update = 0;

   /* Notification message & title */
   public String message = null, title = null;

   /* Last update time */
   public int upTime = 0;

   /* Change updated time ? */
   public int changeUpdateTime = 0;

   public Model() 
   {
       m_modelMatrix               = new float[16];
       m_modelViewProjectionMatrix = new float[16];
	   m_projectionMatrix          = new float[16]; 

       Matrix.setIdentityM(m_modelMatrix, 0);
	   Matrix.setIdentityM(m_projectionMatrix, 0); 
	   
	  // Log.e("", stringFromJNI()); 
   }
   
   public void loadShader()
   {
       String vertexShaderCode =
	            "precision mediump float;"+
                "uniform mat4 u_modelViewProjectionMatrix;" +
                "attribute vec3 a_Position;"+
				"attribute vec2 a_TexCoord;"+
				"varying vec2   v_TexCoord;"+
                "void main() {" +
				    "v_TexCoord = a_TexCoord;" +
                    "gl_Position = u_modelViewProjectionMatrix * vec4(a_Position,1.0); }";

        Log.e("", vertexShaderCode);
		String fragmentShaderCode =
                "precision mediump float;"+
				"uniform sampler2D u_Texture;"+
                "varying vec2 v_TexCoord;"+
                "void main() {"+
                     "gl_FragColor = texture2D(u_Texture, v_TexCoord);"+
                "}";

		mShader = new Shader(vertexShaderCode, fragmentShaderCode);              
   }

   public void loadModel()
   {
       /* Vertex and Texture Coords */
       float Vertex[] = {
						  -1.0f, -1.0f,  1.0f,  
						   1.0f, -1.0f,  1.0f,  
						  -1.0f,  1.0f,  1.0f,  
						   1.0f,  1.0f,  1.0f, 

						   1.0f, -1.0f, -1.0f,  
						  -1.0f, -1.0f, -1.0f,  
						   1.0f,  1.0f, -1.0f,  
						  -1.0f,  1.0f, -1.0f,  

						  -1.0f, -1.0f, -1.0f,  
						  -1.0f, -1.0f,  1.0f,   
						  -1.0f,  1.0f, -1.0f,  
						  -1.0f,  1.0f,  1.0f,  

						   1.0f, -1.0f,  1.0f,  
						   1.0f, -1.0f, -1.0f,  
						   1.0f,  1.0f,  1.0f,  
						   1.0f,  1.0f, -1.0f,  

						  -1.0f,  1.0f,  1.0f,  
						   1.0f,  1.0f,  1.0f,  
						  -1.0f,  1.0f, -1.0f,  
						   1.0f,  1.0f, -1.0f, 

						  -1.0f, -1.0f, -1.0f,  
						   1.0f, -1.0f, -1.0f,  
						  -1.0f, -1.0f,  1.0f,  
						   1.0f, -1.0f,  1.0f   
                        };
 
	   float TexCoord[] = {
							 0.0f, 1.0f,  
							 1.0f, 1.0f,  
							 0.0f, 0.0f,  
							 1.0f, 0.0f, 
							 
							 0.0f, 1.0f,  
							 1.0f, 1.0f,  
							 0.0f, 0.0f,  
							 1.0f, 0.0f,
							 
							 0.0f, 1.0f,  
							 1.0f, 1.0f,  
							 0.0f, 0.0f,  
							 1.0f, 0.0f,
							 
							 0.0f, 1.0f,  
							 1.0f, 1.0f,  
							 0.0f, 0.0f,  
							 1.0f, 0.0f,
							 
							 0.0f, 1.0f,  
							 1.0f, 1.0f,  
							 0.0f, 0.0f,  
							 1.0f, 0.0f,
							 
							 0.0f, 1.0f,  
							 1.0f, 1.0f,  
							 0.0f, 0.0f,  
							 1.0f, 0.0f         
						  };

       ByteBuffer byteBuf = ByteBuffer.allocateDirect(Vertex.length * 4);
       byteBuf.order(ByteOrder.nativeOrder());
       m_vertexBuffer = byteBuf.asFloatBuffer();
       m_vertexBuffer.put(Vertex);
       m_vertexBuffer.position(0);
                
	   byteBuf = ByteBuffer.allocateDirect(TexCoord.length * 4);
       byteBuf.order(ByteOrder.nativeOrder());
       m_textureBuffer = byteBuf.asFloatBuffer();
       m_textureBuffer.put(TexCoord);
       m_textureBuffer.position(0);
	   
	   mShader.linkVertexBuffer(m_vertexBuffer);
	   mShader.linkTexCoordBuffer(m_textureBuffer);
   }

   public void makeProjectionMatrix(int width, int height)
   {
       float ratio = (float) width / height;
       float k=0.055f;
       float left = -k*ratio;
       float right = k*ratio;
       float bottom = -k;
       float top = k;
       float near = 0.1f;
       float far = 10.0f;

	   Matrix.frustumM(m_projectionMatrix, 0, left, right, bottom, top, near, far);
   }

   public boolean checkDownloads()
   {
      File dir = new File ("/data/data/com.nvidia.devtech.Android5/");
      
	  File file = new File(dir, "names.txt");
	  if(file == null) return false;

	  try{		
            Scanner s = new Scanner(file);

            while (s.hasNext()){
               m_latestTexture.add(s.next());
            }

            s.close();
	  }
	  catch(Exception ex){ m_latestTexture.clear(); };
	  update = 1;

	  return true;
   }

   public void draw(Context context)
   {
       if(1 == update) 
       {
	      manageTextures();

		  File dir = new File ("/data/data/com.nvidia.devtech.Android5/");
	      dir.mkdirs();
      
	      File file = new File(dir, "names.txt");
		  try{
			  PrintWriter pw = new PrintWriter(new FileOutputStream(file));

			  for(int i = 0; i < m_textureName.size(); i++)		  
				 pw.println(m_textureName.get(i));
		  
			  pw.close();
		  }
		  catch(Exception ex){};
	   }
	   
       GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);     
	   GLES20.glEnable(GLES20.GL_TEXTURE_2D); 

	   /* Helpful matrix */
	   float[] translate        = new float[16];
	   float[] rotate_X         = new float[16];
	   float[] rotate_Y         = new float[16];
	   float[] rotate_XY        = new float[16];
	   float[] rotate_translate = new float[16];
	   float[] mmodel           = new float[16];


	   Matrix.setIdentityM(translate, 0);
	   Matrix.setIdentityM(rotate_X,  0);
	   Matrix.setIdentityM(rotate_Y,  0);

	   float xModel = 0.0f, yModel = 0.0f, zModel = -6.0f;
       Matrix.translateM(translate, 0, xModel, yModel, zModel);
       Matrix.setRotateM(rotate_X,  0, angleX, 1.0f, 0.0f, 0.0f);
       Matrix.setRotateM(rotate_Y,  0, angleY, 0.0f, 1.0f, 0.0f);

	   Matrix.multiplyMM(rotate_XY, 0, rotate_X, 0, rotate_Y, 0);
	   Matrix.multiplyMM(rotate_translate, 0, translate, 0, rotate_XY, 0);

	   Matrix.multiplyMM(mmodel, 0, m_modelMatrix, 0, rotate_translate, 0);
	   Matrix.multiplyMM(m_modelViewProjectionMatrix, 0, m_projectionMatrix, 0, mmodel, 0);

	   mShader.linkModelViewProjectionMatrix(m_modelViewProjectionMatrix);
	   mShader.useProgram();

	   GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	   GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[currentTextureId]);

	   GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 24);

	   angleX += speedX;  
       angleY += speedY; 
   }

   public void loadTextureAPK(Context context)
   {
      InputStream is = context.getResources().openRawResource(R.drawable.olga);
      Bitmap bitmap = null;
      try {
          bitmap = BitmapFactory.decodeStream(is);
       } finally {
          try {
             is.close();
             is = null;
          } catch (IOException e) {
        }
      }  

	  GLES20.glGenTextures(1, textures, 0);
      setTextureParams(bitmap, textures[0]);
      bitmap.recycle();
   }

   public void setTextureParams(Bitmap bitmap, int id_texture)
   { 
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id_texture);

      GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
      GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

      GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
      GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

      GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
   }

   public void downloadUpdate()
   {
       String fullUrl = getUrl();
	   if(fullUrl == null) return;

	   int update_count = 0; 
	   int url_count    = 0;
	   int maxTime      = 0;

	   int lastPos = -1, nextPos = -1;
	   while((nextPos = fullUrl.indexOf(" ", lastPos + 1)) != -1)
	   {
	      String data = fullUrl.substring(lastPos + 1, nextPos);
		  int idata = Integer.parseInt(data);
		  if(idata > maxTime) maxTime = idata;

		  lastPos = nextPos;
		  nextPos = fullUrl.indexOf(" ", lastPos + 1);
		  	      
	      String name = fullUrl.substring(lastPos + 1, nextPos);

		  lastPos = nextPos;
		  nextPos = fullUrl.indexOf(" ", lastPos + 1);

		  String url  = fullUrl.substring(lastPos + 1, nextPos);

		  if(upTime <= idata && (!m_textureName.contains(name))) { downloadPicture(url, name); update_count++; }
		  lastPos = nextPos;

		  url_count++;
	   }

       if(0 == update_count || update_count < (url_count - m_textureName.size())) { message = "Do not need to be updated !"; title   = "Notification "; update = 0; }
	   else 
	   {
	     message = "Updated !"; title   = "Success !";
	     update = 1; changeUpdateTime = 1;
		 
		 upTime = maxTime; 
	   }
   }

   public String getUrl()
   {
      /* Get URL from DB */
      String url = "http://fetchhell.nightsite.info/todb.php";

	  InputStream is = null;
	  StringBuilder sb = null;
      try
	  {
	       DefaultHttpClient httpClient = new DefaultHttpClient();
           HttpPost httpPost = new HttpPost(url);

           HttpResponse httpResponse = httpClient.execute(httpPost);
           HttpEntity httpEntity = httpResponse.getEntity();
           is = httpEntity.getContent();

		   BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
           sb = new StringBuilder();

           String line = null;
           while ((line = reader.readLine()) != null) 		   
		      sb.append(line);			 	
	  }
	  catch(Exception e)
	  {
	     message = "Can't get URL !";
         title   = "Warning !";
		 
		 return null;  
	  }
	  
	  if(sb == null)
	  {
	     message = "Do not need to be updated !"; title = "Notification ";		 
		 return null;  
	  }

	  return sb.toString();
   }

   public void downloadPicture(String url, String name)
   {
      /* Download picture */
	  InputStream is = null;
	  byte[] data = null;
	  try 
	  {
		  DefaultHttpClient httpClient = new DefaultHttpClient();
		  HttpPost httpPost = new HttpPost(url);

		  HttpResponse httpResponse = httpClient.execute(httpPost);
		  HttpEntity httpEntity = httpResponse.getEntity();

		  is = httpEntity.getContent();		  
		  int contentSize = (int) httpEntity.getContentLength();
		  m_latestTextureSize.add(contentSize);

		  BufferedInputStream bis = new BufferedInputStream(is, 512);

		  data = new byte[contentSize];
	      int bytesRead = 0;
	      int offset = 0;
	  
	      while (bytesRead != -1 && offset < contentSize) {
	          bytesRead = bis.read(data, offset, contentSize - offset);
	          offset += bytesRead;
		  }   
		  is.close();
      }
	  catch(Exception e)
	  {
	     message = "Can't download picture !";
         title   = "Warning !";
		 
		 return;  
	  }
	  
	  /* Save to directory */
      File dir = new File ("/data/data/com.nvidia.devtech.Android5/");
	  dir.mkdirs();
      
	  File file = new File(dir, name);
	  try {
		  FileOutputStream f = new FileOutputStream(file);
		  f.write(data);
		  f.close();

		  m_latestTexture.add(name);
	  }
	  catch(Exception e)
	  {
	       m_latestTexture.clear();
		   message = "Can't write picture !";
           title   = "Warning !";

		   return;
	  }
   }
   
   public boolean manageTextures()
   {
      if(m_latestTexture.isEmpty()) return false;
      
	  File dir = new File ("/data/data/com.nvidia.devtech.Android5/");

	  Bitmap bitmap = null;

	  int a_size = m_latestTexture.size();

	  int id_size = textures.length;
	  int[] tmp_textures = new int[id_size];

	  /* Increasing size of texture id += m_latestTexture.size()*/
	  for(int i = 0; i < id_size; i++)
	     tmp_textures[i] = textures[i];
      
	  textures = new int[id_size + a_size];
	  for(int i = 0; i < id_size; i++)
	     textures[i] = tmp_textures[i];

      /* Gen Textures*/
      int[] textures_gen = new int[a_size];
	  GLES20.glGenTextures(a_size, textures_gen, 0);	    

	  InputStream is = null;
	  for(int i = 0; i < a_size; i++)
	  {
	         File file = new File(dir, m_latestTexture.get(i));
			 textures[id_size + i] = textures_gen[i];
			/* TODO : Load texture in memory, save handles, write function that takes
			   handle and bind texture that correspondes this handle
			*/
			try{
			  is = new FileInputStream(file);

			  BufferedInputStream buf = new BufferedInputStream(is);
			  bitmap = BitmapFactory.decodeStream(buf);
			  if(bitmap == null) return false;

			  setTextureParams(bitmap, textures[id_size + i]);
			  bitmap.recycle();

			  m_textureName.add(m_latestTexture.get(i));

			  is.close();
			}
			catch(Exception e)
			{ 
			   textures = tmp_textures; 

			   GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[currentTextureId]);
			   return false; 
			}
	  }	 

	  /* Load latest texture -> clear */
	  m_latestTexture.clear();

	  GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[currentTextureId]);

	  update = 0;
	  return true;
   }

   public void setIdTexture()
   {
      currentTextureId++;
	  if(currentTextureId >= textures.length) currentTextureId = 0;
   }

  /* public native String  stringFromJNI();

   static {
        System.loadLibrary("hello-jni");
    }
	*/
};