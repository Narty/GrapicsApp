package com.example.grapicsapp;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class MyRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "MyRenderer";
    private Triangle mTriangle;
	private Square mSquare;
	private ObjModel obj;
	
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
	private float[] mTempMatrix = new float[16];
    
    public volatile float mAngle;
    public volatile float mX = 0;
    public volatile float mY;

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //mTriangle = new Triangle();
        //mSquare = new Square();
        obj = new ObjModel();
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //test
        Matrix.setIdentityM(mModelMatrix, 0);
        
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        
        Matrix.translateM(mModelMatrix, 0, 0f, mY, 0f);
        Matrix.rotateM(mModelMatrix, 0, mX, 0f, 1f, 0f);
        
     // combine the model with the view matrix
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mModelMatrix, 0);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        
        // Create a rotation transformation for the triangle
        //long time = SystemClock.uptimeMillis() % 4000L;
        //float angle = 0.090f * ((int) time);
        //Matrix.translateM(mModelMatrix, 0, -1f, 0, 0);
        //Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, mAngle, -1.0f);
        
          
        
        //mTempMatrix = mModelMatrix.clone();
        //Matrix.multiplyMM(mModelMatrix,0 ,mTempMatrix,0 , mRotationMatrix, 0);
        
        //mTempMatrix = mMVPMatrix.clone();
        //Matrix.multiplyMM(mMVPMatrix, 0, mTempMatrix, 0, mModelMatrix, 0);
        
        // Combine the rotation matrix with the projection and camera view
        //Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);
        Matrix.scaleM(mMVPMatrix, 0, 0.25f ,0.25f ,0.25f);
        
        //mTriangle.draw(mMVPMatrix);
        obj.draw(mMVPMatrix);
        System.out.println("frame drawn " + mAngle);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        
        float ratio = (float) width / height;
        
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }
    
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

}
