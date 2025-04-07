package com.AIExpense.elementtest.Analyze;

import android.content.Context;
import android.util.Log;

import com.AIExpense.elementtest.Database.DataHandler;
import com.AIExpense.elementtest.Record.UserInfoHandler;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import java.util.Date;

public class APIAnalyzer {
    private static final String apiKey = "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";
    private final Context context;
    private final DataHandler dataHandler;
    private boolean operating = true;
    private boolean done = false;

    public APIAnalyzer(Context context, DataHandler dataHandler) {
        this.context = context;
        this.dataHandler = dataHandler;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isOperating() {
        return operating;
    }

    class Task implements Runnable {
        private final String userInput;
        private final String operation;

        public Task(String command, String operation) {
            userInput = command;
            this.operation = operation;
        }

        @Override
        public void run() {
            StringBuilder GPTResponse = new StringBuilder();

            try {
                OpenAIClient client = OpenAIOkHttpClient.builder()
                        .fromEnv()
                        .apiKey(apiKey)
                        .build();

                ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4O)
                        .maxCompletionTokens(2048)
                        .addDeveloperMessage(String.format("Today's dat is: %s", new Date()))
                        .addUserMessage(userInput)
                        .build();

                client.chat().completions().create(createParams).choices().stream()
                        .flatMap(choice -> choice.message().content().stream())
                        .forEach(GPTResponse::append);

                Log.e("Debug", GPTResponse.toString());

                switch (operation) {
                    case "Expense":
                        dataHandler.saveExpenses(GPTResponse.toString());
                        break;
                    case "Habit":
                        new UserInfoHandler(context).writeToFile(GPTResponse.toString());
                        break;
                    default:
                        return;
                }

                done = true;
                operating = false;
            } catch (Exception e) {
                Log.e("Debug", e.getMessage());
                operating = false;
            }
        }
    }
}
