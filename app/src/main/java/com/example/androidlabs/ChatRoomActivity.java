package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    MyListAdapter adapter;
    private ArrayList<String> elements=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ListView List=findViewById(R.id.TheListView);
        List.setAdapter(adapter= new MyListAdapter());
    }

    class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {//get called first
            return 5;//elements.size(); //how many time it's gonna get called
        }

        @Override
        public String getItem(int position) {//get called second
            return "This is row:";//+elements.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long)position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            TextView tv = new TextView(ChatRoomActivity.this);
//            tv.setText(getItem(position));
            LayoutInflater inflater=getLayoutInflater();
           View newView= inflater.inflate(R.layout.row_layout, parent, false);
           TextView tv=newView.findViewById(R.id.TextGoesHere);
           tv.setText(getItem(position));
            return newView;
        }
    }
}
