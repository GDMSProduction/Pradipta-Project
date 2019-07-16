package com.example.pictureuploader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;

import javax.security.auth.login.LoginException;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    //Search Button
    Button searchbtn;

    // ArrayList<ImageView> arraylist = new arraylist<ImageView>();

    static final String API_KEY = "563492ad6f91700001000001a31e67b4f9464550ad8c0f89490b8e20";
    static  String API_URL = "";
    static String searchTopic = "";

//    private boolean networkOk;
    Menu menu;
    //Button searchbtn = (Button) findViewById(R.id.search_btn);
    //SearchView responseView;
    //SearchView searchView;

    EditText searchView;
   private TextView textView;

    private Context mContext;

    //getter
    public ImageView getImageView() {
        return imageView;
    }

    //setter
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    private boolean clickable = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.responseView);

        final Button searchbtn = (Button) findViewById(R.id.search_btn);



        //boolean Download = new RetrieveFeedTask().isDownload;
       searchbtn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               searchTopic = textView.getText().toString();
               API_URL = "https://api.pexels.com/v1/search?query=" + searchTopic + "query&per_page=15&page=1";

               searchbtn.setEnabled(false);

               //To get Image from the pexels web
               ImageView imageView = findViewById(R.id.ExImage);               //Initialize imageView
               Picasso.get()
                       .load("http://i.imgur.com/DvpvklR.png")
                       .placeholder(R.mipmap.ic_launcher)
                       .resize(500,500)
                       .rotate(0)
                       .into(imageView, new Callback() {
                           @Override
                           public void onSuccess() {
                               Toast.makeText(getApplicationContext(), "Fetched image from internet", Toast.LENGTH_SHORT).show();
                           }

                           @Override
                           public void onError(Exception e) {
                               Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                           }
                       });


               new RetrieveFeedTask().execute();
           }
       });
    }




    class RetrieveFeedTask extends AsyncTask<String, String, String>{
        private Exception exception;
        public static final String TAG = "string";

        TextView textView = (TextView) findViewById(R.id.responseView);
        public  boolean isDownload = false;


        protected void onPreExecute(){
            //Log.d (TAG, "execute - UI thread");
            // searchView.getActionView();
                //searchView.getText("");
                textView.setText("");
        }

            //App Connect to API
        protected String doInBackground(String...urls){
//            SearchView searchView = findViewById(R.id.searchView);

                // HTTP URL connection reference.
                URL url;
                HttpURLConnection connection = null;
                InputStream is = null;
                String results = "";
                try {
                    // Create new URL
                    url = new URL(API_URL);
                    // Open connection
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Authorization", API_KEY);
                    // Perform connection operation
                    connection.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Input stream reference
                try {
                    if (connection != null) {
                        Log.i("TAG", "dataUrl: not null");
                        // Get the stream
                        is = connection.getInputStream();
                        Log.i("TAG", "dataUrl: IS "+ is);
                        // Convert the stream to a string (think about out utils lib)
                        if (is != null) {
                            results =  IOUtils.toString(is,"UTF-8");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Error check connection
                    if (connection != null) {
                        if (is != null) {
                            try {
                                // If we have a stream, try to close it.
                                is.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // If we have a connection disconnect it
                        connection.disconnect();
                    }

//                    try {
//                        Thread.sleep(1000);
//                    }catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
                }
                return results;
        }



        protected void onPostExecute(String response) {

            if(response == null) {
                response = "THERE WAS AN ERROR";
            }

            //if search is empty will show an error

            if(response.equals("{\"total_results\":0,\"page\":1,\"per_page\":15,\"photos\":[]}"))
            {
                textView.setText("No Images found");
            }
            else
            {
                textView.setText(response);
            }
            //progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);


            // TODO: check this.exception
            // TODO: do something with the feed

//            try {
//                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
//                String requestID = object.getString("requestId");
//                int likelihood = object.getInt("likelihood");
//                JSONArray photos = object.getJSONArray("photos");
////                .
////                .
////                .
////                .
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
}

//    void parseJSONDataBasicNum(String _jsonDataString){
//        final String TAG = "";
//
//        TextView textView = (TextView) findViewById(R.id.responseView);
//        try {
//            JSONObject outerObject = new JSONObject(_jsonDataString);
//            int value = outerObject.getInt("Key");
//            Log.i(TAG, "value: " + value);
//            textView.setText("value: " +  value);
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//
//    }



}



