package com.example.grapicsapp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class ObjModel {
	private final FloatBuffer vertexBuffer;
	private final ShortBuffer drawListBuffer;
	private final FloatBuffer vertexNormalBuffer;
	static final int COORDS_PER_VERTEX = 3; // number of coordinates per vertex in this array
	float colour[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f }; // Set colour with red, green, blue and alpha (opacity) values
	private int mPositionHandle;
	private int mColourLocation;
	private int mNormalHandle;
	private int mMVPMatrixHandle;
	private int mMVMatrixHandle;
	private int mLightPosHandle;
	public final int mPerVertexProgramHandle;
    public final int mPointProgramHandle; //light
	private final int vertexCount = MainActivity.floatArray.length / COORDS_PER_VERTEX;	    
	private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
	
	private final String vertexShaderCode =
    "uniform mat4 uMVPMatrix;" +               // A constant representing the combined model/view/projection matrix.
    "uniform mat4 uMVMatrix;" +                // A constant representing the combined model/view matrix.        
    "uniform vec3 uLightPos;" +                // The position of the light in eye space.
          
    "attribute vec4 vPosition;" +              // Per-vertex position information we will pass in.
    "attribute vec4 vColor;" +                 // Per-vertex colour information we will pass in.
    "attribute vec3 vNormal;" +                // Per-vertex normal information we will pass in.
    
    "varying vec4 aColor;" +                   // This will be passed into the fragment shader.
    
    "void main()" +         				   // The entry point for our vertex shader.
    "{" +                
    // Transform the vertex into eye space.
    "vec3 modelViewVertex = vec3(uMVMatrix * vPosition);" +
    // Transform the normal's orientation into eye space.
    "vec3 modelViewNormal = vec3(uMVMatrix * vec4(vNormal, 0.0));" +
    // Will be used for attenuation.
    "float distance = length(uLightPos - modelViewVertex);" +
    // Get a lighting direction vector from the light to the vertex.
    "vec3 lightVector = normalize(uLightPos - modelViewVertex);" +
    // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
    // pointing in the same direction then it will get max illumination.
    "float diffuse = max(dot(modelViewNormal, lightVector), 0.1);" +                                                                                                                                       
    // Attenuate the light based on distance.
    "diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));" +
 	 // Multiply the colour by the illumination level. It will be interpolated across the triangle.
    "aColor = vColor * diffuse;" +          
    // gl_Position is a special variable used to store the final position.
    // Multiply the vertex by the matrix to get the final point in normalised screen coordinates.                
    "gl_Position = uMVPMatrix * vPosition;" +     
    "}"; 
	
	private final String fragmentShaderCode =
	"precision mediump float;" +                // Set the default precision to medium. We don't need as high of a 
												// precision in the fragment shader.                                
    "varying vec4 aColor;" +                	// This is the color from the vertex shader interpolated across the 
    											// triangle per fragment.                          
    "void main()" +                				// The entry point for our fragment shader.
    "{" +
    "   gl_FragColor = aColor;" +               // Pass the color directly through the pipeline.                  
    "}";
	
	private final String pointVertexShaderCode =
	"uniform mat4 uMVPMatrix;" +
	"attribute vec4 vPosition;" +
	"void main() {" +
	"  gl_Position = uMVPMatrix * vPosition;" + 
	"  gl_PointSize = 5.0;" +
	"}";

	private final String pointFragmentShaderCode =
	"precision mediump float;" +
	"void main() {" +
	"  gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);" +
	"}";

	public ObjModel() {
		// Initialise vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
		// (number of coordinate values * 4 bytes per float)
				MainActivity.floatArray.length * 4);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		// add the coordinates to the FloatBuffer
		vertexBuffer.put(MainActivity.floatArray);
		// set the buffer to read the first coordinate
		vertexBuffer.position(0);

		// Initialise vertex byte buffer for normal coordinates
		ByteBuffer nb = ByteBuffer.allocateDirect(
		// (number of coordinate values * 4 bytes per float)
				MainActivity.floatNormalArray.length * 4);
		// use the device hardware's native byte order
		nb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		vertexNormalBuffer = nb.asFloatBuffer();
		// add the coordinates to the FloatBuffer
		vertexNormalBuffer.put(MainActivity.floatNormalArray);
		// set the buffer to read the first coordinate
		vertexNormalBuffer.position(0);

		// Initialise byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(
		// (# of coordinate values * 2 bytes per short)
                MainActivity.drawListArray.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(MainActivity.drawListArray);
        drawListBuffer.position(0);
		
		final int vertexShader = MyRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
	    final int fragmentShader = MyRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
	    
	    final int pointVertexShader = MyRenderer.loadShader(GLES20.GL_VERTEX_SHADER, pointVertexShaderCode);                
        final int pointFragmentShader = MyRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShaderCode);

	    mPerVertexProgramHandle = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(mPerVertexProgramHandle, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(mPerVertexProgramHandle, fragmentShader); // add the fragment shader to program
	    GLES20.glBindAttribLocation(mPerVertexProgramHandle, 0, "vPosition");
	    GLES20.glBindAttribLocation(mPerVertexProgramHandle, 1, "vColor");
	    GLES20.glBindAttribLocation(mPerVertexProgramHandle, 2, "vNormal");
	    GLES20.glLinkProgram(mPerVertexProgramHandle);                  // creates OpenGL ES program executables
	    
	    mPointProgramHandle = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(mPointProgramHandle, pointVertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(mPointProgramHandle, pointFragmentShader); // add the fragment shader to program
	    GLES20.glBindAttribLocation(mPointProgramHandle, 0, "vPosition");
	    GLES20.glLinkProgram(mPointProgramHandle);                  // creates OpenGL ES program executables
	}
	
	public void draw(float[] modelViewProjectionMatrix, float[] mLightPosInEyeSpace, float[] modelViewMatrix) {
		

	    // get handle to vertex shader's vPosition member
	    mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "vPosition");
	    mNormalHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "vNormal");
	    // get handle to fragment shader's vColor member
	    mColourLocation = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "vColor");
	    mLightPosHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "uLightPos");
	    
	    // Prepare the triangle coordinate data
	    GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
	                                 GLES20.GL_FLOAT, false,
	                                 vertexStride, vertexBuffer);
	    // Enable a handle to the triangle vertices
	    GLES20.glEnableVertexAttribArray(mPositionHandle);
	    
	    GLES20.glVertexAttribPointer(mNormalHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 
	    		vertexStride, vertexNormalBuffer);

	    GLES20.glEnableVertexAttribArray(mNormalHandle);
	    
	    // Set colour for drawing the triangle
	    GLES20.glUniform4fv(mColourLocation, 1, colour, 0);
	    
	    mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "uMVMatrix");
	    MyRenderer.checkGlError("glGetUniformLocation");
	    
	    GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, modelViewMatrix, 0);
	    MyRenderer.checkGlError("glUniformMatrix4fv");
	    
	    // get handle to shape's transformation matrix
	    mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "uMVPMatrix");
	    MyRenderer.checkGlError("glGetUniformLocation");

	    // Apply the projection and view transformation
	    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, modelViewProjectionMatrix, 0);
	    MyRenderer.checkGlError("glUniformMatrix4fv");
	    
	    GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

	    // Draw the object
	    GLES20.glDrawElements(GLES20.GL_TRIANGLES, MainActivity.drawListArray.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

	    // Disable vertex array
	    GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
	
	public void drawLight(float[] modelViewProjectionMatrix, float[] mLightPosInModelSpace){
		
		
		final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(mPointProgramHandle, "uMVPMatrix");
		MyRenderer.checkGlError("glGetUniformLocation");
        final int pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle, "vPosition");
        
        // Pass in the position.
        GLES20.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2]);

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(pointPositionHandle);  
                       
        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, modelViewProjectionMatrix, 0);
        MyRenderer.checkGlError("glUniformMatrix4fv");
                
        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
	}
}