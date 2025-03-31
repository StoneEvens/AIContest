package com.AIExpense.elementtest.RealtimeSession;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketHandler {

    private final String url = "wss://api.openai.com/v1/realtime?model=gpt-4o-realtime-preview";
    private final String apiKey = "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";
    private final String prompt = "You are a helpful assistant!";
    private final String voice = "ash";
    private WebSocket webSocket;
    private String sessionID;
    private String encodedAudio;
    private AudioPlayer audioPlayer;
    private boolean connected;

    public WebSocketHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        connected = false;
    }

    public void connect() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("OpenAI-Beta", "realtime=v1")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                Log.i("Debug","Connected to OpenAI API");
                connected = true;
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.i("Debug","Received message: " + text);
                filterMessage(text);
                // Process and play the received audio
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // Handle binary messages (e.g., audio stream from server)
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                Log.i("Debug","Closing connection: " + code + reason);
                webSocket.close(1000, null);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                Log.i("Debug","Error: " + t.getMessage());
                connected = true;
            }
        });
    }

    public void sendAudioData(byte[] audioData) {
        if (webSocket != null && sessionID != null) {
            // Encode the audio data in Base64
            String base64Audio = Base64.encodeToString(audioData, Base64.NO_WRAP);

            // Create a JSON payload to send the encoded audio
            String jsonPayload = String.format("{\"event_id\": \"%s\",\"type\": \"input_audio_buffer.append\", \"audio\": \"%s\"}", sessionID, base64Audio);

            // Send the payload
            webSocket.send(jsonPayload);
        } else {
            Log.e("Debug","WebSocket is not connected.");
        }
    }

    public void sessionUpdate() {
        if (webSocket != null && sessionID != null) {
            String jsonPayload = String.format("{"
                    + "\"event_id\": \"%s\","
                    + "\"type\": \"session.update\","
                    + "\"session\": {"
                    +       "\"instructions\": \"%s\","
                    +       "\"voice\": \"%s\","
                    +       "\"turn_detection\": {\n" +
                                    "\"type\": \"server_vad\",\n" +
                                    "\"threshold\": 0.5,\n" +
                                    "\"prefix_padding_ms\": 300,\n" +
                                    "\"silence_duration_ms\": 500,\n" +
                                    "\"create_response\": true\n" +
                            "},"
                    + "}"
                    ,sessionID, prompt, voice);
        }
    }

    private void filterMessage(String text) {
        int startIndex = 9;
        int endIndex = text.indexOf(",", startIndex) - 1;
        String type = text.substring(startIndex, endIndex);

        switch (type) {
            case "session.created":
                setSessionID(text);
                sessionUpdate();
                break;
            case "response.audio.delta":
                encodedAudio = getAudioData(text);
                audioPlayer.addAudioData(Base64.decode(encodedAudio, Base64.DEFAULT));
                encodedAudio = "";
                break;
            case "input_audio_buffer.speech_started":
                audioPlayer.clearAudioData();
                break;
            default:
                break;
        }
    }

    private String getAudioData(String text) {
        int startIndex = text.lastIndexOf("delta") + 8;
        return text.substring(startIndex);
    }

    private void setSessionID(String text) {
        int startIndex = text.indexOf("event_id") + 11;
        int endIndex = text.indexOf(",", startIndex) - 1;
        sessionID = text.substring(startIndex, endIndex);
        Log.e("Debug", sessionID);
    }

    public boolean isConnected() {
        return connected;
    }
}
