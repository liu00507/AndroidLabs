package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3);
        Button btn=findViewById(R.id.logbutton);
        Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = prefs.getString("EmailAddress", "liu00507@algonquinlive.com");
        EditText typeField = findViewById(R.id.TypeEmail);
        typeField.setText(savedString);
        btn.setOnClickListener(click->{
            saveSharedPrefs(typeField.getText().toString());
            goToProfile.putExtra("Email",prefs.getString("EmailAddress","liu00507@algonquinlive.com"));
            startActivity(goToProfile);
        });

//       final LinearLayout GL=findViewById(R.id.linear);
//        final GridLayout GL=findViewById(R.id.grid);
//       final RelativeLayout GL=findViewById(R.id.relative);
//        Button btn=findViewById(R.id.button2);
//        final Switch sw =findViewById(R.id.switch1);
//        sw.setChecked(false);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {Toast.makeText(MainActivity.this, R.string.toast_message, Toast.LENGTH_LONG).show();}
//        });
//        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton cb, final boolean isChecked) {
//                Snackbar sb=Snackbar.make(GL,"The switch is now "+(isChecked? " on":"off"),Snackbar.LENGTH_LONG);
//                sb.setAction("Undo", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sw.setChecked(!isChecked);
//                    }
//                });
//                sb.show();
//            }
//        });
    }
    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("EmailAddress", stringToSave);
        editor.commit();
    }
}