package com.AIExpense.elementtest.Record;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class UserInfoHandler {
    private final String fileName = "UserInfo.txt";
    private final Context context;

    public UserInfoHandler(Context context) {
        this.context = context;
    }

    public void writeToFile(String data) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            osw.write(data);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFromFile() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            int c;
            while ((c = fis.read()) != -1) {
                stringBuilder.append((char) c);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public void checkFile() {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            fis.close();
        } catch (IOException e) {
            writeToFile("");
        }
    }
}
