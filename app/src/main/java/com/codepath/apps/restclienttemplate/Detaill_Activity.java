package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class Detaill_Activity extends AppCompatActivity {

   Context context;
    public static final int Max_Lines = 140;
    TwitterClient client;


    Button btnTweet;
    ImageView imageView;
    TextView name;
    TextView userName;
    TextView description;
    TextView tvTime;
    TextView tvFavorites;
    TextView tvRetweets;
    TextView dtRetweet;
    TextView dtRetweet_green;
    TextView dtHeart;
    TextView dtHeart_red;
    TextView dtReply;
    TextView dtShare;
    ImageView dtUrl;
    EditText etReply;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int arrow = R.id.homeAsUp;
        Intent intent = new Intent(Detaill_Activity.this,TimelineActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(intent, 0);
        return true;
    }

    private void showEditDialog(Parcelable tweet) {
        FragmentManager fm = getSupportFragmentManager();
        ReplyFragment replyFragment = ReplyFragment.newInstance("Some Title");
        Bundle bundle=new Bundle();
        bundle.putParcelable("userInfo", Parcels.wrap(TimelineActivity.user));
        bundle.putParcelable("tweet", tweet);
        replyFragment.setArguments(bundle);
        replyFragment.show(fm, "fragment_reply");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaill);

        Toolbar toolbar = findViewById(R.id.wToolBar);
        setSupportActionBar(toolbar);

        // Display icon in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setLogo(R.drawable.ic_twitter2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("  Tweet");


        name = findViewById(R.id.name);
        userName = findViewById(R.id.userName);
        description = findViewById(R.id.description);
        imageView = findViewById(R.id.imageView);
        tvTime = findViewById(R.id.tvTime);
        tvFavorites = findViewById(R.id.tvFavorites);
        tvRetweets = findViewById(R.id.tvRetweets);
        dtRetweet = findViewById(R.id.dtRetweet);
        dtRetweet_green = findViewById(R.id.dtRetweet_green);
        dtHeart = findViewById(R.id.dtHeart);
        dtHeart_red = findViewById(R.id.dtHeart_red);
        dtUrl = findViewById(R.id.dtUrl);
        dtReply = findViewById(R.id.dtReply);
        dtShare = findViewById(R.id.dtShare);
        etReply = findViewById(R.id.etReply);
        btnTweet = findViewById(R.id.btnTweet);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweets"));
        client =TwitterApp.getRestClient(context);
        description.setText(tweet.getBody());
        name.setText(tweet.getUser().getName());
        userName.setText(tweet.getUser().getScreenName());
        tvTime.setText(Tweet.getTimeStamp(tweet.createAt));
        tvFavorites.setText(tweet.getFavoriteCount() + " Favorites");
        tvRetweets.setText(tweet.getRetweetCount() + " Retweets");
        etReply.setHint("Reply to " + tweet.getUser().getName());

        // Click on Reply icon
        dtReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(Parcels.wrap(tweet));
            }

        });

        // Click on Share icon
        dtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, tweet.getUrl());
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });

        // click on Tweet button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etReply.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(Detaill_Activity.this, "Sorry your tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > Max_Lines) {
                    Toast.makeText(Detaill_Activity.this, "Sorry your tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i("tweet", tweet.body);



                            etReply.setHint("Reply");
                            etReply.setText("");
                            Toast.makeText(Detaill_Activity.this, "Tweeted", Toast.LENGTH_LONG).show();




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                    }
                });

            }
        });

        dtRetweet.setText(tweet.getRetweetCount());

        if(!tweet.retweeted){
            dtRetweet.setVisibility(View.VISIBLE);
            dtRetweet_green.setVisibility(View.INVISIBLE);
        }else{
            dtRetweet.setVisibility(View.INVISIBLE);
            dtRetweet_green.setVisibility(View.VISIBLE);
        }

        // Condition for heart icon
        dtHeart.setText(tweet.getFavoriteCount());

        if(!tweet.favorited){
            dtHeart.setVisibility(View.VISIBLE);
            dtHeart_red.setVisibility(View.INVISIBLE);
        }else{
            dtHeart.setVisibility(View.INVISIBLE);
            dtHeart_red.setVisibility(View.VISIBLE);
        }

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .transform(new RoundedCorners(100))
                .into(imageView);

        if(!tweet.Entity.mediaUrls.isEmpty()) {
            dtUrl.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(tweet.Entity.mediaUrls)
                    .transform(new RoundedCorners(50))
                    .into(dtUrl);
        }

        // Add click on Retweet icon
        dtRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweet.retweetCount++;
                dtRetweet.setVisibility(View.INVISIBLE);
                dtRetweet_green.setVisibility(View.VISIBLE);
                dtRetweet_green.setText(tweet.getRetweetCount());
                tweet.retweeted = true;

            }
        });
        dtRetweet_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tweet.retweetCount--;
                dtRetweet.setVisibility(View.VISIBLE);
                dtRetweet_green.setVisibility(View.INVISIBLE);
                dtRetweet.setText(tweet.getRetweetCount());
                tweet.retweeted = false;

            }
        });

        // Add click on Heart icon
        dtHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweet.favoriteCount++;
                dtHeart.setVisibility(View.INVISIBLE);
                dtHeart_red.setVisibility(View.VISIBLE);
                dtHeart_red.setText(tweet.getFavoriteCount());
                tweet.favorited = true;
            }
        });
        dtHeart_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweet.favoriteCount--;
                dtHeart.setVisibility(View.VISIBLE);
                dtHeart_red.setVisibility(View.INVISIBLE);
                dtHeart.setText(tweet.getFavoriteCount());
                tweet.favorited = false;

            }
        });
    }
}