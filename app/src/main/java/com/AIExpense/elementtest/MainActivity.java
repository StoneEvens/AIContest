package com.AIExpense.elementtest;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText Text;
    Button btnText;
    MediaPlayer mediaPlayer;
    Speaker speaker;
    GPTConnector GPTConnector;

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
        mediaPlayer = new MediaPlayer();
        speaker = new Speaker();

        GPTConnector = new GPTConnector();

        // Adding OnClickListener
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = Text.getText().toString();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!input.equals("end")) {
                                    String response = GPTConnector.sendMessage(input);
                                    Log.e("Debug", response);

                                    String[] sentences = response.split("。");

                                    for (String sentence: sentences) {
                                        AITextToSpeech textToSpeech = new AITextToSpeech();
                                        textToSpeech.setSpeaker(speaker);
                                        textToSpeech.execute(sentence);
                                    }
                                } else {
                                    GPTConnector.deleteAssistant();
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }).start();
                }
            }
        });
    }
}