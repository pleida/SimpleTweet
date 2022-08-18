package com.codepath.apps.restclienttemplate.models;


import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
@Parcel
public class Tweet {
    public String body;
    public String createAt;
    public long id;
    public User user;
    public String favoriteCount;
    public String retweetCount;
    public Entities Entity;



    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createAt = jsonObject.getString("created_at");
        tweet.id = jsonObject.getLong("id");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.Entity = Entities.fromJson(jsonObject.getJSONObject("entities"));
        tweet.favoriteCount = jsonObject.getString("favorite_count");
        tweet.retweetCount = jsonObject.getString("retweet_count");
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
        return favoriteCount;
    }

    public String getRetweetCount() {
        return retweetCount;
    }
}
