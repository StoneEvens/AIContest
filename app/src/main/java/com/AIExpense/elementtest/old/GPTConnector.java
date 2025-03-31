package com.AIExpense.elementtest.old;

import android.util.Log;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.beta.assistants.Assistant;
import com.openai.models.beta.assistants.AssistantDeleteParams;
import com.openai.models.beta.assistants.AssistantDeleted;
import com.openai.models.beta.assistants.AssistantRetrieveParams;
import com.openai.models.beta.threads.messages.MessageCreateParams;
import com.openai.models.beta.threads.messages.MessageListPage;
import com.openai.models.beta.threads.messages.MessageListParams;
import com.openai.models.beta.threads.runs.Run;
import com.openai.models.beta.threads.runs.RunCreateParams;
import com.openai.models.beta.threads.runs.RunRetrieveParams;
import com.openai.models.beta.threads.runs.RunStatus;
import com.openai.models.beta.threads.Thread;

import java.util.concurrent.atomic.AtomicBoolean;

public class GPTConnector {
    private OpenAIClient client;
    private Assistant assistant;
    private Thread thread;
    private String response = "";

    public GPTConnector() {
        new java.lang.Thread(new Runnable() {
            @Override
            public void run() {
                client = OpenAIOkHttpClient.builder()
                        .fromEnv()
                        .apiKey("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")
                        .build();

                //assistant = client.beta()
                        //.assistants()
                        //.create(AssistantCreateParams.builder()
                                //.name("Expense Helper")
                                //.instructions("You are a friend of the user who wants to record their daily expenses. Please talk to him and help him remember what he bought and record it. You should only provide response with words, that means you need to turn all the numbers and into words and remove any unnecessary markings, as that is easier for the user to understand. Please don't use the English quotation mark, as they can mess up the handling system. You also don't need to list out the calculation process, when answering, simply repeat the important information and provide the calculated solution. But the user doesn't speak English, please speak to them in Chinese Mandarin")
                                //.model(ChatModel.GPT_3_5_TURBO)
                                //.build());
                Log.e("Debug", "Debug");

                assistant = client.beta()
                        .assistants()
                        .retrieve(AssistantRetrieveParams.builder()
                                .assistantId("asst_jo0JgHb2WdzbZg20paW2cVpu")
                                .build()
                        );

                thread = client.beta().threads().create();
            }
        }).start();
    }

    public String sendMessage(String input) throws InterruptedException {
        AtomicBoolean done = new AtomicBoolean(false);

        new java.lang.Thread(new Runnable() {

            @Override
            public void run() {
                String curThreadID = thread.id();
                String curAssistantID = assistant.id();

                client.beta()
                        .threads()
                        .messages()
                        .create(MessageCreateParams.builder()
                                .threadId(curThreadID)
                                .role(MessageCreateParams.Role.USER)
                                .content(input)
                                .build());

                Run run = client.beta()
                        .threads()
                        .runs()
                        .create(RunCreateParams.builder()
                                .threadId(curThreadID)
                                .assistantId(curAssistantID)
                                .instructions("Please address the user as Steven. And since the user doesn't speak English, please respond to the user in Chinese Mandarin using Traditional Chinese characters.")
                                .build());

                while (run.status().equals(RunStatus.QUEUED) || run.status().equals(RunStatus.IN_PROGRESS)) {
                    System.out.println("Polling run...");

                    try {
                        java.lang.Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    run = client.beta()
                            .threads()
                            .runs()
                            .retrieve(RunRetrieveParams.builder()
                                    .threadId(curThreadID)
                                    .runId(run.id())
                                    .build());
                }
                Log.e("Debug", "Run completed with status: " + run.status() + "\n");

                if (!run.status().equals(RunStatus.COMPLETED)) {
                    response = "Failed";
                }

                MessageListPage page = client.beta()
                        .threads()
                        .messages()
                        .list(MessageListParams.builder()
                                .threadId(curThreadID)
                                .order(MessageListParams.Order.ASC)
                                .build());
                page.autoPager().stream().forEach(currentMessage -> {
                    currentMessage.content().stream()
                            .flatMap(content -> content.text().stream())
                            .forEach(textBlock -> {
                                setresponse(textBlock.text().value());
                                done.set(true);
                            });
                });
            }
        }).start();

        while (!done.get()) {
            java.lang.Thread.sleep(100);
        }

        return response;
    }

    public void setresponse(String response) {
        this.response = response;
    }

    public void deleteAssistant() {
        AssistantDeleted assistantDeleted = client.beta()
                .assistants()
                .delete(AssistantDeleteParams.builder()
                        .assistantId(assistant.id())
                        .build());

        //thread = null;
        //assistant = null;
        //client = null;

        System.out.println("Assistant deleted: " + assistantDeleted.deleted());
    }

    public boolean checkClient() {
        return client != null;
    }
}
