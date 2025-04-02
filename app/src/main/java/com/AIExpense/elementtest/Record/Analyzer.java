package com.AIExpense.elementtest.Record;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Analyzer {
    private static final String LLMName = "gpt-4o";
    private static final String apiUrl = "https://api.openai.com/v1/chat/completions";
    private static final String apiKey = "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";
    private static final String expensePrompt = "Here is a transcript of a conversation about the expenses of the user. Please extract all the expense record for the user. All prices are in NTD, otherwise the currencies will be explicitly mentioned. The format should be [Category][Item]: [Price]. For example: [Food][Apple]: [50]. Categories include [Food], [Clothing], [Housing], [Commuting], [Education], [Leisure], [Others]. Simply start writing the summary directly, if no data provided, return [Nothing].";
    private static final String habitPrompt = "Here is a transcript of a conversation about the expenses of the user. Here is also a record of the habits of the user prior to the conversation. Please extract and refine the habits of the user using the two information, however, if there is no previous records, you may simply summarize the transcript. The summary should be brief and short, just enough to remind the model of the user's habit. If no data is provided, simply return [Nothing].";
    private final String transcription;
    private static boolean operating = true;
    private static boolean done = false;
    private static Context context;

    public Analyzer(String transcription, Context context) {
        this.transcription = transcription;
        this.context = context;
    }

    public void analyzeExpense() {
        new Thread(new Task(String.format("Prompt: %s; Transcript: %s", expensePrompt, transcription), true)).start();
    }

    public void analyzeHabit() {
        new Thread(new Task(String.format("Prompt: %s; Transcript: %s; Previous Habit Record: %s", habitPrompt, transcription, new UserInfoHandler(context).readFromFile()), false)).start();
    }

    public static boolean isOperating() {
        return operating;
    }

    public static boolean isDone() {
        return done;
    }

    private static class Task implements Runnable {
        private static String userInput;
        private static boolean writeFile = false;

        public Task(String command, boolean writeFile) {
            userInput = command;
            Task.writeFile = writeFile;
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
