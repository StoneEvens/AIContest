package com.AIExpense.elementtest.Transcription;

import java.util.LinkedList;
import java.util.Queue;

public class Transcription {
    private final Queue<String> transcriptionQueue;

    public Transcription() {
        transcriptionQueue = new LinkedList<>();
    }

    public void addTranscription(String transcription) {
        transcriptionQueue.add(transcription);
    }

    public void clearTranscriptions() {
        transcriptionQueue.clear();
    }

    public Queue<String> getTranscriptions() {
        return transcriptionQueue;
    }
}
