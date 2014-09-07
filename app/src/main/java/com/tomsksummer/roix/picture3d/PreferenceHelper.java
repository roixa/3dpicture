package com.tomsksummer.roix.picture3d;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bsr on 07.09.14.
 */
public class PreferenceHelper {
    //private params may changed from ui

    //////accelModule
    public static final float  sensetivity=70f;
    public static final int weight=6;// time of average sensor data
    ////coordHelper
    public static final float focuse=0.17f;//1/a a is dist from cam to focuse
    /////pictureRenderer
    private static final int currentPictureId=R.drawable.bustygs;//from res
    ////pictureModel
    private static final int dimension=42;//max edge num of polygons in 3d view
    public  static final float surfaceSize=4f;//size in gl coordinates
    public static final float scaleFactor=8f;//scale bitmap for correct load in gl
    public static final float fingerSize=0.05f;//det relative draw area when touched
    private static final float shift=0.2f;//relative size of max shift
    public static final int savedSteps=10;
    ///////////////////
    private static final String PREFS_NAME="picture3dprefs";
    private static final String KEY_PICTURE_ID="key_picture_id";
    private static final String KEY_SHIFT="key_shift";
    private static final String KEY_DIMENSION="key_dimension";

    private static Context context;


    public static void setContext(Context c){
        context=c;
    }

    public static int getCurrentPictureResId(){
        int ret=getIntFromPrefs(KEY_PICTURE_ID,0);
        if (ret==0) ret=currentPictureId;
        return ret;
    }

    public static float getShift(){
        return shift;
    }

    public static int getDimension(){
        return dimension;
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
