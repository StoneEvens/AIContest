package com.AIExpense.elementtest;

import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
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
    private static MediaPlayer mediaPlayer;


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
            playAudio(audioData);
        } else {
            Log.e("Debug", "Audio data is null");
        }
    }

    private void playAudio(byte[] audioData) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        try {
            mediaPlayer = new MediaPlayer();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(audioData);
            mediaPlayer.setDataSource(new ByteArrayMediaDataSource(audioData));

            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        } catch (IOException e) {
            Log.e("Debug", "Error playing audio", e);
        }
    }

    private static class ByteArrayMediaDataSource extends MediaDataSource {
        private final byte[] data;

        public ByteArrayMediaDataSource(byte[] data) {
            this.data = data;
        }

        @Override
        public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
            if (position >= data.length) {
                return -1; // End of stream
            }
            int bytesToRead = (int) Math.min(size, data.length - position);
            System.arraycopy(data, (int) position, buffer, offset, bytesToRead);
            return bytesToRead;
        }

        @Override
        public long getSize() throws IOException {
            return data.length;
        }

        @Override
        public void close() throws IOException {
            // No resources to close for ByteArray
        }
    }
}
