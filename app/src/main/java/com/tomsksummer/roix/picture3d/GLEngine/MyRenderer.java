package com.tomsksummer.roix.picture3d.GLEngine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.tomsksummer.roix.picture3d.GLEngine.models3d.Cube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by bsr on 04.08.14.
 */
public class MyRenderer  implements GLSurfaceView.Renderer{

    private Cube cube;
    private static float angleCube = 0;     // rotational angle in degree for cube
    private static float speedCube = -1.5f; // rotational speed for cube
    private float mAngle;
    public AccelModule base;
    public Context context;
    private Texture box;
    public void setContext(Context context) {
        base=new AccelModule();
        this.context = context;
        cube=new Cube();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

        cube.loadTexture(gl, context);    // Load image into Texture (NEW)
        gl.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture (NEW)


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float)width / height;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset

        // You OpenGL|ES display re-sizing code here

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);



        gl.glLoadIdentity();                  // Reset the current model-view matrix
        gl.glTranslatef(0.0f, 0.0f, -6.0f);   // Translate into the screen
        //gl.glRotatef(angleCube, 0.1f, 1.0f, 0.2f); // Rotate
        //Log.d("MyRenderer",String.valueOf(base.getXAngle()));
        gl.glRotatef(base.getXAngle(), 1.0f, 0.0f, 0.0f); // Rotate accel x
        gl.glRotatef(base.getYAngle(), 0.0f, 1.0f, 0.0f); // Rotate accel y
        gl.glRotatef(base.getZAngle(), 0.0f, 0.0f, 1.0f); // Rotate accel y
        cube.draw(gl);

        // Update the rotational angle after each refresh.
        angleCube += speedCube;



    }
    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }
}
