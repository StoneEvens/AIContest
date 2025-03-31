package com.AIExpense.elementtest.RealtimeSession;

import android.Manifest;

import androidx.annotation.RequiresPermission;

public class Realtime {
    private AudioStreamer audioStreamer;
    private WebSocketHandler webSocketHandler;
    private AudioPlayer audioPlayer;

    public Realtime() {
        audioPlayer = new AudioPlayer();
        webSocketHandler = new WebSocketHandler(audioPlayer);
        audioStreamer = new AudioStreamer(webSocketHandler, audioPlayer);
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    public void startStreaming() {
        // Connect to the WebSocket
        webSocketHandler.connect();

        // Start recording and streaming audio
        audioStreamer.startRecording();
    }

    public void stopStreaming() {
        // Stop recording and close the WebSocket connection
        audioStreamer.stopRecording();
        webSocketHandler.sendAudioData(null);
    }
}