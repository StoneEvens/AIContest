package com.AIExpense.elementtest.Record;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TestDownload {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void copyFileToDownloads(Context context) {
        // Locate the original file from your app's internal storage.
        File originalFile = new File(context.getFilesDir(), "UserInfo.txt");
        if (!originalFile.exists()) {
            Log.e("FileCopy", "Original file does not exist");
            return;
        }

        try (FileInputStream fis = new FileInputStream(originalFile)) {
            // Prepare the metadata for the file to be inserted in the Downloads folder.
            ContentValues values = new ContentValues();
            values.put(MediaStore.Downloads.DISPLAY_NAME, "UserInfo.txt");
            values.put(MediaStore.Downloads.MIME_TYPE, "text/plain");
            // Using the constant here ensures the file goes to the Downloads folder.
            values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            // Insert the new file into MediaStore.
            // This will target the primary external volume.
            Uri downloadsCollection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri fileUri = context.getContentResolver().insert(downloadsCollection, values);

            if (fileUri == null) {
                Log.e("FileCopy", "Failed to create file in Downloads folder");
                return;
            }

            // Open an output stream to the newly created file.
            OutputStream os = context.getContentResolver().openOutputStream(fileUri);

            if (os == null) {
                Log.e("FileCopy", "Output stream is null");
                return;
            }
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            Log.d("FileCopy", "File successfully copied to Downloads!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
