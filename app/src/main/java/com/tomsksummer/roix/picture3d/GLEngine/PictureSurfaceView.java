package com.tomsksummer.roix.picture3d.GLEngine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.tomsksummer.roix.picture3d.interfaces.TouchSurfaceListener;

import java.nio.FloatBuffer;

/**
 * Created by root on 06.08.14.
 */
public class PictureSurfaceView extends GLSurfaceView {

    public final String TAG="PictureSurfaceView";
    public  PictureRenderer renderer;
    private boolean modeIsDraw;
    private ScaleGestureDetector SGD;
    private GestureDetector myG;//gestures
    private float width;
    private float height;

    public PictureSurfaceView(Context context, AttributeSet attrs) {
        super(context,  attrs);
        renderer = new PictureRenderer();
        renderer.setContext(this.getContext());
        setEGLContextClientVersion(1);
        setRenderer(renderer);
        SGD = new ScaleGestureDetector(this.getContext(),new ScaleListener());
        myG = new GestureDetector(this.getContext(),new Gesture());
        // Render the view only when there is a change in the drawing data

        modeIsDraw=true;

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        requestRender();
        //setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        Log.d(TAG,String.valueOf(width));

    }

    /*public PictureSurfaceView(Context context) {
        super(context);
        renderer = new PictureRenderer();
        renderer.setContext(this.getContext());
        setEGLContextClientVersion(1);
        setRenderer(renderer);

        // Render the view only when there is a change in the drawing data

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }*/



    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, we are only
        // interested in events where the touch position changed.
        /*width=this.getWidth();
        height=this.getHeight();*/
        width=this.getWidth();
        height=this.getHeight();
        Log.d(TAG,String.valueOf(width));
        SGD.onTouchEvent(e);
        myG.onTouchEvent(e);
        TouchSurfaceListener touchListener=renderer;

        float x = e.getX();
        float y = e.getY();


        //this convert to normal coords [-1,1]


        float nx=x*2/width-1f;
        float ny=y*2/height-1f;

        Log.d(TAG,String.valueOf(width));

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchListener.OnStartTouch();
                requestRender();
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d(TAG,"on touch x, y: "+String.valueOf(x)+" "+String.valueOf(y));
                //requestRender();

                //-1 for in gl y inverted
                touchListener.OnTouch(nx,-ny);

                break;
            case MotionEvent.ACTION_UP:
                touchListener.OnEndTouch();
        }


        return true;
    }

    public boolean  changeMode(){
        modeIsDraw=!modeIsDraw;
        renderer.changeMode();
        return modeIsDraw;
    }

    public void setDrawMode(){
        modeIsDraw=true;
        renderer.setMode(modeIsDraw);
    }

    public void setMoveMode(){
        modeIsDraw=false;
        renderer.setMode(modeIsDraw);
    }
    public void buttonPushedBack(){
        renderer.buttonPushedBack();
    }




    private class Gesture extends GestureDetector.SimpleOnGestureListener{
        private static final float scaleFactor=0.011f;

        public boolean onSingleTapUp(MotionEvent ev) {
            return true;
        }
        public void onLongPress(MotionEvent ev) {}

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {
            if(modeIsDraw){
                return false;
            }
            if(e1.getPointerCount()>1||e2.getPointerCount()>1){
                return false;
            }
            Log.d(TAG,String.valueOf(width));
            float nx=distanceX*2f/width;
            float ny=distanceY*2f/height;
            int sx=nx>0?1:-1;
            int sy=ny>0?-1:1;

            float dx=scaleFactor*sx;
            float dy=scaleFactor*sy;
            renderer.moveCam(dx,dy);
            requestRender();
            Log.d("scale","on scroll"+String.valueOf(nx));
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return true;
        }
    }




    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float startedScaleFactor;
        private static final float scaleDeepFactor=0.05f;

        // Detects that new pointers are going down.
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            if(modeIsDraw){
                return false;
            }
            startedScaleFactor=detector.getScaleFactor();

            return true;
        }


        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if(modeIsDraw){
                return false;
            }
            float scale = detector.getScaleFactor();
            //int direct=startedScaleFactor>scale?1:-1;//pinch,zoom
            float deep=startedScaleFactor-scale;
            float add=scaleDeepFactor*deep;//direct*deep;
            renderer.zoomCam(add);
            Log.d("scale ", String.valueOf(startedScaleFactor-scale));
            return false;

        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }

}
