package com.codepath.apps.restclienttemplate.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
@Parcel
@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId"))
public class Tweet {
    @ColumnInfo
    @PrimaryKey
    public long id;

    @ColumnInfo
    public String body;
    @ColumnInfo
    public String createAt;
    @ColumnInfo
    public Long userId;
    @ColumnInfo
    public int favoriteCount;
    @ColumnInfo
    public int retweetCount;
    @ColumnInfo
    public Boolean favorited;
    @ColumnInfo
    public Boolean retweeted;

    @Ignore
    public User user;
    @Ignore
    public Entities Entity;



    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createAt = jsonObject.getString("created_at");
        tweet.id = jsonObject.getLong("id");
        User user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.user = user;
        tweet.Entity = Entities.fromJson(jsonObject.getJSONObject("entities"));
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.retweetCount = jsonObject.getInt("retweet_count");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");

        tweet.userId = user.id;
        return tweet;
    }
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i<jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public static String getTimeStamp(String createAt) {
        return TimeFormatter.getTimeStamp(createAt);
    }

    public String getBody() {
        return body;
    }

    public String getCreateAt() {
        return createAt;
    }

    public static String getFormattedTime(String createAt){
        return TimeFormatter.getTimeDifference(createAt);
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getFavoriteCount() {
        return  String.valueOf(favoriteCount);
    }

    public String getRetweetCount() {
        return String.valueOf(retweetCount);
    }

    public String getUrl() {
        return  "https://twitter.com" +user.screenName+ "/status/"+id;
    }
}
