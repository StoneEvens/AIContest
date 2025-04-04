package com.AIExpense.elementtest.Call;

import android.Manifest;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;

import com.AIExpense.elementtest.Record.PostCallHandler;
import com.AIExpense.elementtest.Record.Transcription;

public class Realtime {
    private final AudioStreamer audioStreamer;
    private final WebSocketHandler webSocketHandler;
    private final AudioPlayer audioPlayer;
    private final Context context;
    private final Transcription transcription;

    public Realtime(Context context) {
        this.context = context;
        transcription = new Transcription();
        audioPlayer = new AudioPlayer();
        webSocketHandler = new WebSocketHandler(audioPlayer, transcription, context);
        audioStreamer = new AudioStreamer(webSocketHandler, audioPlayer);
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    public void startStreaming() {
        // Connect to the WebSocket
        new Thread(new Runnable() {
            @RequiresPermission(Manifest.permission.RECORD_AUDIO)
            @Override
            public void run() {
                if (!webSocketHandler.isConnected()) {
                    webSocketHandler.connect();

                    while (!webSocketHandler.isConnected()) {
                        try {
                            Log.i("Debug","Waiting for connection...");
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Start recording and streaming audio
                audioStreamer.startRecording();
                audioPlayer.start();

                Looper.prepare();
                Toast.makeText(context, "Call Started", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    public void pauseStreaming() {
        // Pause recording and close the WebSocket connection
        audioStreamer.stopRecording();
        audioPlayer.close();

        Toast.makeText(context, "Call Paused", Toast.LENGTH_SHORT).show();
    }

    public Transcription stopStreaming() {
        // Stop recording and close the WebSocket connection
        audioStreamer.stopRecording();
        audioPlayer.close();

        Toast.makeText(context, "Call Stopped", Toast.LENGTH_SHORT).show();

        return transcription;
    }
}