package com.example.pictureuploader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //change text view to Image View
    //public SearchView searchView;

     ImageView imageView;

    //int[] pictureList;


     //Search Button
    //Button SearchButton;
    // ArrayList<ImageView> arraylist = new arraylist<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Generate simple data
        //Put a picture as an array
//        pictureList = new int[] {
//                R.drawable.bicycle1,
//                R.drawable.car1,
//                R.drawable.camera1
//        };

        //mTextView = (TextView) findViewById (R.id.image);

        // searchView = (SearchView)  findViewById(R.id.searchView);                 //Declare Search View
         imageView = (ImageView) findViewById (R.id.ExImage);                      //Declare Image View

        //SearchButton = (Button) findViewById(R.id.SearchButton);


        //Need to work on Search Bar

    }

    //Make Image downloadable
    public void btnAction(View view) {

        imageDownloader myTask = new imageDownloader();

        Bitmap bitmapImage;

        bitmapImage = myTask.execute("")



    }

    public class imageDownloader extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {

            try{
                URL url = new URL(Strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                return bitmap;

            }catch(MalformedURLException e){

                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}


