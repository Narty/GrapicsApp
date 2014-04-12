package com.example.grapicsapp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class MainActivity extends Activity {

	private GLSurfaceView GLView;
	private ArrayList<Float> vectors = new ArrayList<Float>();
	private ArrayList<Short> faces = new ArrayList<Short>();
	private ArrayList<Float> vectorNormals = new ArrayList<Float>();
	private Scanner scanner;
	public static float[] floatArray;
	public static short[] drawListArray;
	public static float[] floatNormalArray;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GLView = new MyGLSurfaceView(this);
		setContentView(GLView);
		Button b = new Button(this);
		b.setText("Record");
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("Button Clicked");
				
			}});
		//final Button button = (Button) findViewById(R.id.button1);
		this.addContentView(b, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		System.out.println("onCreate complete");
		readFile();
		System.out.println(vectors.get(2));
		System.out.println(faces.get(faces.size() - 1));
		System.out.println("Read complete");
		System.out.println("Vectors: " + vectors.size());
		System.out.println("Vector Normals: " + vectorNormals.size());
		System.out.println("Faces: " + faces.size());
		System.out.println("Start vector array conversion");
		floatArray = new float[vectors.size()];		
		for (int i = 0; i < vectors.size(); i++) {
		    Float f = vectors.get(i);
		    floatArray[i] = (f != null ? f : Float.NaN);
		}
		System.out.println("Array conversion complete!");
		System.out.println("Start vector normal array conversion");
		floatNormalArray = new float[vectorNormals.size()];		
		for (int i = 0; i < vectorNormals.size(); i++) {
		    Float f = vectorNormals.get(i);
		    floatNormalArray[i] = (f != null ? f : Float.NaN);
		}
		System.out.println("Array conversion complete!");
		System.out.println("Start face array conversion");
		drawListArray = new short[faces.size()];		
		for (int i = 0; i < faces.size(); i++) {
		    Short s = faces.get(i);
		    drawListArray[i] = (short) (s - 1); // -1 from the short values as they are read in starting from 1 whereas the array is zero based for the vertices
		}
		System.out.println("Array conversion complete!");
	}
	
	private void readFile() {
		try {
			Scanner lineScanner;
			String line = null;

			scanner = new Scanner(new FileReader(
					"/storage/sdcard0/Download/capsule.obj"));
			scanner.nextLine();
			line = scanner.nextLine();
			System.out.println("while vector values start...");
			while (line.charAt(0) == 'v' && line.charAt(1) != 't'
					&& line.charAt(1) != 'n') {
				lineScanner = new Scanner(line);
				lineScanner.next();
				vectors.add(Float.parseFloat(lineScanner.next()));
				vectors.add(Float.parseFloat(lineScanner.next()));
				vectors.add(Float.parseFloat(lineScanner.next()));
				if (scanner.hasNextLine())
					line = scanner.nextLine();
				else
					break;
			}
			System.out.println("while vector values finished!");			
			System.out.println("while vector normal values start");
			
			while (line.charAt(0) == 'v' && line.charAt(1) == 'n') {
				lineScanner = new Scanner(line);
				lineScanner.next();
				vectorNormals.add(Float.parseFloat(lineScanner.next()));
				vectorNormals.add(Float.parseFloat(lineScanner.next()));
				vectorNormals.add(Float.parseFloat(lineScanner.next()));
				if (scanner.hasNextLine())
					line = scanner.nextLine();
				else
					break;
			}
			System.out.println("while vector normal values finished!");
			
			while (line.charAt(0) != 'v' && line.charAt(1) != 'n') {
				line = scanner.nextLine();
			}

			// read other scanner values
			while (line.charAt(0) != 'f') {
				line = scanner.nextLine();
			}
			// read in face values
			System.out.println("while face values start...");
			while (line.charAt(0) == 'f') {
				lineScanner = new Scanner(line);
				lineScanner.useDelimiter(" |/");
				lineScanner.next();
				faces.add(Short.parseShort(lineScanner.next()));
				lineScanner.next();
				lineScanner.next();// skip the texture and normal face values
									// for now
				faces.add(Short.parseShort(lineScanner.next()));
				lineScanner.next();
				lineScanner.next();
				faces.add(Short.parseShort(lineScanner.next()));
				if (scanner.hasNextLine())
					line = scanner.nextLine();
				else
					break;
			}
			System.out.println("while face values finished!");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			scanner.close();
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
