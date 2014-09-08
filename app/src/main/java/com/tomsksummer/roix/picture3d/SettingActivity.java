package com.tomsksummer.roix.picture3d;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SettingActivity extends Activity implements View.OnClickListener{

    EditText editText;
    Button sqModeButton;
    Button save;
    Button cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        editText=(EditText)findViewById(R.id.settingsEditText);
        sqModeButton=(Button)findViewById(R.id.polModeButton);
        save=(Button)findViewById(R.id.settingsEnableButton);
        cancel=(Button)findViewById(R.id.settingCancelButton);
        sqModeButton.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        editText.setText(String.valueOf(PreferenceHelper.getDimension()));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.polModeButton:
                break;
            case R.id.settingsEnableButton:
                break;
            case R.id.settingCancelButton:
                break;


        }

    }

    private void startPictureActivity(){
        Intent intent=new Intent(SettingActivity.this,PictureActivity.class);
        startActivity(intent);
        finish();
    }
}
