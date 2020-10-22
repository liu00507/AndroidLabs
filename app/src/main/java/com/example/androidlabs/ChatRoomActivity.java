package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {
    MyListAdapter adapterTx;
    private ArrayList<Msg> elements=new ArrayList<>();
    boolean isSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ListView List=findViewById(R.id.TheListView);
        EditText text=findViewById(R.id.ChatInput);
        Button send=findViewById(R.id.Txbtn);
        Button receive=findViewById(R.id.Rebtn);
        List.setAdapter(adapterTx= new MyListAdapter());
        send.setOnClickListener(click->{
            isSend=true;
            elements.add(new Msg(text.getText().toString(),isSend));
            adapterTx.notifyDataSetChanged();
            text.getText().clear();
        });
        receive.setOnClickListener(click->{
            isSend=false;
            elements.add(new Msg(text.getText().toString(),isSend));
            adapterTx.notifyDataSetChanged();
            text.getText().clear();
        });
        List.setOnItemLongClickListener((p,b,pos,id)->{
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("Do you want to delete this?").setMessage("The selected row is "+pos+" and The database id id: "+id)
            .setPositiveButton("Yes",(click,arg)->{
                elements.remove(pos);
                adapterTx.notifyDataSetChanged();
            })
            .setNegativeButton("No",(click,arg)->{})
            .setView(getLayoutInflater().inflate(R.layout.row_layout,null)).create().show();
                    return true;
        });
    }

    class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {//get called first
            return elements.size();//5; //how many time it's gonna get called
        }

        @Override
        public Msg getItem(int position) {//get called second
            return elements.get(position);//+elements.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long)position;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            Msg msg=getItem(position);
            View newView;
            if(msg.getState())
                newView=inflater.inflate(R.layout.row_layout, parent, false);
            else{
                newView=inflater.inflate(R.layout.row_layout2, parent, false);
            }
           TextView tv=newView.findViewById(R.id.TextGoesHere);
           tv.setText(getItem(position).getMessage());
            return newView;
        }
    }

    private class Msg{
        Boolean state;
        String message;
        public Msg(String message, boolean state){
            this.message=message;
            this.state=state;
        }
        public String getMessage(){
            return message;
        }
        public boolean getState(){
            return state;
        }
    }

}
