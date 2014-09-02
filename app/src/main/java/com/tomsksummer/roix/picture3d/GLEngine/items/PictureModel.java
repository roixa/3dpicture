package com.tomsksummer.roix.picture3d.GLEngine.items;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by root on 06.08.14.
 */
public class PictureModel {

    public final  String TAG="PictureModel";
    public  static final float surfaceSize=4f;//size in gl coordinates
    private static final float scaleFactor=8f;
    private static final float fingerSize=0.05f;
    private static final float shift=0.2f;


    private int width;//of base bitmap
    private int height;
    private int dimension;//max num of polygons in 3d view
    private int cellSize;//one polygon in pixels
    private float scale;//connect normal metric and pixels (or float)
    private float finger;//determ finger paint size
    private float yBiass;//to remove bug in horizontal photos

    private ArrayList<Bitmap> textures;
    private ArrayList<Vertices> polygons;//size not more dimension ^2
    private ArrayList<Vertices> previousPolygons;

    /**
     * all scene range is [-surfaceSize/2 ;surfaceSize/2]
     * */



    public PictureModel (Bitmap bitmap,int dimension){

        this.dimension=dimension;
        bitmap=scaleBitmap(bitmap);
        previousPolygons=new ArrayList<Vertices>();
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
        finger=fingerSize*z;

        x=x-finger/2f;
        y=y-finger/2f;
        Vertices fingerArea=new Vertices(x,y,finger);
        Log.d(TAG,String.valueOf(x));
        for (Vertices v:polygons){
            if(v.crossedWith(fingerArea)) v.setTouch(true);
        }
    }

    public void untouch(){
        previousPolygons=copy(polygons);
        bendSurface(this.shift);
        disableTouchPolygons();
    }

    public void returnSurfaceState(){
        polygons=copy(previousPolygons);

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
        float scale=scaleFactor*(float)dimension/largest;

        return Bitmap.createScaledBitmap(bitmap,(int)(scale*w),(int)(scale*h),true);
    }

    private void bendSurface(float shift){
        for (Vertices v:polygons){
            if(v.isTouched()) {
                float a=shift/2f;
                float b=4f/finger;
                float lt=(float)Math.pow(distanceToUntouchedArea(v.ltx,v.lty)*b,0.5)*a;
                float lb=(float)Math.pow(distanceToUntouchedArea(v.lbx,v.lby)*b,0.5)*a;
                float rb=(float)Math.pow(distanceToUntouchedArea(v.rbx,v.rby)*b,0.5)*a;
                float rt=(float)Math.pow(distanceToUntouchedArea(v.rtx,v.rty)*b,0.5)*a;
                lt=lt>shift?shift:lt;
                lb=lb>shift?shift:lb;
                rb=rb>shift?shift:rb;
                rt=rt>shift?shift:rt;
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

        int largest=width>height?width:height;
        cellSize=largest/dimension;


        float xPos=-surfaceSize/2;//position in normal coordinates [-1,1]
        float yPos=-surfaceSize/2;
        float step=surfaceSize/(float)dimension;
        scale=largest/surfaceSize;



        boolean generateDone=false;

        while (!generateDone){
            Vertices vertices = new Vertices( // Vertices for a face
                     xPos,         yPos,       0.0f,  // 0. left-bottom-front
                    (xPos+step),   yPos,       0.0f,  // 1. right-bottom-front
                     xPos,        (yPos+step), 0.0f,  // 2. left-top-front
                    (xPos+step),  (yPos+step), 0.0f   // 3. right-top-front
            );
            polygons.add(vertices);

            Log.d("Picture model","ypos:  "+String.valueOf(yPos)+" xpos: "+String.valueOf(xPos)+ "  "+String.valueOf((xPos+1f)*scale));

            /**
             * @TODO "cutting image"
             * */
            xPos+=step;

            boolean inTheEndX=(xPos>=surfaceSize/2)||(((xPos+surfaceSize/2)*scale)>=width);//!!! *2
            if(inTheEndX){
                xPos=-surfaceSize/2;
                yPos+=step;
            }
            boolean inTheEndY=(yPos>=surfaceSize/2)||(((yPos+surfaceSize/2)*scale)>=height);
            generateDone=(inTheEndX&&inTheEndY);

                if(generateDone&&width>height){yBiass=yPos;}//to remove bug

        }

    }

    private void generateTextures(Bitmap bitmap){

        float step=surfaceSize/(float)dimension;
        float cs=step*scale;
        for(Vertices vertices:polygons){
            int xpos=(int)((vertices.lbx+surfaceSize/2)*scale);
            int ypos=(int)((-vertices.lby+surfaceSize/2-yBiass)*scale);
            ypos=(ypos+(int)cs)>height?(height-(int)cs):ypos;
            xpos=(xpos+(int)cs)>width?(width-(int)cs):xpos;
            ypos=ypos<0?0:ypos;
            xpos=xpos<0?0:xpos;

            Log.d("Picture model",String.valueOf(xpos)+"  "+String.valueOf(ypos)+"  size:"+String.valueOf(width));
            //textures.add(Bitmap.createBitmap(bitmap,100,100,50,50));
            textures.add(Bitmap.createBitmap(bitmap,xpos,ypos,cellSize,cellSize));
        }


        /**
         * @TODO "inverted bitmap"
         * @TODO "large bitmap not load"
         * y axis is inverted now this looks up
         *
         * */

         /*for(Vertices vertices:polygons){

            int xpos=(int)((vertices.lbx+1f)*scale);

            int ypos=(int)((-vertices.lby+1f)*scale);

            ypos=(ypos+cellSize)>height?(height-cellSize):ypos;
            xpos=(xpos+cellSize)>width?(width-cellSize):xpos;

            Log.d("Picture model",String.valueOf(xpos)+"  "+String.valueOf(ypos)+"  size:"+String.valueOf(width));
            //textures.add(Bitmap.createBitmap(bitmap,100,100,50,50));
            textures.add(Bitmap.createBitmap(bitmap,xpos,ypos,cellSize,cellSize));
        }*/
    }


}
