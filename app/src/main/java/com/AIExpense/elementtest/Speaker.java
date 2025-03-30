package com.AIExpense.elementtest;

import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Speaker {
    volatile static MediaPlayer mediaPlayer;
    //volatile static Queue<byte[]> audioQueue;
    volatile static byte[][] audioBuffer;
    boolean available, playing;

    public Speaker() {
        mediaPlayer = new MediaPlayer();
        //audioQueue = new LinkedList<>();
        available = true;
        playing = false;

        new Thread(new audioRunnable()).start();
    }

    public void setAudioBufferSize(int size) {
        audioBuffer = new byte[size][];
    }

    public void addAudio(byte[] audio, int index) {
        //audioQueue.add(audio);
        audioBuffer[index] = audio;
    }

    public void stop() {
        available = false;
    }

    private class audioRunnable implements Runnable {
        int index = 0;

        @Override
        public void run() {
            while (available) {
//                if (!audioQueue.isEmpty() && !playing) {
//                    byte[] data = audioQueue.poll();
//
//                    if (data != null) {
//
//                    }
//                }

                if (!playing && audioBuffer != null && audioBuffer[index] != null) {

                    try {
                        playing = true;
                        mediaPlayer.setDataSource(new ByteArrayMediaDataSource(audioBuffer[index]));

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

                    if (index < audioBuffer.length - 1) {
                        index++;
                    } else {
                        audioBuffer = null;
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
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
}
