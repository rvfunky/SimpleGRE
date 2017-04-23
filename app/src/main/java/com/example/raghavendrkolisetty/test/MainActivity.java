package com.example.raghavendrkolisetty.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private static final int MY_INTENT_CLICK=302;
    Button btn;
    TextToSpeech textToSpeech;
    HashMap hashmap = new HashMap<String,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        btn = (Button)findViewById(R.id.button);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                System.out.println(path);
                File file = new File(path,"test.txt");
                System.out.println(file);
                //Read text from file
                StringBuilder text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }

                //System.out.println("this is text"+text);

                //String toSpeak = "hello raghu. Welcome to the world of android. How are you doing today?";
                //textToSpeech.speak(text.toString(), TextToSpeech.QUEUE_FLUSH, null);

                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"),MY_INTENT_CLICK);

            }
        });
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == MY_INTENT_CLICK)
            {
                if (null == data) return;

                String selectedImagePath;
                Uri selectedImageUri = data.getData();

                //MEDIA GALLERY
                selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
                Log.i("Image File Path", ""+selectedImagePath);
                //txta.setText("File Path : \n"+selectedImagePath);
                Toast toast = Toast.makeText(getApplicationContext(),selectedImagePath,Toast.LENGTH_LONG);

                toast.show();
                File file = new File(selectedImagePath);
                StringBuilder text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }

                //System.out.println("this is text"+text);

                String toSpeak = "hello raghu. Welcome to the world of android. How are you doing today?";
                textToSpeech.speak(text.toString(), TextToSpeech.QUEUE_FLUSH, null);

            }
        }
    }
}



