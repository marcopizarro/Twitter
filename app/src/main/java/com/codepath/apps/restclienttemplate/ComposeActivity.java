package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_LEN = 140;

    Button btnTweet;
    EditText etCompose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        btnTweet = findViewById(R.id.btnTweet);
        etCompose = findViewById(R.id.etCompose);
        Log.i("compose", "created");

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("compose", "clicked");

                Toast.makeText(ComposeActivity.this, "here", Toast.LENGTH_SHORT);
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tweetContent.length() > MAX_LEN) {
                    Toast.makeText(ComposeActivity.this, "Tweet is over 140 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i("compose", tweetContent);
            }
        });
    }
}