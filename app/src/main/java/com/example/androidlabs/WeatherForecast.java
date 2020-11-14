package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherForecast extends AppCompatActivity {
    ProgressBar progressBar;
    TextView value,min,max,uv;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        progressBar=findViewById(R.id.ProgBar);
        progressBar.setVisibility(View.VISIBLE);
        value=findViewById(R.id.temp);
        min=findViewById(R.id.Mintemp);
        max=findViewById(R.id.Maxtemp);
        uv=findViewById(R.id.UVRating);
        image=findViewById(R.id.pic);
        ForecastQuery req = new ForecastQuery(); //creates a background thread
        req.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }
                                                //type1     2       3
    private class ForecastQuery extends AsyncTask<String, Integer, String> {
                                                    public boolean fileExistance(String fname){
                                                        File file = getBaseContext().getFileStreamPath(fname);
                                                        return file.exists();   }
                                                    Bitmap img = null;
                                                    //Type1
        public String doInBackground(String ... args)//same as String[]args, '...' means multiple elements notation
        {
            String value =null,min=null,max=null, UV=null;

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

                URL url2 = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    img = BitmapFactory.decodeStream(connection.getInputStream());
                    publishProgress(100);
                }
                FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                img.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();
                FileInputStream fis = null;
//                try {    fis = openFileInput(image);   }
//                catch (FileNotFoundException e) {    e.printStackTrace();  }
//                Bitmap bm = BitmapFactory.decodeStream(fis);
                URL url3 = new URL("https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection urlConnection2 = (HttpURLConnection) url3.openConnection();

                //wait for data:
                InputStream response2 = urlConnection2.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response2, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
               double uvRating = uvReport.getDouble("value");
               UV=String.valueOf(uvRating);

            }
            catch (Exception e)
            {

            }

            return value+" "+min+" "+max+" "+UV;//goes to String fromDoInBackground in onPostExecute()
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
            image.setImageBitmap(img);
            value.setText("Temperture: "+fromDoInBackground.split(" ")[0]);
            min.setText("Minimum Temperture: "+fromDoInBackground.split(" ")[1]);
            max.setText("Maxmum Temperture: "+fromDoInBackground.split(" ")[2]);
            uv.setText("UV Rating: "+fromDoInBackground.split(" ")[3]);
            progressBar.setVisibility(View.INVISIBLE);
           //Log.i("HTTP", fromDoInBackground);
        }//gets called after doinbackground and no more progress update

    }
}