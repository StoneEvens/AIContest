package com.AIExpense.elementtest.RealtimeSession;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class AudioPlayer {
    private Queue<byte[]> audioQueue;
    private MediaPlayer mediaPlayer;
    private boolean available, playing;

    public AudioPlayer() {
        audioQueue = new LinkedList<>();
        mediaPlayer = new MediaPlayer();
        available = true;
        playing = false;

        new AudioThread().start();
    }

    private class AudioThread extends Thread {
        @Override
        public void run() {
            while (available) {
//                if (!audioQueue.isEmpty()) {
//                    byte[] audioData = audioQueue.poll();
//                    playAudio(audioData);
//                } else {
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }

                if (!audioQueue.isEmpty() && !playing) {
                    byte[] data = audioQueue.poll();

                    try {
                        playing = true;
                        mediaPlayer.setDataSource(new AudioPlayer.ByteArrayMediaDataSource(data));

                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        Log.e("Debug", "Finished");

                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mediaPlayer.release();
                                mediaPlayer = new MediaPlayer();

                                playing = false;
                            }
                        });
                    } catch (IOException e) {
                        Log.e("Debug", "Error playing audio", e);
                    }
                }
            }
        }
    }

    static class ByteArrayMediaDataSource extends MediaDataSource {
        private final byte[] data;

        public ByteArrayMediaDataSource(byte[] data) {
            this.data = data;
        }

        @Override
        public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
            if (position >= data.length) {
                return -1; // End of stream
            }
            int bytesToRead = (int) Math.min(size, data.length - position);
            System.arraycopy(data, (int) position, buffer, offset, bytesToRead);
            return bytesToRead;
        }

        @Override
        public long getSize() {
            return data.length;
        }

        @Override
        public void close() {
            // No resources to close for ByteArray
        }
    }

    public void addAudioData(byte[] audioData) {
        audioQueue.add(audioData);
    }

    public void clearAudioData() {
        audioQueue = new LinkedList<>();
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
        available = false;
    }
}
