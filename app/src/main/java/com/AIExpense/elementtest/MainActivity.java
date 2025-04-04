package com.AIExpense.elementtest;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.AIExpense.elementtest.Call.Realtime;
import com.AIExpense.elementtest.Database.DBHandler;
import com.AIExpense.elementtest.Database.DataHandler;
import com.AIExpense.elementtest.Record.PostCallHandler;
import com.AIExpense.elementtest.Record.Transcription;
import com.AIExpense.elementtest.Record.UserInfoHandler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button startButton, pauseButton, endButton, expenseButton;
    private TextView expenseText;
    //private Realtime realtime;
    private DataHandler dataHandler;
    private boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new UserInfoHandler(this.getApplicationContext()).checkFile();

        startButton = findViewById(R.id.StartButton);
        pauseButton = findViewById(R.id.PauseButton);
        endButton = findViewById(R.id.EndButton);

        expenseButton = findViewById(R.id.expenseButton);
        expenseText = findViewById(R.id.expenseText);

        //realtime = new Realtime(this.getApplicationContext());
        dataHandler = new DataHandler(this.getApplicationContext());

        //false by default
        active = true;

        // Adding OnClickListener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!active) {
                    active = true;

                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    //realtime.startStreaming();
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (active) {
                    //realtime.pauseStreaming();

                    active = false;
                }
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (active) {
                    //Transcription transcription = realtime.stopStreaming();
                    Transcription transcription = new Transcription();
                    new Thread(new PostCallHandler(transcription, getApplicationContext(), dataHandler)).start();

                    active = false;
                }
            }
        });

        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> dataList = dataHandler.getExpense("Food", "04");

                StringBuilder text = new StringBuilder();

                for (String s: dataList) {
                    text.append(s + " ");
                }

                expenseText.setText(text.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}