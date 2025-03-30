package com.AIExpense.elementtest.RealtimeSession;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.provider.MediaStore;

import java.util.LinkedList;
import java.util.Queue;

public class AudioPlayer {
    private Queue<byte[]> audioQueue;
    private boolean end;

    public AudioPlayer() {
        audioQueue = new LinkedList<>();
        end = false;

        new AudioThread().start();
    }

    private class AudioThread extends Thread {
        @Override
        public void run() {
            while (!end) {
                if (!audioQueue.isEmpty()) {
                    byte[] audioData = audioQueue.poll();
                    playAudio(audioData);
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void addAudioData(byte[] audioData) {
        audioQueue.add(audioData);
    }

    private void playAudio(byte[] audioData) {
        AudioTrack audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                16000, // Sample rate
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                audioData.length,
                AudioTrack.MODE_STATIC
        );

        audioTrack.write(audioData, 0, audioData.length);
        audioTrack.play();
    }

    public void close() {
        end = true;
    }
}
