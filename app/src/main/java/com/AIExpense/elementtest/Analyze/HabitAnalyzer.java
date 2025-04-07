package com.AIExpense.elementtest.Analyze;

import android.content.Context;

import com.AIExpense.elementtest.Database.DataHandler;
import com.AIExpense.elementtest.Record.UserInfoHandler;

public class HabitAnalyzer extends APIAnalyzer{
    private static final String prompt = "Here is a transcript of a conversation about the expenses of the user. Here is also a record of the habits of the user prior to the conversation. Please extract and refine the habits of the user using the two information, however, if there is no previous records, you may simply summarize the transcript. The summary should be brief and short, just enough to remind the model of the user's habit. If no data is provided, simply return [Nothing].";
    private final String transcription;
    private Context context;
    public HabitAnalyzer(String transcription, Context context, DataHandler dataHandler) {
        super(context, dataHandler);
        this.transcription = transcription;
        this.context = context;
    }

    public void start() {
        new Thread(new Task(String.format("Prompt: %s; Transcript: %s; Previous Habit Record: %s", prompt, transcription, new UserInfoHandler(context).readFromFile()), "Habit")).start();

    }
}
