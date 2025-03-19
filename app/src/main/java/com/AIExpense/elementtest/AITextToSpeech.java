package com.AIExpense.elementtest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AITextToSpeech extends AsyncTask<String, Void, byte[]> {
    private static final String apiKey = "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";
    private static final String apiUrl = "https://api.openai.com/v1/audio/speech";
    private static final String modelName = "tts-1";
    private static final String voice = "coral";
    private Speaker speaker;

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    @Override
    protected byte[] doInBackground(String... strings) {
        String text = strings[0];
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        String jsonPayload = String.format("{\"model\": \"%s\", \"input\": \"%s\", \"voice\": \"%s\"}", modelName, text, voice);
        RequestBody body = RequestBody.create(jsonPayload, mediaType);

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Log.e("Debug", "API Error: " + response.code());
                return null;
            }

            return response.body().bytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(byte[] audioData) {
        if (audioData != null) {
            Log.e("Debug", "Audio Generated Finished");
            speaker.addAudio(audioData);
        } else {
            Log.e("Debug", "Audio data is null");
        }
    }
}
