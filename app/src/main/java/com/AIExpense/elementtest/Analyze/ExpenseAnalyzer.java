package com.AIExpense.elementtest.Analyze;

import android.content.Context;

import com.AIExpense.elementtest.Database.DataHandler;

public class ExpenseAnalyzer extends APIAnalyzer{
    private static final String prompt = "Here is a transcript of a conversation about the expenses of the user. Please extract all the expense record for the user. All prices are in NTD, otherwise the currencies will be explicitly mentioned. The format should be Date:Category:Item:Price. For example: [2025/03/25]:[Food]:[Apple]:[50]. Categories include [Food], [Clothing], [Housing], [Commuting], [Education], [Leisure], [Others]. Simply start writing the summary directly, if no data provided, return [Nothing].";
    private final String transcription;
    public ExpenseAnalyzer(String transcription, Context context, DataHandler dataHandler) {
        super(context, dataHandler);
        this.transcription = transcription;
    }

    public void start() {
        new Thread(new Task(String.format("Prompt: %s; Transcript: %s", prompt, transcription), "Expense")).start();
    }
}
