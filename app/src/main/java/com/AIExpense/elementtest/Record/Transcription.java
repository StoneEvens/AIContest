package com.AIExpense.elementtest.Record;

import java.util.LinkedList;
import java.util.Queue;

public class Transcription {
    private StringBuilder transcription;

    public Transcription() {
        transcription = new StringBuilder();
    }

    public void addTranscription(String transcription) {
        this.transcription.append(transcription);
    }

    public void clearTranscriptions() {
        transcription = new StringBuilder();
    }

    public String getTranscriptions() {
        return transcription.toString();
    }
}
