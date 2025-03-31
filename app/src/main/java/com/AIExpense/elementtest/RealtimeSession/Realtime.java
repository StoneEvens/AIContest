package com.AIExpense.elementtest.RealtimeSession;

import android.Manifest;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;

public class Realtime {
    private AudioStreamer audioStreamer;
    private WebSocketHandler webSocketHandler;
    private AudioPlayer audioPlayer;
    private Context context;

    public Realtime(Context context) {
        audioPlayer = new AudioPlayer();
        webSocketHandler = new WebSocketHandler(audioPlayer);
        audioStreamer = new AudioStreamer(webSocketHandler, audioPlayer);
        this.context = context;
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

    public void stopStreaming() {
        // Stop recording and close the WebSocket connection
        audioStreamer.stopRecording();
        audioPlayer.close();

        Toast.makeText(context, "Call Stopped", Toast.LENGTH_SHORT).show();
    }
}