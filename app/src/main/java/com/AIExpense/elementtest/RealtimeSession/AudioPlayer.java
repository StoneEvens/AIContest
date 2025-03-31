package com.AIExpense.elementtest.RealtimeSession;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class AudioPlayer {
    private volatile Queue<byte[]> audioQueue;
    //private MediaPlayer mediaPlayer;
    private boolean available, playing;

    AudioTrack audioTrack;

    public AudioPlayer() {
        audioQueue = new LinkedList<>();

        available = true;
        playing = false;

        new PlayerThread().start();
    }

    private class PlayerThread extends Thread {
        @Override
        public void run() {
            while (available) {
                if (!audioQueue.isEmpty() && !playing) {
                    try {
                        playing = true;

                        int sampleRate = 24000; // Replace with the sample rate of the audio
                        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
                        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

                        int bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);

                        audioTrack = new AudioTrack(
                                AudioManager.STREAM_MUSIC,
                                sampleRate,
                                channelConfig,
                                audioFormat,
                                bufferSize,
                                AudioTrack.MODE_STREAM
                        );

                        audioTrack.play();
                        writeAudioData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        audioTrack.stop();
                        audioTrack.release();
                        playing = false;
                        Log.e("Debug", "Playing Set to False");
                    }
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void writeAudioData() {
            if (!audioQueue.isEmpty()) {
                byte[] audioData = audioQueue.poll();
                audioTrack.write(audioData, 0, audioData.length);

                audioTrack.setNotificationMarkerPosition(audioData.length / 2);

                if (!audioQueue.isEmpty()) {
                    writeAudioData();
                }
            }
        }
    }

    public void addAudioData(byte[] audioData) {
        audioQueue.add(audioData);
    }

    public void clearAudioData() {
        audioQueue = new LinkedList<>();
    }

    public boolean getPlaying() {
        return playing;
    }

    public void close() {
        available = false;
    }
}
