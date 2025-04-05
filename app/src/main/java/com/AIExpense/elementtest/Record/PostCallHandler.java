package com.AIExpense.elementtest.Record;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.AIExpense.elementtest.Analyze.ExpenseAnalyzer;
import com.AIExpense.elementtest.Analyze.HabitAnalyzer;
import com.AIExpense.elementtest.Database.DataHandler;

public class PostCallHandler implements Runnable {
    private final Transcription transcription;
    private final Context context;
    private final DataHandler dataHandler;

    public PostCallHandler(Transcription transcription, Context context, DataHandler dataHandler) {
        this.transcription = transcription;
        this.context = context;
        this.dataHandler = dataHandler;
    }

    @Override
    public void run() {
        try {
            Looper.prepare();
            ExpenseAnalyzer expenseAnalyzer = new ExpenseAnalyzer(transcription.getTranscriptions(), context, dataHandler);
            HabitAnalyzer habitAnalyzer = new HabitAnalyzer(transcription.getTranscriptions(), context, dataHandler);

            //ExpenseAnalyzer expenseAnalyzer = new ExpenseAnalyzer(transcription.getTestTranscription(), context, dataHandler);
            //HabitAnalyzer habitAnalyzer = new HabitAnalyzer(transcription.getTestTranscription(), context, dataHandler);

            expenseAnalyzer.start();
            habitAnalyzer.start();

            while (expenseAnalyzer.isOperating() || habitAnalyzer.isOperating()) {
                try {
                    Log.e("Debug", "Operating...");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    return;
                }
            }

            if (expenseAnalyzer.isDone() && habitAnalyzer.isDone()) {
                Toast.makeText(context, "Record Saved", Toast.LENGTH_SHORT).show();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    new TestDownload().copyFileToDownloads(context);
                }
            } else {
                Toast.makeText(context, "Error Saving Record", Toast.LENGTH_SHORT).show();
            }

            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
