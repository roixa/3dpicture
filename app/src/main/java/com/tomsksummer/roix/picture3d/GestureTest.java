package com.tomsksummer.roix.picture3d;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import com.tomsksummer.roix.picture3d.R;

import java.util.ArrayList;

public class GestureTest extends Activity  {

    ScaleGestureDetector SGD;
    GestureDetector myG;//gestures





    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SGD = new ScaleGestureDetector(this,new ScaleListener());
        myG = new GestureDetector(this,new Gesture());

        //setContentView(gestureOverlayView);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        SGD.onTouchEvent(event);
        myG.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }


    class Gesture extends GestureDetector.SimpleOnGestureListener{
        public boolean onSingleTapUp(MotionEvent ev) {
            return true;
        }
        public void onLongPress(MotionEvent ev) {

        }
        private float dist=0;
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {
            if(e1.getPointerCount()>1||e2.getPointerCount()>1){
                return false;
            }
            dist+=distanceX;
            Log.d("scale","on scroll"+String.valueOf(dist));
            return true;
        }
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return true;
        }
        }




    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            String a="dasda";
            Log.d("scale ", String.valueOf(scale));
            return true;
        }
    }

}
