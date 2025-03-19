package com.AIExpense.elementtest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AIResponse extends AsyncTask<String, Void, String> {
    private static final String apiKey = "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";
    private static final String apiUrl = "https://api.openai.com/v1/chat/completions";
    private static final String LLMName = "gpt-3.5-turbo";

    private static String output = "";

    @Override
    protected String doInBackground(String... strings) {
        String GPTResponse = new String();

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");


            String userInput = strings[0];
            String jsonPayload = String.format("{\"model\": \"%s\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}", LLMName, userInput);
            RequestBody body = RequestBody.create(jsonPayload, mediaType);

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));

            if (!response.isSuccessful()) {
                Log.e("Debug", "API Error: " + response.code());
            }

            StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                int firstChar = stringBuilder.indexOf("content") + 11;
                int lastChar = stringBuilder.indexOf("\"", firstChar);
                GPTResponse = stringBuilder.substring(firstChar, lastChar);

            Log.e("Debug", GPTResponse);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return GPTResponse;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
