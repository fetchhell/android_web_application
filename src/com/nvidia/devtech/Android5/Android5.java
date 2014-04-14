/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nvidia.devtech.Android5; 

//------------------------------
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;

//------------------------------
import android.widget.TextView;
import android.widget.Toast;

//------------------------------
import android.os.Bundle;

//------------------------------
import android.opengl.GLSurfaceView;

//------------------------------
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;

//------------------------------
import android.os.StrictMode;

import android.content.Context;

public class Android5 extends Activity
{   
	MyGLSurfaceView view;
	final Context context = this;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{	   
		super.onCreate(savedInstanceState);        
        
		StrictMode.ThreadPolicy policy 
		        = new StrictMode.ThreadPolicy.Builder().permitAll().permitDiskReads().permitDiskWrites().build();

        StrictMode.setThreadPolicy(policy); 

        view = new MyGLSurfaceView(this);
        setContentView(view);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {    
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_upload:
			{
				/* TODO : Check for update, download content if needed */
				view.renderer.m_model.downloadUpdate(); 

				AlertDialog.Builder builder = new AlertDialog.Builder((Activity)context);
                builder.setMessage(view.renderer.m_model.message).setTitle(view.renderer.m_model.title);
                AlertDialog dialog = builder.create();
		        dialog.show();

				return true;
			}
			case R.id.action_change:
			{
		        /* TODO : Change texture id_texture = ccount % number_of_textures */
				view.renderer.m_model.setIdTexture();
				return true;
			}
			default:
				return super.onOptionsItemSelected(item);
    }
}
}
