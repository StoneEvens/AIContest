package com.AIExpense.elementtest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText Text;
    TextView RecognizeText;
    Button btnText, recognizeButton;
    MediaPlayer mediaPlayer;
    Speaker speaker;
    GPTConnector GPTConnector;
    //SpeechToText stt;
    //SpeechRecognizer speechRecognizer;

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

        RecognizeText = findViewById(R.id.recognizedText);
        recognizeButton = findViewById(R.id.speechBtn);

        mediaPlayer = new MediaPlayer();
        speaker = new Speaker();

        GPTConnector = new GPTConnector();
        //stt = new SpeechToText(getApplicationContext(), recognizeButton);

        // Initialize SpeechRecognizer
//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//        speechRecognizer.setRecognitionListener(new RecognitionListener() {
//            @Override
//            public void onReadyForSpeech(Bundle params) {
//                Toast.makeText(MainActivity.this, "Listening...", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onBeginningOfSpeech() { }
//
//            @Override
//            public void onRmsChanged(float rmsdB) { }
//
//            @Override
//            public void onBufferReceived(byte[] buffer) { }
//
//            @Override
//            public void onEndOfSpeech() { }
//
//            @Override
//            public void onError(int error) {
//                Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResults(Bundle results) {
//                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                if (matches != null) {
//                    RecognizeText.setText(matches.get(0));
//                }
//            }
//
//            @Override
//            public void onPartialResults(Bundle partialResults) { }
//
//            @Override
//            public void onEvent(int eventType, Bundle params) { }
//        });
//
//        startListening();

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
                                        //AITextToSpeech textToSpeech = new AITextToSpeech();
                                        //textToSpeech.setSpeaker(speaker);
                                        //textToSpeech.execute(sentence);
                                        GoogleTTS googleTTS = new GoogleTTS();
                                        googleTTS.setSpeaker(speaker);
                                        googleTTS.speak(sentence);
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

//        recognizeButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Log.e("Debug", "Attempting STT");
//                //stt.start();
//            }
//        });
    }

//    private void startListening() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
//        speechRecognizer.startListening(intent);
//    }

    @Override
    protected void onDestroy() {
        GPTConnector.deleteAssistant();
        super.onDestroy();
    }
}