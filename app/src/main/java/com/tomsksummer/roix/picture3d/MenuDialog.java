package com.tomsksummer.roix.picture3d;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by bsr on 07.09.14.
 */
public class MenuDialog extends DialogFragment implements View.OnClickListener{
    private Button loadPhotoButton;
    private Button setToWallpaperButton;
    private Button takeAPhotoButton;
    private Button settingsButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_dialog, container);
        loadPhotoButton=(Button)view.findViewById(R.id.loadPhotoButton);
        setToWallpaperButton=(Button)view.findViewById(R.id.setWallpaperButton);
        takeAPhotoButton=(Button)view.findViewById(R.id.makePhotoButton);
        settingsButton=(Button)view.findViewById(R.id.settingsButton);

        getDialog().setTitle(R.string.menu);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        loadPhotoButton.setOnClickListener(this);
        setToWallpaperButton.setOnClickListener(this);
        takeAPhotoButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loadPhotoButton:
                FragmentActivity a=getActivity();
                Intent intent=new Intent(a,LoadPictureActivity.class);

                startActivity(intent);
                a.finish();

        }

    }
}
