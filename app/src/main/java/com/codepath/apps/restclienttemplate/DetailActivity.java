package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import okhttp3.Headers;

public class DetailActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvHandle;
    TextView tvBody;
    TextView tvSince;
    TextView tvName;
    ImageView ivImage;
    Tweet tweet;
    ImageButton btnReply;
//    ImageButton btnLike;
    TwitterClient client;

    public static final int REQUEST_CODE = 40;
    public static final String TAG = "Detail View";

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (ParseException e) {
            Log.i("Tweets Adapter", "getRelativeTimeAgo failed");
            e.printStackTrace();
        }
        return "";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        client = TwitterApp.getRestClient(this);

        ivProfileImage = findViewById(R.id.ivProfileImageDetail);
        tvHandle = findViewById(R.id.tvHandleDetail);
        tvBody = findViewById(R.id.tvBodyDetail);
        tvSince = findViewById(R.id.tvSinceDetail);
        tvName = findViewById(R.id.tvNameDetail);
        ivImage = findViewById(R.id.ivImageDetail);
        btnReply = findViewById(R.id.btnReplyDetail);
//        btnLike = findViewById(R.id.btnLikeDetail);

        Intent intent = getIntent();
        tweet = Parcels.unwrap(intent.getParcelableExtra("tweet"));
        bind(tweet);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.composeactivity, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.twitter_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        return true;
    }

    public void bind(final Tweet tweet) {
        tvSince.setText(String.format(" · %s", getRelativeTimeAgo(tweet.createdAt)));
        tvHandle.setText(String.format("@%s", tweet.user.screenName));
        if (tweet.imageUrl != null) {
            Glide.with(this)
                    .load(tweet.imageUrl)
                    .into(ivImage);
            ivImage.setVisibility(View.VISIBLE);
        } else {
            ivImage.setVisibility(View.GONE);
        }
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, ComposeActivity.class);
                intent.putExtra("tweet", Parcels.wrap(tweet));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
//        if (tweet.liked == Boolean.TRUE) {
//            btnLike.setImageResource(R.drawable.ic_vector_heart);
//
//        } else {
//            btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
//        }

//        btnLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("det", String.valueOf(tweet.liked));
//                if (tweet.liked == Boolean.TRUE) {
//                    client.unlikeTweet(tweet.id, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Headers headers, JSON json) {
//                            tweet.liked = Boolean.FALSE;
//                            Log.i(TAG, "Unliked");
//                            btnLike.setImageResource(R.drawable.ic_vector_heart);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
//                            Log.e(TAG, "Error Unliking", throwable);
//                        }
//                    });
//                } else {
//                    client.likeTweet(tweet.id, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Headers headers, JSON json) {
//                            tweet.liked = Boolean.TRUE;
//                            Log.i(TAG, "Liked");
//                            btnLike.setImageResource(R.drawable.ic_vector_heart);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
//                            Log.e(TAG, "Error Liking", throwable);
//                        }
//                    });
//                }
//            }
//        });
        tvBody.setText(tweet.body);
        tvName.setText(tweet.user.name);
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .circleCrop()
                .into(ivProfileImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Intent intent = new Intent(DetailActivity.this, TimelineActivity.class);
            startActivity(intent);
        }
    }
}