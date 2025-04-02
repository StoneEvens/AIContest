package com.AIExpense.elementtest.RealtimeSession;

import android.Manifest;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;

import com.AIExpense.elementtest.Transcription.Transcription;
import com.AIExpense.elementtest.Transcription.UserInfoHandler;

import java.util.Queue;

public class Realtime {
    private final AudioStreamer audioStreamer;
    private final WebSocketHandler webSocketHandler;
    private final AudioPlayer audioPlayer;
    private final Context context;
    private final Transcription transcription;

    public Realtime(Context context) {
        transcription = new Transcription();
        audioPlayer = new AudioPlayer();
        webSocketHandler = new WebSocketHandler(audioPlayer, transcription);
        audioStreamer = new AudioStreamer(webSocketHandler, audioPlayer);
        this.context = context;
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    public void startStreaming() {
        String s = new UserInfoHandler(context).readFromFile();
        Log.e("Debug",s);

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

    public void stopStreaming() {
        // Stop recording and close the WebSocket connection
        audioStreamer.stopRecording();
        audioPlayer.close();

        Queue<String> dataQueue = transcription.getTranscriptions();
        StringBuilder transcription = new StringBuilder();

        while (!dataQueue.isEmpty()) {
            transcription.append(dataQueue.poll()).append("\n");
        }

        UserInfoHandler userInfoHandler = new UserInfoHandler(context);
        userInfoHandler.writeToFile(transcription.toString());

        Toast.makeText(context, "Call Stopped", Toast.LENGTH_SHORT).show();
    }
}