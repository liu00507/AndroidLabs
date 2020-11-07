package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ListView List=findViewById(R.id.TheListView);
        EditText text=findViewById(R.id.ChatInput);
        Button send=findViewById(R.id.Txbtn);
        Button receive=findViewById(R.id.Rebtn);
        loadDataFromDatabase();
        List.setAdapter(adapterTx= new MyListAdapter());
        send.setOnClickListener(click->{
            isSend=true;
            String msg=text.getText().toString();
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_MSG, msg);
            newRowValues.put(MyOpener.COL_TYPE, 1);
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            Msg message=new Msg(msg,newId,true);
            elements.add(message);
            adapterTx.notifyDataSetChanged();
            text.getText().clear();
        });
        receive.setOnClickListener(click->{
            isSend=false;
            String msg=text.getText().toString();
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_MSG, msg);
            newRowValues.put(MyOpener.COL_TYPE, 0);
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            Msg message=new Msg(msg,newId,false);
            elements.add(message);
            adapterTx.notifyDataSetChanged();
            text.getText().clear();
        });
        List.setOnItemLongClickListener((p,b,pos,id)->{
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("Do you want to delete this?").setMessage("The selected row is "+pos+" and The database id id: "+id)
            .setPositiveButton("Yes",(click,arg)->{
                deleteMessage(elements.get(pos));
                elements.remove(pos);
                adapterTx.notifyDataSetChanged();
            })
            .setNegativeButton("No",(click,arg)->{})
            .setView(getLayoutInflater().inflate(R.layout.row_layout,null)).create().show();
                    return true;
        });
    }

    protected void deleteMessage(Msg c)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }

    class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {//get called firstreturn elements.size();//5; //how many time it's gonna get called
            return elements.size();
        }

        @Override
        public Msg getItem(int position) {//get called second
            return elements.get(position);//+elements.get(position);
        }

        @Override
        public long getItemId(int position) {
//            String [] columns = {MyOpener.COL_TYPE, MyOpener.COL_MSG, MyOpener.COL_ID};
//            Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
//
//            int MsgColumnIndex = results.getColumnIndex(MyOpener.COL_MSG);
//            //int TypeColIndex = results.getColumnIndex(MyOpener.COL_TYPE);
//            int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
//
//            while(results.moveToNext())
//            {
//                if(elements.get(position).getMessage().equals(results.getString(MsgColumnIndex)))
//                    return results.getLong(idColIndex);
//            }
//           return -1;
            return getItem(position).getId();
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
    private void loadDataFromDatabase(){
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer

        String [] columns = {MyOpener.COL_TYPE, MyOpener.COL_MSG, MyOpener.COL_ID};
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int MsgColumnIndex = results.getColumnIndex(MyOpener.COL_MSG);
        int TypeColIndex = results.getColumnIndex(MyOpener.COL_TYPE);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        while(results.moveToNext())
        {
            Boolean state;
            String message = results.getString(MsgColumnIndex);
            int type = results.getInt(TypeColIndex);
            long id = results.getLong(idColIndex);
            if(type==1) state=true;
            else state=false;
            //add the new Contact to the array list:
            elements.add(new Msg(message, id, state));
        }
    }
    private class Msg{
        Boolean state;
        long id;
        String message;

        public Msg(String message, long id, boolean state){
            this.message=message;
            this.id=id;
            this.state=state;
        }
        public String getMessage(){
            return message;
        }
        public boolean getState(){
            return state;
        }
        public long getId() {
            return id;
        }
    }
    private void printCursor(Cursor c, int version){
        System.out.println("DB version: "+db.getVersion());
        System.out.println("Total columns" + 	c.getColumnCount());
        System.out.println("Column names are: "+c.getColumnName(0)+" "+c.getColumnName(1)+" "+c.getColumnName(2));
        System.out.println("Total rows"+c.getCount());

    }

}
