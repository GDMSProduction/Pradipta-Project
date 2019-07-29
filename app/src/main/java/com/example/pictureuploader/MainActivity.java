package com.example.pictureuploader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.TestLooperManager;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

   public ImageView imageView;

    //Search Button
    public Button searchbtn;

    // ArrayList<ImageView> arraylist = new arraylist<ImageView>();

    static final String API_KEY = "563492ad6f91700001000001a31e67b4f9464550ad8c0f89490b8e20";
    static  String API_URL = "";
    static String searchTopic = "";



    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    public boolean ex;

    //Button searchbtn = (Button) findViewById(R.id.search_btn);
    //SearchView responseView;
    //SearchView searchView;

    private TextView textView;

    private String TAG = "DEBUGGING PROJECT -------------------------------------------------------------------";

    //private parseJSON selectedExamples;

    //private Context mContext;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.responseView);

//        final Button searchbtn = (Button) findViewById(R.id.search_btn);
            searchbtn =  (Button) findViewById(R.id.search_btn);

            //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        final String jsonText = textView.getText().toString();



        //boolean Download = new RetrieveFeedTask().isDownload;
        searchbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchTopic = textView.getText().toString();
                API_URL = "https://api.pexels.com/v1/search?query=" + searchTopic + "query&per_page=10&page=1";


//                //To get Image from the pexels web
                imageView = findViewById(R.id.ExImage);               //Initialize imageView

                ex = false;
                searchbtn.setEnabled(false);

                //progressBar.setVisibility(View.VISIBLE);
                if (!searchTopic.isEmpty()){
                    new RetrieveFeedTask().execute(API_URL);
                }else{
                    textView.setText("No Result");
                    searchbtn.setEnabled(true);
                }

            }
        });
        //Progress Bar
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (progressStatus < 50){
//                    progressStatus += 1;
//
//                    //Update the progress bar
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setProgress(progressStatus);
//                            textView.setText(progressStatus + "/" + progressBar.getMax());
//                        }
//                    });
//                    try{
//                        //sleep for 200 miliseconds
//                        Thread.sleep(200);
//                    }catch(InterruptedException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }

    class RetrieveFeedTask extends AsyncTask<String, String, String>{
        //private Exception exception;
        //public static final String TAG = "string";

      public  ArrayList<ImageObject> Arrayimages =  new ArrayList<>();

        TextView textView = (TextView) findViewById(R.id.responseView);
        public  boolean isDownload = false;
        String results = " ";
       // public String apiurlTry = "https://api.pexels.com/v1/search?query=pets+query&per_page=15&page=1";


        protected void onPreExecute(){
            //Log.d (TAG, "execute - UI thread");
            // searchView.getActionView();
            //searchView.getText("");
            textView.setText(" Loading....");
            Log.i(TAG, "onPreExecute: Start of Running...");
            super.onPreExecute();

        }


//App Connect to API
        protected String doInBackground(String... urls){
              //SearchView searchView = findViewById(R.id.searchView);
              //String results = " ";
          return InputDownload(urls[0]);
        }

        private String InputDownload(String i ){
            // HTTP URL connection reference.
            URL url;
            HttpURLConnection connection = null;
            InputStream is = null;


           // String results = "";
            if(!isDownload) {
//                Log.i(TAG, "InputDownload: is" + i);
                try {
                    // Create new URL
                    url = new URL(i);
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
                        Log.i("TAG", "dataUrl: IS " + is);
                        // Convert the stream to a string (think about out utils lib)
                        if (is != null) {
                            results = IOUtils.toString(is, "UTF-8");
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

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isDownload = false;
            }


            return results;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(String response) {


            if(response == null) {
                response = "THERE WAS AN ERROR";
            }


            //if search is empty will show an error
           // Log.i(TAG, "onPostExecute: " + API_URL);
                if(response.equals("{\"total_results\":0,\"page\":1,\"per_page\":10,\"photos\":[]}"))
                {

                    textView.setText("No Images found");
                }
                else
                {
                    textView.setText("");
                    parseJsonDataObjectMultiple(response);

                }

            //progressBar.setVisibility(View.GONE);
           // Log.i("INFO", response);


            searchbtn.setEnabled(true);



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
        void parseJsonDataObjectMultiple(String _jsonDataString){
            Log.i(TAG, "parseJsonDataObjectMultiple: it got here");
            try{
                JSONObject  outerObj = new JSONObject(_jsonDataString);

                JSONArray Arrayobj = outerObj.getJSONArray("photos");
                // Log.i(TAG, "parseJsonDataObjectMultiple: "+ Arrayobj);

                for (int i = 0; i < Arrayobj.length(); i++){
                    JSONObject object = Arrayobj.getJSONObject(i);
//                    Log.i(TAG, "parseJsonDataObjectMultiple: "+ object);
                    String urlJson = object.getString("url");
                    String photographerJson = object.getString("photographer");
                    String idJson = object.getString("id");
//
//                    Log.i(TAG, "parseJsonDataObjectMultiple: "+ urlJson+ photographerJson + idJson);
//                    Log.i(TAG, "parseJsonDataObjectMultiple: " + photographerJson);
//                    Log.i(TAG, "parseJsonDataObjectMultiple: " +idJson);

                    JSONObject innerobj = object.getJSONObject("src");

                    String srcJson = innerobj.getString("original");
                    //Log.i(TAG, "parseJsonDataObjectMultiple: " + srcJson);

                    ImageObject images = new ImageObject(idJson, urlJson,srcJson,photographerJson);

                    Arrayimages.add(images);



                }


//
//                String ValueA = innerObj.getString("KeyA");
//                String ValueB = innerObj.getString("KeyB");
//                String ValueC = innerObj.getString("KeyC");

//                Log.i(TAG, "Value A: " + ValueA);
//                Log.i(TAG, "Value B: " + ValueB);
//                Log.i(TAG, "Value C: " + ValueC);

                // textView.setText("ValueA: " + ValueA + "\n ValueB:" + ValueB + "\nValueC" + ValueC);

            } catch(Exception e){
                Log.i(TAG, "parseJsonDataObjectMultiple: ERROR");
                e.printStackTrace();
            }

            String Show = "";
            for (int i = 0; i < Arrayimages.size() ; i++) {
                textView.append(Arrayimages.get(i).toString());

                if (imageView != null) {
                    Picasso.get().load(Arrayimages.get(i).imageurl).into(imageView);
                }


            }

        }



    }




}



