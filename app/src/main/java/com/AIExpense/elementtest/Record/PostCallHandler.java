package com.AIExpense.elementtest.Record;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.AIExpense.elementtest.Analyze.ExpenseAnalyzer;
import com.AIExpense.elementtest.Analyze.HabitAnalyzer;

public class PostCallHandler implements Runnable {
    private final Transcription transcription;
    private final Context context;

    public PostCallHandler(Transcription transcription, Context context) {
        this.transcription = transcription;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            Looper.prepare();
            ExpenseAnalyzer expenseAnalyzer = new ExpenseAnalyzer(transcription.getTranscriptions(), context);
            HabitAnalyzer habitAnalyzer = new HabitAnalyzer(transcription.getTranscriptions(), context);

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
