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
    private final float[] modelViewProjectionMatrix = new float[16];
    private float[] mLightModelMatrix = new float[16];
    private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
    private final float[] mLightPosInWorldSpace = new float[4];
    private final float[] mLightPosInEyeSpace = new float[4];
    private final float[] modelViewMatrix = new float[16];
    
    public volatile float mAngle;
    public volatile float normalisedX = 0;
    public volatile float normalisedY;

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame colour
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //mTriangle = new Triangle();
        //mSquare = new Square();
        obj = new ObjModel();
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background colour
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // Add program to OpenGL ES environment        
	    GLES20.glUseProgram(obj.mPerVertexProgramHandle);
        //Calculate light position
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -5.0f);   
        //Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);
               
        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, viewMatrix, 0, mLightPosInWorldSpace, 0);
        
        //Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0); removed here
        
        Matrix.setIdentityM(modelMatrix, 0);
        
        //Matrix.translateM(modelMatrix, 0, normalisedX, normalisedY, 0f);
        
        Matrix.rotateM(modelMatrix, 0, (90f / -normalisedX), 0f, 1f, 0f);
        Matrix.rotateM(modelMatrix, 0, (90f / -normalisedY), 1f, 0f, 0f);
        //Matrix.rotateM(modelMatrix, 0, 90f, 1f, 0f, 0f);
        
        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0); // added here
        
        //Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0); removed here
        
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        
        obj.draw(modelViewProjectionMatrix, mLightPosInModelSpace, modelViewMatrix);
        System.out.println("frame drawn " + mAngle);
        // Draw a point to indicate the light.    
        // Pass in the transformation matrix.
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewMatrix, 0, mLightModelMatrix, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewProjectionMatrix, 0);
        GLES20.glUseProgram(obj.mPointProgramHandle); 
        obj.drawLight(modelViewProjectionMatrix, mLightPosInModelSpace);
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

        if (shader != 0) 
        {
                // Pass in the shader source.
                GLES20.glShaderSource(shader, shaderCode);

                // Compile the shader.
                GLES20.glCompileShader(shader);

                // Get the compilation status.
                final int[] compileStatus = new int[1];
                GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

                // If the compilation failed, delete the shader.
                if (compileStatus[0] == 0) 
                {
                        Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
                        GLES20.glDeleteShader(shader);
                        shader = 0;
                }
        }

        if (shader == 0)
        {                        
                throw new RuntimeException("Error creating shader.");
        }

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
