package com.AIExpense.elementtest.Analyze;

import android.content.Context;
import android.util.Log;

import com.AIExpense.elementtest.Record.UserInfoHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Analyzer {
    private static final String LLMName = "gpt-4o";
    private static final String apiUrl = "https://api.openai.com/v1/chat/completions";
    private static final String apiKey = "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";
    private boolean operating = true;
    private boolean done = false;
    private Context context;

    public Analyzer(Context context) {
        this.context = context;
    }

    public boolean isOperating() {
        return operating;
    }

    public boolean isDone() {
        return done;
    }

    class Task implements Runnable {
        private final String userInput;
        private final boolean writeFile;

        public Task(String command, boolean writeFile) {
            userInput = command;
            this.writeFile = writeFile;
        }

        @Override
        public void run() {
            String GPTResponse = "";

            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");

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
                    operating = false;
                    return;
                }

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                int firstChar = stringBuilder.lastIndexOf("content") + 11;
                int lastChar = stringBuilder.indexOf("\"", firstChar);
                GPTResponse = stringBuilder.substring(firstChar, lastChar);

                Log.e("Debug", GPTResponse);

                if (writeFile) {
                    new UserInfoHandler(context).writeToFile(GPTResponse);
                }

                done = true;
                operating = false;
            } catch (IOException e) {
                operating = false;
            }
        }
    }
}
