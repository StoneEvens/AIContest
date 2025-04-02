package com.AIExpense.elementtest.Record;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TestDownload {
    public void copyFileToDownloads(Context context) {
        try {
            // Step 1: Read the original file from internal storage
            File originalFile = new File(context.getFilesDir(), "UserInfo.txt");
            FileInputStream fis = new FileInputStream(originalFile);

            // Step 2: Create a new file in the Downloads folder
            ContentValues values = new ContentValues();
            values.put(MediaStore.Downloads.DISPLAY_NAME, "UserInfo.txt");
            values.put(MediaStore.Downloads.MIME_TYPE, "text/plain");
            values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
            }

            if (uri != null) {
                OutputStream os = context.getContentResolver().openOutputStream(uri);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                fis.close();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
