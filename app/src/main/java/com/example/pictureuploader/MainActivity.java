package com.example.pictureuploader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ImageView imageView;
    public ArrayList<Bitmap> ImagesList = new ArrayList<>();

    //Search Button
    public Button searchbtn;

    // ArrayList<ImageView> arraylist = new arraylist<ImageView>();

    static final String API_KEY = "563492ad6f91700001000001a31e67b4f9464550ad8c0f89490b8e20";
    static  String API_URL = "";
    static String searchTopic = "";

    public boolean ex;

    SearchView searchView;

    ListView imageListview;
    private TextView textView;

    private String TAG = "DEBUGGING PROJECT -------------------------------------------------------------------";

    //private parseJSON selectedExamples;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
        imageListview = findViewById(R.id.imagesList);
        textView = findViewById(R.id.responseView);
        searchView = findViewById(R.id.searchView);
        searchbtn =  (Button) findViewById(R.id.search_btn);
        final String jsonText = textView.getText().toString();


//       searchView.setOnKeyListener(new View.OnKeyListener() {
//           @Override
//           public boolean onKey(View v, int keyCode, KeyEvent event) {
//               if(event.getAction() == KeyEvent.ACTION_DOWN)
//               {
//                   switch(keyCode)
//                   {
//                       case KeyEvent.KEYCODE_SEARCH:
//                        // Apply action which you want on search key press on keypad
//
//                           return true;
//                       default:
//                           break;
//                   }
//               }
//               return false;
//           }
//       });



        searchbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchTopic = "";
                searchTopic = searchView.getQuery().toString();

                searchView.clearFocus();

                API_URL = "https://api.pexels.com/v1/search?query=" + searchTopic + "+query&per_page=15&page=1";

                //To get Image from the pexels web
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
    }
    private class DownloadImageTask extends AsyncTask<String, Bitmap, Bitmap> {
        ImageView bmImage ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public DownloadImageTask(ImageView bmImage) {

            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            return InputDownload(urls[0]);
        }

        private Bitmap InputDownload(String i ){
            String urldisplay = i;
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Bitmap... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Bitmap result) {

            ImagesList.add(result);

            imageView.setImageBitmap(result);

            imageListview.invalidateViews();
        }
    }

    class RetrieveFeedTask extends AsyncTask<String, String, String>{
        //private Exception exception;
        //public static final String TAG = "string";

        public  ArrayList<ImageObject> Arrayimages =  new ArrayList<>();

        TextView textView = (TextView) findViewById(R.id.responseView);
        public  boolean isDownload = false;
        String results = " ";

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

            ImageView bmImage = (ImageView) findViewById(R.id.ExImage);


            if(response == null) {
                response = "THERE WAS AN ERROR";
            }

            //if search is empty will show an error
            // Log.i(TAG, "onPostExecute: " + API_URL);
            if(response.equals("{\"total_results\":0,\"page\":1,\"per_page\":15,\"photos\":[]}"))
            {

                textView.setText("No Images found");
            }
            else
            {
                textView.setText("");
//                bmImage.setImageBitmap(bitmap);

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

            } catch(Exception e){
                Log.i(TAG, "parseJsonDataObjectMultiple: ERROR");
                e.printStackTrace();
            }

            String Show = "";

            ArrayList<String>imagesurl = new ArrayList<>();
            ArrayAdapter<Bitmap> itemsAdapter = new ArrayAdapter<Bitmap>(getApplicationContext(), android.R.layout.simple_gallery_item, ImagesList);
            for (int i = 0; i < Arrayimages.size() ; i++) {
                textView.append(Arrayimages.get(i).toString());
                imagesurl.add(Arrayimages.get(i).imageurl);


                if (imageView != null) {
                    //imageView.setVisibility(View.VISIBLE);
                  DownloadImageTask downloadimage = new DownloadImageTask((imageView));
                  downloadimage.execute(Arrayimages.get(i).imageurl);

                  //DownloadImageTask.execute(Arrayimages.get(i).imageurl);

//                    Picasso.get().load(Arrayimages.get(i).imageurl).into(imageView);
                }

            }
            ImageAdapter imgadapter = new ImageAdapter(getApplicationContext(), ImagesList);

            imageListview.setAdapter(imgadapter);
        }
    }


}

class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> images;

    public ImageAdapter(Context c, ArrayList<Bitmap> imagesbmp) {
        mContext = c;
        images = new ArrayList<>();
        images = imagesbmp;
    }

    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 15, 8, 15);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(images.get(position));
        return imageView;
    }

    // references to your images

}