package com.AIExpense.elementtest.Analyze;

import android.content.Context;

import com.AIExpense.elementtest.Record.UserInfoHandler;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class APIAnalyzer {
    private static final String apiKey = "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";
    private boolean operating = true;
    private boolean done = false;
    private Context context;

    public APIAnalyzer(Context context) {
        this.context = context;
    }

    public boolean getDone() {
        return done;
    }

    public boolean isOperating() {
        return operating;
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
            StringBuilder GPTResponse = new StringBuilder();

            try {
                OpenAIClient client = OpenAIOkHttpClient.builder()
                        .fromEnv()
                        .apiKey(apiKey)
                        .build();

                ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4O)
                        .maxCompletionTokens(2048)
                        //.addDeveloperMessage("Make sure you mention Stainless!")
                        .addUserMessage(userInput)
                        .build();

                client.chat().completions().create(createParams).choices().stream()
                        .flatMap(choice -> choice.message().content().stream())
                        .forEach(GPTResponse::append);

                int firstChar = GPTResponse.lastIndexOf("content") + 11;
                int lastChar = GPTResponse.indexOf("\"", firstChar);
                String information = GPTResponse.substring(firstChar, lastChar);

                if (writeFile) {
                    new UserInfoHandler(context).writeToFile(information);
                }

                done = true;
                operating = false;
            } catch (Exception e) {
                operating = false;
            }
        }
    }
}
