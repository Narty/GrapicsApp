package com.example.grapicsapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	private GLSurfaceView GLView;
	private static ArrayList<Float> vectors = new ArrayList<Float>();
	private Scanner scanner;
	public static float[] floatArray;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GLView = new MyGLSurfaceView(this);
		setContentView(GLView);
		System.out.println("onCreate complete");
		readFile();
		System.out.println(vectors.get(2));
		System.out.println("Read complete");
		System.out.println(vectors.size());
		System.out.println("Start array conversion");
		floatArray = new float[vectors.size()];
		
		for (int i = 0; i < vectors.size(); i++) {
		    Float f = vectors.get(i);
		    floatArray[i] = (f != null ? f : Float.NaN);
		}
		System.out.println("Array conversion complete!");
	}
	
	private void readFile() {
		try {
			//buf = new BufferedReader(new FileReader("/storage/sdcard0/Download/capsule.obj"));
			Scanner lineScanner;
			
			scanner = new Scanner(new FileReader("/storage/sdcard0/Download/capsule.obj"));
			//buf.readLine();
			scanner.nextLine();
			String line = scanner.nextLine();
			while(line != null && line.charAt(0) == 'v' && line.charAt(1) != 't') {
				lineScanner = new Scanner(line);
				lineScanner.next();
				vectors.add(lineScanner.nextFloat());
				vectors.add(lineScanner.nextFloat());
				vectors.add(lineScanner.nextFloat());
				if(scanner.hasNextLine())
					line = scanner.nextLine();
				else
					line = null;
			}
			//close scanners
			//System.out.println("Read " + (char) buf.read());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	 @Override
	    protected void onPause() {
	        super.onPause();
	        // The following call pauses the rendering thread.
	        // If your OpenGL application is memory intensive,
	        // you should consider de-allocating objects that
	        // consume significant memory here.
	        GLView.onPause();
	    }

	    @Override
	    protected void onResume() {
	        super.onResume();
	        // The following call resumes a paused rendering thread.
	        // If you de-allocated graphic objects for onPause()
	        // this is a good place to re-allocate them.
	        GLView.onResume();
	    }

}
