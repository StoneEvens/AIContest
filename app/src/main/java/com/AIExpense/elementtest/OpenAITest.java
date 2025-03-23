package com.AIExpense.elementtest;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
import com.openai.models.ChatModel;
import com.openai.models.beta.assistants.Assistant;
import com.openai.models.beta.assistants.AssistantCreateParams;
import com.openai.models.beta.assistants.AssistantDeleteParams;
import com.openai.models.beta.threads.Thread;
import com.openai.models.beta.threads.ThreadCreateParams;
import com.openai.models.beta.threads.messages.MessageCreateParams;
import com.openai.models.beta.threads.messages.MessageListPageAsync;
import com.openai.models.beta.threads.messages.MessageListParams;
import com.openai.models.beta.threads.runs.Run;
import com.openai.models.beta.threads.runs.RunCreateParams;
import com.openai.models.beta.threads.runs.RunRetrieveParams;
import com.openai.models.beta.threads.runs.RunStatus;

import java.util.concurrent.CompletableFuture;

public class OpenAITest {
    private final OpenAIClientAsync client;
    private CompletableFuture<Assistant> assistantFuture;
    CompletableFuture<Thread> threadIdFuture;
    CompletableFuture<Run> runFuture;
    public OpenAITest() {
        client = OpenAIOkHttpClientAsync.builder()
                .fromEnv()
                .apiKey("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")
                .build();

        assistantFuture = client.beta()
                .assistants()
                .create(AssistantCreateParams.builder()
                        .name("AI Recorder")
                        .instructions("You will act as a friend of the user and kindly listen and respond to the user when they are sharing what they buy")
                        .model(ChatModel.GPT_4O_MINI)
                        .build());

        threadIdFuture = client.beta()
                .threads()
                .create(ThreadCreateParams.builder().build());
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public void sendMessage(String input) {
        String curThreadID = threadIdFuture.join().id();
        String curAssistantID = assistantFuture.join().id();

        client.beta()
                .threads()
                .messages()
                .create(MessageCreateParams.builder()
                        .threadId(curThreadID)
                        .role(MessageCreateParams.Role.USER)
                        .content(input)
                        .build())
                .join();

        runFuture = client.beta()
                .threads()
                .runs()
                .create(RunCreateParams.builder()
                        .threadId(curThreadID)
                        .assistantId(curAssistantID)
                        .instructions("Please call the user Steven.")
                        .build());

        CompletableFuture<Run> polledRunFuture = runFuture.thenComposeAsync(run -> pollRun(client, run));

        polledRunFuture
                .thenComposeAsync(run -> {
                    if (!run.status().equals(RunStatus.COMPLETED)) {
                        return CompletableFuture.completedFuture(null);
                    }

                    return listThreadMessages(client, run.threadId());
                })
                .join();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public void deleteAssistant() {
        CompletableFuture<Run> polledRunFuture = runFuture.thenComposeAsync(run -> pollRun(client, run));

        polledRunFuture
                .thenComposeAsync(run -> {
                    if (!run.status().equals(RunStatus.COMPLETED)) {
                        return CompletableFuture.completedFuture(null);
                    }

                    return listThreadMessages(client, run.threadId())
                            .thenComposeAsync(unused -> client.beta()
                                    .assistants()
                                    .delete(AssistantDeleteParams.builder()
                                            .assistantId(assistantFuture.join().id())
                                            .build()))
                            .thenAccept(assistantDeleted ->
                                    Log.e("Debug", "Assistant deleted:" + assistantDeleted.deleted()));
                })
                .join();
    }

    private static CompletableFuture<Run> pollRun(OpenAIClientAsync client, Run run) {
        if (!run.status().equals(RunStatus.QUEUED) && !run.status().equals(RunStatus.IN_PROGRESS)) {
            Log.e("Debug","Run Completed with status" + run.status().toString());
            return CompletableFuture.completedFuture(run);
        }

        Log.e("Debug", "Polling Run");
        try {
            java.lang.Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return client.beta()
                .threads()
                .runs()
                .retrieve(RunRetrieveParams.builder()
                        .threadId(run.threadId())
                        .runId(run.id())
                        .build()
                )
                .thenComposeAsync(task -> pollRun(client, task));
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private static CompletableFuture<Void> listThreadMessages(OpenAIClientAsync client, String threadId) {
        CompletableFuture<MessageListPageAsync> page = client.beta()
                .threads()
                .messages()
                .list(MessageListParams.builder()
                        .threadId(threadId)
                        .order(MessageListParams.Order.ASC)
                        .build()
                );

        return page.thenAcceptAsync(pg -> pg.autoPager()
                .forEach(message -> {
                    Log.e("Debug", message.role().toString().toUpperCase());
                    message.content().stream()
                            .flatMap(messageContent -> messageContent.text().stream())
                            .forEach(text -> Log.e("Debug", text.text().value()));
                return true;
                }, page.defaultExecutor()
                )
        );
    }
}
