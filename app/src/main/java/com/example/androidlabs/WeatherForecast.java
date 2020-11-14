package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherForecast extends AppCompatActivity {
    ProgressBar progressBar;
    TextView value,min,max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        progressBar=findViewById(R.id.ProgBar);
        progressBar.setVisibility(View.VISIBLE);
        value=findViewById(R.id.temp);
        min=findViewById(R.id.Mintemp);
        max=findViewById(R.id.Maxtemp);
        ForecastQuery req = new ForecastQuery(); //creates a background thread
        req.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }
                                                //type1     2       3
    private class ForecastQuery extends AsyncTask<String, Integer, String> {

                                    //Type1
        public String doInBackground(String ... args)//same as String[]args, '...' means multiple elements notation
        {
            String value =null,min=null,max=null;
            try {
                //String encoded = args[0] + URLEncoder.encode(args[1], "UTF-8");
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8"); //response is data from the server

                String iconName = null;


                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature"))
                        {
                            //If you get here, then you are pointing to a <Weather> start tag
                             value =xpp.getAttributeValue(null,    "value");
                            publishProgress(25);
                             min =xpp.getAttributeValue(null,    "min");
                            publishProgress(50);
                             max =xpp.getAttributeValue(null,    "max");
                            publishProgress(75);
                        }
                        else if(xpp.getName().equals("weather")){
                            iconName=xpp.getAttributeValue(null,    "icon");

                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }
                Bitmap image = null;
                URL url2 = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                    publishProgress(100);
                }
                FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();

//                public boolean fileExistance(String fname){
//                    File file = getBaseContext().getFileStreamPath(fname);
//                    return file.exists();   }


            }
            catch (Exception e)
            {

            }

            return value+" "+min+" "+max;//goes to String fromDoInBackground in onPostExecute()
        }//do in background(calc and other stuff)

        //Type 2
        public void onProgressUpdate(Integer ... args)//update gui
        {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            value.setText("Temperture: "+fromDoInBackground.split(" ")[0]);
            min.setText("Minimum Temperture: "+fromDoInBackground.split(" ")[1]);
            max.setText("Maxmum Temperture: "+fromDoInBackground.split(" ")[2]);
            progressBar.setVisibility(View.INVISIBLE);
           //Log.i("HTTP", fromDoInBackground);
        }//gets called after doinbackground and no more progress update
    }
}