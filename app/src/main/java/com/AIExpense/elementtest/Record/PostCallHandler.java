package com.AIExpense.elementtest.Record;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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
            Analyzer expenseAnalyzer = new Analyzer(transcription.getTranscriptions(), context);
            Analyzer habitAnalyzer = new Analyzer(transcription.getTranscriptions(), context);

            expenseAnalyzer.analyzeExpense();
            habitAnalyzer.analyzeHabit();

            while (expenseAnalyzer.isOperating() || habitAnalyzer.isOperating()) {
                try {
                    Log.e("Debug", "Operating...");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!expenseAnalyzer.isDone() || !habitAnalyzer.isDone()) {
                Toast.makeText(context, "Record Saved", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(context, "Error Saving Record", Toast.LENGTH_SHORT).show();

            Looper.loop();

            new TestDownload().copyFileToDownloads(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
