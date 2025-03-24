package com.AIExpense.elementtest;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private static final String apiUrl = "https://api.openai.com/v1/assistants";
    private static final String LLMName = "gpt-4o";
    private static final String assistantID = "asst_Ope0QQ7Il5V7qnrkFYdF6JOR";
    private OkHttpClient client;
    private MediaType mediaType;
    private String sessionID;

    @Override
    protected String doInBackground(String... strings) {
        String GPTResponse = new String();
        client = new OkHttpClient();

        /*
        try {
            //OkHttpClient client = new OkHttpClient();
            mediaType = MediaType.parse("application/json");


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

            Log.e("Debug", response.body().string());

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
        */
        createSession();

        sendMessage(strings[0]);

        return GPTResponse;
    }

    public String createSession() {
        mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(new JSONObject().toString(), mediaType);

        String url = apiUrl + "/sessions";
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                Log.e("Debug", "Connection Failed");
            }

            String responseBody = response.body().string();

            Log.e("Debug", responseBody);

            sessionID = getSessionID(responseBody);
            return sessionID;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getSessionID(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            return jsonObject.getString("id");
        } catch (JSONException e) {
            Log.e("Debug", "Error parsing JSON", e);
            return null;
        }
    }

    public String sendMessage(String message) {
        String url = apiUrl + "/sessions/" + sessionID + "/messages";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("content", message);
        } catch (JSONException e) {
            Log.e("Debug", "Error creating JSON body: " + e.getMessage(), e);
        }

        RequestBody requestBody = RequestBody.create(jsonBody.toString(), mediaType);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                Log.e(TAG, "sendMessage: " + response.code());
                return null;
            }
            String responseBody = response.body().string();
            return parseResponse(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String parseResponse(String responseBody) throws IOException {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray choices = jsonResponse.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject firstChoice = choices.getJSONObject(0);
                return firstChoice.getString("content");
            } else {
                throw new IOException("No content found in response");
            }
        } catch (JSONException e) {
            throw new IOException("Failed to parse response: " + e.getMessage(), e);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
