package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class Detaill_Activity extends AppCompatActivity {

    Context context;


    ImageView imageView;
    TextView name;
    TextView userName;
    TextView description;
    TextView tvTime;
    TextView tvFavorites;
    TextView tvRetweets;
    TextView dtRetweet;
    TextView dtHeart;
    TextView dtHeart_red;
    ImageView dtUrl;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int arrow = R.id.homeAsUp;
        Intent intent = new Intent(Detaill_Activity.this,TimelineActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
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
        dtHeart = findViewById(R.id.dtHeart);
        dtHeart_red = findViewById(R.id.dtHeart_red);
        dtUrl = findViewById(R.id.dtUrl);
        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweets"));

        description.setText(tweet.getBody());
        name.setText(tweet.getUser().getName());
        userName.setText(tweet.getUser().getScreenName());
        tvTime.setText(Tweet.getTimeStamp(tweet.createAt));
        tvFavorites.setText(tweet.getFavoriteCount() + " Favorites");
        tvRetweets.setText(tweet.getRetweetCount() + " Retweets");
        dtRetweet.setText(tweet.getRetweetCount());
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