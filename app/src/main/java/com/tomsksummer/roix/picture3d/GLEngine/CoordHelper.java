package com.tomsksummer.roix.picture3d.GLEngine;

import com.tomsksummer.roix.picture3d.GLEngine.items.PictureModel;

/**
 * Created by root on 07.08.14.
 */
public class CoordHelper {
    public float camX;
    public float camY;
    public float camZ;
    private final float focuse=0.17f;//1/a a is dist from cam to focuse

    public CoordHelper (float x,float y,float z){
        camX=x; camY=y; camZ=z;
    }

    /**
     * this transfer from normal coords to gl coords on surface with distance camZ from cam
     * normal coords is [-1,1]
     * */

     public float getX(float nx){
         float scale = PictureModel.surfaceSize/2f;
         float focusScale=camZ*focuse;
         float x=nx*scale*focusScale+camX;
         return x;
     }

    public float getY(float ny){
        float scale = PictureModel.surfaceSize/2f;
        float focusScale=camZ*focuse;
        float y=ny*scale*focusScale+camY;
        return y;
    }


    /**
     * to calc camera moves in xy surface
     * */

     public float getCamXAdd(float nx){
        float scale = PictureModel.surfaceSize/2f;
        float focusScale=camZ*focuse;
        float x=nx*scale*focusScale;//!!
        return x;
    }

    public float getCamYAdd(float ny){
        float scale = PictureModel.surfaceSize/2f;
        float focusScale=camZ*focuse;
        float y=ny*scale*focusScale;//!!
        return y;
    }



}
