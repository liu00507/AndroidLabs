package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);
       // LinearLayout LL=findViewById(R.id.linear);
        final GridLayout GL=findViewById(R.id.grid);
       // RelativeLayout RL=findViewById(R.id.relative);
        Button btn=findViewById(R.id.button2);
        final Switch sw =findViewById(R.id.switch1);
        sw.setChecked(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Toast.makeText(MainActivity.this, R.string.toast_message, Toast.LENGTH_LONG).show();}
        });
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, final boolean isChecked) {
                Snackbar sb=Snackbar.make(GL,"The switch is now "+(isChecked? "on":"off"),Snackbar.LENGTH_LONG);
                sb.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sw.setChecked(!isChecked);
                    }
                });
                sb.show();
            }
        });
    }

}