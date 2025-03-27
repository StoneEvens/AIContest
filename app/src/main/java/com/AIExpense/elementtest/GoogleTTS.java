package com.AIExpense.elementtest;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GoogleTTS {
    private static final String TAG = "GoogleCloudTTS";
    private static final String TTS_API_URL = "https://texttospeech.googleapis.com/v1/text:synthesize";
    private static final String apiKey = "AIzaSyDosZtgE8SVLTfYJtM2p1pes5yByNSMvD0";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private Speaker speaker;

    public GoogleTTS() {

    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public void speak(String text) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                JSONObject jsonObject = new JSONObject();

                // Create input JSON
                JSONObject input = new JSONObject();
                input.put("text", text);

                // Create voice JSON
                JSONObject voice = new JSONObject();
                voice.put("languageCode", "cmn-TW");
                voice.put("ssmlGender", "MALE");

                // Create audio config JSON
                JSONObject audioConfig = new JSONObject();
                audioConfig.put("audioEncoding", "MP3");

                // Combine into request JSON
                jsonObject.put("input", input);
                jsonObject.put("voice", voice);
                jsonObject.put("audioConfig", audioConfig);

                // Build request
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Request request = new Request.Builder()
                        .url(TTS_API_URL + "?key=" + apiKey)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    JSONObject responseJson = new JSONObject(responseString);
                    String audioContent = responseJson.getString("audioContent");

                    // decode base64 audio
                    byte[] audioBytes = Base64.decode(audioContent, Base64.DEFAULT);

                    //playAudio(audioBytes);
                    speaker.addAudio(audioBytes);
                } else {
                    Log.e(TAG, "Request failed: " + response.body().string());
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }).start();
    }
}
