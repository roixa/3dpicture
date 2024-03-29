package com.tomsksummer.roix.picture3d;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by bsr on 07.09.14.
 */
public class PreferenceHelper {
    //private params may changed from ui

    public static final boolean isDemo=true;

    //////accelModule
    public static final float  sensetivity=70f;
    public static final int weight=6;// time of average sensor data
    ////coordHelper
    public static final float focuse=0.17f;//1/a a is dist from cam to focuse
    /////pictureRenderer
    private static final int currentPictureId=R.drawable.bustygs;//from res
    ////pictureModel
    private static final int dimension=37;//max edge num of polygons in 3d view
    public  static final float surfaceSize=4f;//size in gl coordinates
    public static final float scaleFactor=8f;//scale bitmap for correct load in gl
    public static final float fingerSize=0.03f;//det relative draw area when touched
    private static final float shift=0.2f;//relative size of max shift
    public static final int savedSteps=10;
    public static  ArrayList<Integer> picturesResId;

    ///////////////////
    private static final String PREFS_NAME="picture3dprefs";
    private static final String KEY_PICTURE_ID="key_picture_id";
    private static final String KEY_SHIFT="key_shift";
    private static final String KEY_DIMENSION="key_dimension";
    private static final String KEY_MODE="mode";


    public static final int MODE_RECTANGLE=1;
    public static final int MODE_SQUARE=2;

    private static final int currentMode=MODE_SQUARE;

    private static Context context;

    public static void init( Context c){
        setContext(c);
        fillPicturesResId();
    }


    private static void setContext(Context c){
        context=c;
    }

    private static void fillPicturesResId(){
        picturesResId=new ArrayList<Integer>();
        picturesResId.add(R.drawable.bustygs);
        picturesResId.add(R.drawable.nature);
        picturesResId.add(R.drawable.verticalbridge);
        picturesResId.add(R.drawable.man);
        picturesResId.add(R.drawable.room);



    }

    public static ArrayList<Integer> getPicturesResId(){
        return picturesResId;
    }

    public static int getCurrentPictureResId(){
        int ret=getIntFromPrefs(KEY_PICTURE_ID,0);
        if (ret==0) ret=currentPictureId;
        return ret;
    }

    public static void saveCurrentPictureResId(int id){
        setIntInPrefs(KEY_PICTURE_ID,id);
    }

    public static float getShift(){
        float ret=getFloatFromPrefs(KEY_SHIFT,0);
        if (ret==0) ret=shift;
        return ret;
    }

    public static void saveShift(float s){
        setFloatInPrefs(KEY_SHIFT,s);
    }

    public static int getDimension(){
        int ret=getIntFromPrefs(KEY_DIMENSION,0);
        if (ret==0) ret=dimension;
        return ret;
    }

    public static void saveDimension(int dim){
        setIntInPrefs(KEY_DIMENSION,dim);
    }

    public static void saveMode(int mode){
        setIntInPrefs(KEY_MODE,mode);
    }

    public static int getMode(){
        int ret=getIntFromPrefs(KEY_MODE,0);
        if (ret==0) ret=currentMode;
        return ret;
    }




    private static String getStringFromPrefs(String key, String def){
        return getPreferences().getString(key,def);
    }
    private static void setStringInPrefs(String key, String val){
        getPreferences().edit().putString(key, val).commit();
    }

    private static int getIntFromPrefs(String key, int def){
        return getPreferences().getInt(key, def);
    }
    private static void setIntInPrefs(String key, int val){
        getPreferences().edit().putInt(key, val).commit();
    }

    private static boolean getBooleanFromPrefs(String key, boolean def){
        return getPreferences().getBoolean(key, def);
    }

    private static void setBooleanInPrefs(String key, boolean val){
        getPreferences().edit().putBoolean(key, val).commit();
    }

    private static float getFloatFromPrefs(String key, float def){
        return getPreferences().getFloat(key, def);
    }

    private static void setFloatInPrefs(String key, float val){
        getPreferences().edit().putFloat(key, val).commit();
    }

    private static SharedPreferences getPreferences(){
        if(context!=null)
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return  null;
    }


}
