package com.AIExpense.elementtest;

import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Speaker {
    MediaPlayer mediaPlayer;
    Queue<byte[]> audioQueue;
    boolean available;

    public Speaker() {
        mediaPlayer = new MediaPlayer();
        audioQueue = new LinkedList<>();
        available = true;

        new Thread(new audioRunnable()).start();
    }

    public void addAudio(byte[] audio) {
        audioQueue.add(audio);
    }

    public void stop() {
        available = false;
    }

    private class audioRunnable implements Runnable {
        @Override
        public void run() {
            while (available) {
                if (!(audioQueue.isEmpty() || mediaPlayer.isPlaying())) {
                    byte[] data = audioQueue.poll();

                    try {
                        mediaPlayer = new MediaPlayer();

                        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
                        mediaPlayer.setDataSource(new ByteArrayMediaDataSource(data));

                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
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
        public long getSize() throws IOException {
            return data.length;
        }

        @Override
        public void close() throws IOException {
            // No resources to close for ByteArray
        }
    }
}
