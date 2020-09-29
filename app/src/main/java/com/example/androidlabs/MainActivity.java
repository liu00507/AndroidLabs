package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);
        Button btn=findViewById(R.id.button2);
        Switch sw =findViewById(R.id.switch1);
        sw.setChecked(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Toast.makeText(MainActivity.this, R.string.toast_message, Toast.LENGTH_LONG).show();}
        });
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                Snackbar.setAction( "Undo", click -> sw.setChecked(!isChecked));
            }
        });
    }

}