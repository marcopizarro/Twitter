package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_LEN = 140;
    public static final String TAG = "Compose";

    Button btnTweet;
    EditText etCompose;
    TwitterClient client;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.composeactivity, menu);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.twitter_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        btnTweet = findViewById(R.id.btnTweet);
        etCompose = findViewById(R.id.etCompose);

        client = TwitterApp.getRestClient(this);
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
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "Success");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Intent data = new Intent();
                            // Pass relevant data back as a result
                            data.putExtra("tweet", Parcels.wrap(tweet));
//                            data.putExtra("code", 200); // ints work too
                            // Activity finished ok, return the data
                            setResult(RESULT_OK, data); // set result code and bundle data for response
                            finish(); // closes the activity, pass data to parent
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "error tweeting", throwable);
                    }
                });
            }
        });
    }
}