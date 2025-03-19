package com.AIExpense.elementtest;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText Text;
    Button btnText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Text = findViewById(R.id.Text);
        btnText = findViewById(R.id.btnText);

        // create an object textToSpeech and adding features into it
        /*
        Set<String> strSet = new HashSet<>();
        strSet.add("male");
        //Voice voice = new Voice("en-us-x-sfg#male_2-local",new Locale("en","US"),400,200,true,strSet);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.TRADITIONAL_CHINESE);


                    Set<Voice> voices = textToSpeech.getVoices();
                    List<Voice> voiceList = new ArrayList<>(voices);
                    Voice selectedVoice = voiceList.get(400);

                    int count = 0;
                    for (Voice voice: voiceList) {
                        Log.e("Debug", String.format("#%d: %s", count, voice.getName()));
                        count++;
                    }

                    Voice selectedVoice = new Voice("cmn-tw-x-ctd-local", new Locale("cmn", "TW"), 400, 200, true, strSet);

                    textToSpeech.setVoice(selectedVoice);
                    textToSpeech.setPitch(1f);
                    textToSpeech.setSpeechRate(1.25f);
                }
            }
        });
        */

        // Adding OnClickListener
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = Text.getText().toString();
                Text.setText("");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String response = new AIResponse().execute(userInput).get();

                            new AITextToSpeech().execute(response);
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        });
    }
}