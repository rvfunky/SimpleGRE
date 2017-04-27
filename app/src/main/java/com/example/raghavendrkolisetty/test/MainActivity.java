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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private static final int MY_INTENT_CLICK=302;
    Button btn,btnStartReading;
    String delimiter;
    TextToSpeech textToSpeech;
    HashMap hashmap = new HashMap<String,String>();
    File file;
    //RadioGroup radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);



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
        btnStartReading = (Button) findViewById(R.id.btnStartReading);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeech.setSpeechRate((float)0.8);
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
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
                }*/

                //System.out.println("this is text"+text);

                //String toSpeak = "hello raghu. Welcome to the world of android. How are you doing today?";
                //textToSpeech.speak(text.toString(), TextToSpeech.QUEUE_FLUSH, null);

                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"),MY_INTENT_CLICK);

            }
        });

        btnStartReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        //text.append(line);
                        //text.append('\n');
                        String[] strings = line.split(delimiter);
                        StringBuilder builder = new StringBuilder();
                        int len = strings.length;
                        for(int i=1;i<len;i++){
                            builder.append(strings[i]);
                        }
                        hashmap.put(strings[0].trim(),builder.toString().trim());

                    }
                    br.close();
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }

                //System.out.println("this is text"+text);
                Iterator iterator = hashmap.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry<String,String> pair = (Map.Entry)iterator.next();
                    textToSpeech.speak(pair.getKey().toString().trim(), TextToSpeech.QUEUE_ADD, null);
                    textToSpeech.speak(pair.getValue(), TextToSpeech.QUEUE_ADD, null);
                    textToSpeech.playSilence(750, TextToSpeech.QUEUE_ADD, null);
                }
            }
        });

        /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println("half successss"+checkedId);
            }
        });*/
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        //System.out.println("print success");
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.comma:
                if (checked)
                    // Pirates are the best
                    delimiter=",";
                    break;
            case R.id.colon:
                if (checked)
                    // Ninjas rule
                    delimiter=":";
                    break;
            case R.id.equalTo:
                if (checked)
                    delimiter = "=";
                    break;
            case R.id.space:
                if (checked)
                    delimiter = "\\s+";
        }
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
                String nameOfFile;
                String[] parts = selectedImagePath.split("/");
                int len2 = parts.length;
                Toast t = Toast.makeText(getApplicationContext(),parts[len2-1],Toast.LENGTH_LONG);
                System.out.println(parts[len2-1]);
                Log.i("Image File Path", ""+selectedImagePath);
                //txta.setText("File Path : \n"+selectedImagePath);
                Toast toast = Toast.makeText(getApplicationContext(),selectedImagePath,Toast.LENGTH_LONG);

                toast.show();
                file = new File(selectedImagePath);

                //String toSpeak = "hello raghu. Welcome to the world of android. How are you doing today?";


            }
        }
    }


}



