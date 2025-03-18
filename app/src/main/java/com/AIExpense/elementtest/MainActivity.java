package com.AIExpense.elementtest;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.protobuf.ByteString;

public class MainActivity extends AppCompatActivity {

    EditText Text;
    Button btnText;
    //TextToSpeech textToSpeech;

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
                //textToSpeech.speak(Text.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                try {
                    InputStream credientialsStream = getResources().openRawResource(R.raw.Credentials);
                    GoogleCredentials credential = GoogleCredentials.fromStream(credientialsStream);
                    TextToSpeechSettings settings = TextToSpeechSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(credential)).build();

                    try (TextToSpeechClient ttsClient = TextToSpeechClient.create(settings)) {
                        SynthesisInput input = SynthesisInput.newBuilder().setText("Hello, world!").build();

                        VoiceSelectionParams voice = VoiceSelectionParams.newBuilder().setLanguageCode("cmn-CN").setName("cmn-CN-Wavenet-A").build();

                        AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

                        SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);

                        ByteString audioContents = response.getAudioContent();

                        playAudio(audioContents.toByteArray());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void playAudio(byte[] audioData) {
        int sampleRate = 16000; // Use 16 kHz for LINEAR16
        AudioTrack audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                audioData.length,
                AudioTrack.MODE_STATIC
        );
        audioTrack.write(audioData, 0, audioData.length);
        audioTrack.play();
    }
}