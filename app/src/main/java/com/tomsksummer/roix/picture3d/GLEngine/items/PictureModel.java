package com.tomsksummer.roix.picture3d.GLEngine.items;

import android.graphics.Bitmap;
import android.util.Log;

import com.tomsksummer.roix.picture3d.PreferenceHelper;

import java.util.ArrayList;

/**
 * Created by root on 06.08.14.
 */
public class PictureModel {

    public final  String TAG="PictureModel";


    private int width;//of base bitmap
    private int height;
    private int dimension;//max num of polygons in 3d view
    private int cellSize;//one polygon in pixels
    private float scale;//connect normal metric and pixels (or float)
    private float finger;//determ finger paint size
    private float yBiass;//to remove bug in horizontal photos
    /// second version

    private float scaleX;
    private float scaleY;
    private float cellSizeX;
    private float cellSizeY;
    float surfaceSizeX;
    float surfaceSizeY;

    private ArrayList<Bitmap> textures;
    private ArrayList<Vertices> polygons;//size not more dimension ^2
    private ArrayList<Vertices> previousPolygons;
    private ArrayList <ArrayList<Vertices> > previousSteps;

    /**
     * all scene range is [-surfaceSize/2 ;surfaceSize/2]
     * */



    public PictureModel (Bitmap bitmap,int dimension){

        this.dimension=dimension;
        bitmap=scaleBitmap(bitmap);
        previousPolygons=new ArrayList<Vertices>();
        previousSteps=new ArrayList<ArrayList<Vertices>>();
        width=bitmap.getWidth();
        height=bitmap.getHeight();
        textures=new ArrayList<Bitmap>();
        polygons=new ArrayList<Vertices>();
        generatePolygons();
        generateTextures(bitmap);
    }



    public ArrayList <Bitmap> getTextures(){
        return textures;
    }

    public ArrayList<Vertices> getPolygons(){
        return polygons;
    }

    public void touch(float x,float y,float z){//z is camz
        finger= PreferenceHelper.fingerSize*z;

        x=x-finger/2f;
        y=y-finger/2f;
        /*
        Vertices fingerArea=new Vertices(x,y,finger);
        Log.d(TAG,String.valueOf(x));
        for (Vertices v:polygons){
            if(v.crossedWith(fingerArea)) v.setTouch(true);
        }
        */
        for (Vertices v:polygons){
            if(v.crossedWithCircle(x,y,finger)) v.setTouch(true);
        }
    }

    public void untouch(){
        previousPolygons=copy(polygons);
        previousSteps.add(previousPolygons);
        bendSurface(PreferenceHelper.getShift());
        disableTouchPolygons();
    }

    public void returnSurfaceState(){
        ArrayList <Vertices> vert=new ArrayList<Vertices>();
        if(previousSteps.size()==0) return;
        vert=previousSteps.get(previousSteps.size()-1);

        previousSteps.remove(vert);

        polygons=copy(vert);

    }

    private void disableTouchPolygons(){
        for (Vertices v:polygons){
             v.setTouch(false);
        }
    }


    private ArrayList <Vertices> copy(ArrayList <Vertices> v){
        ArrayList <Vertices> r=new ArrayList<Vertices>();
        for (Vertices vert:v){
            Vertices c=new Vertices(vert);
            r.add(c);
        }
        return r;
    }

    private Bitmap scaleBitmap(Bitmap bitmap){
        int w=bitmap.getWidth();
        int h=bitmap.getHeight();
        float largest=h>w?h:w;
        float scale=PreferenceHelper.scaleFactor*(float)dimension/largest;

        return Bitmap.createScaledBitmap(bitmap,(int)(scale*w),(int)(scale*h),true);
    }

    private void bendSurface(float shift){
        for (Vertices v:polygons){
            if(v.isTouched()) {
                float a=shift/2f;
                float b=2.3f/finger;
                float lt=(float)Math.pow(distanceToUntouchedArea(v.ltx,v.lty)*b,0.5)*a;
                float lb=(float)Math.pow(distanceToUntouchedArea(v.lbx,v.lby)*b,0.5)*a;
                float rb=(float)Math.pow(distanceToUntouchedArea(v.rbx,v.rby)*b,0.5)*a;
                float rt=(float)Math.pow(distanceToUntouchedArea(v.rtx,v.rty)*b,0.5)*a;
                if(shift>0){
                    lt=lt>shift?shift:lt;
                    lb=lb>shift?shift:lb;
                    rb=rb>shift?shift:rb;
                    rt=rt>shift?shift:rt;
                }
                else{
                    lt=lt<shift?shift:lt;
                    lb=lb<shift?shift:lb;
                    rb=rb<shift?shift:rb;
                    rt=rt<shift?shift:rt;
                }
                v.shift(lb,rb,lt,rt);
            }
        }
    }

    private float distanceToUntouchedArea(float x,float y){
        float dist=10000000;
        for (Vertices v:polygons){
            if(!v.isTouched()){
                float tem=v.distanceTo(x,y);
                dist=dist<tem?dist:tem;
            }
        }
        return dist;
    }

    private void generatePolygons(){
        ////all in normal coordinates

        surfaceSizeY=width>height?PreferenceHelper.surfaceSize:(PreferenceHelper.surfaceSize*(float)height/(float)width);
        surfaceSizeX=width<height?PreferenceHelper.surfaceSize:(PreferenceHelper.surfaceSize*(float)width/(float)height);
        float xPos=0;
        float yPos=0;
        float stepX=surfaceSizeX/(float)dimension;
        float stepY=surfaceSizeY/(float)dimension;
        boolean generateDone=false;

        while (!generateDone){

            Vertices vertices = new Vertices( // Vertices for a face
                    xPos,         yPos,       0.0f,  // 0. left-bottom-front
                    (xPos+stepX),   yPos,       0.0f,  // 1. right-bottom-front
                    xPos,        (yPos+stepY), 0.0f,  // 2. left-top-front
                    (xPos+stepX),  (yPos+stepY), 0.0f   // 3. right-top-front
            );
            polygons.add(vertices);

            Log.d("Picture model","ypos:  "+String.valueOf(yPos)+" xpos: "+String.valueOf(xPos)+ "  "+String.valueOf((xPos+1f)*scale));

            /**
             * @TODO "cutting image"
             * */
            xPos+=stepX;

            boolean inTheEndX=(xPos>=surfaceSizeX);//!!! *2
            if(inTheEndX){
                xPos=0;
                yPos-=stepY;
            }
            boolean inTheEndY=(yPos<=-surfaceSizeY);
            generateDone=(inTheEndX&&inTheEndY);

        }

    }

    private void generateTextures(Bitmap bitmap){

        float stepX=surfaceSizeX/(float)dimension;
        float stepY=surfaceSizeY/(float)dimension;
        scaleX=(float)width/surfaceSizeX;
        scaleY=(float)height/surfaceSizeY;
        cellSizeX=stepX*scaleX;
        cellSizeY=stepY*scaleY;

        for(Vertices vertices:polygons){
            int xpos=(int)(vertices.lbx*scaleX);
            int ypos=-(int)(vertices.lby*scaleY);
            ypos=(ypos+(int)cellSizeY)>height?(height-(int)cellSizeY):ypos;
            xpos=(xpos+(int)cellSizeX)>width?(width-(int)cellSizeX):xpos;
            Log.d("Picture model",String.valueOf(xpos)+"  "+String.valueOf(ypos)+"  size:"+String.valueOf(width));
            textures.add(Bitmap.createBitmap(bitmap,xpos,ypos,(int)cellSizeX,(int)cellSizeY));
        }
    }

}
