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
	
	private final float[] modelMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    //private final float[] mRotationMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
	//private float[] mTempMatrix = new float[16];
    
    public volatile float mAngle;
    public volatile float normalisedX = 0;
    public volatile float normalisedY;

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
        
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        
        Matrix.setIdentityM(modelMatrix, 0);
        
        //Matrix.translateM(modelMatrix, 0, normalisedX, normalisedY, 0f);
        
        Matrix.rotateM(modelMatrix, 0, (90f / -normalisedX), 0f, 1f, 0f);
        Matrix.rotateM(modelMatrix, 0, (90f / -normalisedY), 1f, 0f, 0f);
        //Matrix.rotateM(modelMatrix, 0, 90f, 1f, 0f, 0f);
        
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
        
        ////Matrix.translateM(modelMatrix, 0, 0f, mY, 0f);
        ////Matrix.rotateM(modelMatrix, 0, mX, 0f, 1f, 0f);
        
     // combine the model with the view matrix
        ////Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        // Calculate the projection and view transformation
        ////Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewProjectionMatrix, 0);
        
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
        //Matrix.scaleM(modelViewProjectionMatrix, 0, 0.25f ,0.25f ,0.25f);
        
        //mTriangle.draw(mMVPMatrix);
        obj.draw(modelViewProjectionMatrix);
        System.out.println("frame drawn " + mAngle);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        
        float ratio = (float) width / height;
        
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 9);
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 7f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
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
