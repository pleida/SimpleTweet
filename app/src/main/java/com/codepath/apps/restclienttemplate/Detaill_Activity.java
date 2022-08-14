package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaill);

        name = findViewById(R.id.name);
        userName = findViewById(R.id.userName);
        description = findViewById(R.id.description);
        imageView = findViewById(R.id.imageView);
        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweets"));

        description.setText(tweet.getBody());
        name.setText(tweet.getUser().getName());
        userName.setText(tweet.getUser().getScreenName());

        Glide.with(this).load(tweet.user.profileImageUrl).transform(new RoundedCorners(100)).into(imageView);

    }
}