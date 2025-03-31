package com.AIExpense.elementtest.RealtimeSession;

import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.annotation.RequiresPermission;

public class AudioStreamer {
    private WebSocketHandler webSocketHandler;
    private static final int SAMPLE_RATE = 24000; // Adjust based on OpenAI's requirements
    private AudioRecord audioRecord;
    private boolean isStreaming = false;

    public AudioStreamer(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    public void startRecording() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        audioRecord.startRecording();
        isStreaming = true;

        new Thread(() -> {
            byte[] audioBuffer = new byte[bufferSize];
            while (isStreaming) {
                int bytesRead = audioRecord.read(audioBuffer, 0, audioBuffer.length);
                if (bytesRead > 0) {
                    // Pass the audioBuffer to the WebSocket client
                    sendAudioData(audioBuffer);
                }
            }
        }).start();
    }

    public void stopRecording() {
        isStreaming = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
        }
    }

    private void sendAudioData(byte[] audioData) {
        // This method will be implemented in the WebSocket section
        webSocketHandler.sendAudioData(audioData);
    }
}
