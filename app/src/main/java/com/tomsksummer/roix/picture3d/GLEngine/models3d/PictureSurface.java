package com.tomsksummer.roix.picture3d.GLEngine.models3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.tomsksummer.roix.picture3d.GLEngine.items.PictureModel;
import com.tomsksummer.roix.picture3d.GLEngine.items.Vertices;
import com.tomsksummer.roix.picture3d.PreferenceHelper;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by root on 06.08.14.
 */
public class PictureSurface {

    private PictureModel model;
    private Context context;
    private int[] textureIDs;


    float[] texCoords = { // Texture coords for the above face in all polygons
            0.0f, 1.0f,  // A. left-bottom
            1.0f, 1.0f,  // B. right-bottom
            0.0f, 0.0f,  // C. left-top
            1.0f, 0.0f   // D. right-top
    };

    private float[] color = { // Colors for the vertices (NEW)

            0.0f, 1.0f, 0.0f, 1.0f, // Green (NEW)

    };

    public void buttonPushedBack(){
        model.returnSurfaceState();

    }

    public PictureSurface (Context context,int pictureId){
        //model=new PictureModel();
        this.context=context;
        Bitmap picture= BitmapFactory.decodeResource(context.getResources(), pictureId);
        model =new PictureModel(picture, PreferenceHelper.getDimension());
        textureIDs=new int[model.getTextures().size()];
    }

    public void loadTextures(GL10 gl){//load inside gl
        ArrayList <Bitmap> shapes=model.getTextures();
        gl.glGenTextures(shapes.size(), textureIDs, 0);

        // Generate OpenGL texture images
        for (int i = 0; i < shapes.size(); i++) {
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[i]);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
            // Build Texture from loaded bitmap for the currently-bind texture ID
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, shapes.get(i), 0);

            //shapes.get(i).recycle();

        }



    }

    public void touch(float x,float y,float z){
        model.touch(x,y,z);
    }

    public void untouch(){
        model.untouch();
    }

    public void loadTexture(GL10 gl, Context context) {
        gl.glGenTextures(1, textureIDs, 0); // Generate texture-ID array

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);   // Bind to texture ID
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10. GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        // Construct an input stream to texture image "res\drawable\nehe.png"
        //InputStream istream = context.getResources().openRawResource(t5snowtex01);
        Bitmap bitmap;

        // Read and decode input as bitmap
        //bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.t5snowtex01);
        bitmap =model.getTextures().get(4);

        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        //bitmap.recycle();
    }

    public void draw(GL10 gl){
        gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
        gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
        gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)
        ArrayList <Vertices> vert=model.getPolygons();


        for (int i=0;i<vert.size();i++){
            drawPolygon(vert.get(i),i,gl);
        }


        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array (NEW)
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }


    private void drawPolygon(Vertices v,int pos,GL10 gl){

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, v.en());
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array (NEW)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, enBuffer(texCoords)); // Define texture-coords buffer (NEW)


        if (!v.isTouched()) {
            gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }

        else {
            gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);

        }

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[pos]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

    }



    private FloatBuffer enBuffer(float [] t){//encode
        FloatBuffer texBuffer;
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        texBuffer = tbb.asFloatBuffer();
        texBuffer.put(texCoords);
        texBuffer.position(0);
        return  texBuffer;
    }





}
