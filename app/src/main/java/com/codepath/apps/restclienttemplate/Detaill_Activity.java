package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class Detaill_Activity extends AppCompatActivity {
    ImageView imageView;
    TextView name;
    TextView userName;
    TextView description;
    TextView tvTime;
    TextView tvFavorites;
    TextView tvRetweets;
    TextView dtRetweet;
    TextView dtHeart;
    ImageView dtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaill);


        name = findViewById(R.id.name);
        userName = findViewById(R.id.userName);
        description = findViewById(R.id.description);
        imageView = findViewById(R.id.imageView);
        tvTime = findViewById(R.id.tvTime);
        tvFavorites = findViewById(R.id.tvFavorites);
        tvRetweets = findViewById(R.id.tvRetweets);
        dtRetweet = findViewById(R.id.dtRetweet);
        dtHeart = findViewById(R.id.dtHeart);
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

        Glide.with(this).load(tweet.user.profileImageUrl).transform(new RoundedCorners(100)).into(imageView);
        Glide.with(this).load((tweet.Entity.mediaUrls)).transform(new RoundedCorners(50)).into(dtUrl);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.arrow, menu);
        return super.onCreateOptionsMenu(menu);
    }
}