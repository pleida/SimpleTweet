package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {
    @Query("SELECT Tweet.body AS tweet_boby, Tweet.createAt AS tweet_createAt, Tweet.id AS tweet_id, Tweet.favoriteCount AS tweet_favoriteCount, Tweet.retweetCount AS tweet_retweetCount, Tweet.favorited AS tweet_favorited, Tweet.retweeted AS tweet_retweeted, User.* " +
            "FROM Tweet INNER JOIN User ON Tweet.userId = User.id ORDER BY Tweet.createAt DESC LIMIT 5")
    List<TweetWithUser> recentItems();

    //"SELECT Tweet.body AS tweet_boby, Tweet.createAt AS tweet_createAt, Tweet.id AS tweet_id, User.* "

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Tweet... tweets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User... users);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertModel(Entities...entities);

}
