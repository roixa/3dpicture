package com.tomsksummer.roix.picture3d.GLEngine;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;


/**
 * Created by root on 05.08.14.
 */
public class Texture {
    //GLGraphics glGraphics;
    //FileIO fileIO;
    GL10 gl;
    String fileName;
    int textureId;
    int minFilter;
    int magFilter;
    Bitmap bitmap;
    public Texture(GL10 glGame, Context context,int bitmapId) {
        this.gl = glGame;
        //this.fileIO = glGame.getFileIO();
        this.fileName = fileName;
        this.bitmap=decodeBitmap(bitmapId,context);
        load();
    }
    private void load() {
        //gl = glGraphics.getGL();
        int[] textureIds = new int[1];
        gl.glGenTextures(1, textureIds, 0);
        textureId = textureIds[0];


            //Bitmap bitmap = BitmapFactory.decodeStream(in);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

    }

    public void reload() {
        load();
        bind();
        setFilters(minFilter, magFilter);

        //glGraphics.getGL().glBindTexture(GL10.GL_TEXTURE_2D, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
    }
    public void setFilters(int minFilter, int magFilter) {
        this.minFilter = minFilter;
        this.magFilter = magFilter;
        //GL10 gl = glGraphics.getGL();
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                minFilter);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                magFilter);
    }

    public void bind() {
        //GL10 gl = glGraphics.getGL();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }

    public void dispose() {
        //GL10 gl = glGraphics.getGL();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        int[] textureIds = { textureId };
        gl.glDeleteTextures(1, textureIds, 0);
    }

    private Bitmap decodeBitmap(int bitmapId,Context context){
        return BitmapFactory.decodeResource(context.getResources(), bitmapId);

    }
}

