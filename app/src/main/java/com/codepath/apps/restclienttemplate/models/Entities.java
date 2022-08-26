package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity
public class Entities {

    @ColumnInfo
    @PrimaryKey
    public Long id_enty;

    @ColumnInfo
    public String mediaUrls;

    public Entities(){ }

    public static Entities fromJson(JSONObject jsonObject) throws JSONException {
        Entities Entity = new Entities();

        if (jsonObject.has("media")) {
            JSONArray jsonArray = jsonObject.getJSONArray("media");
            Entity.mediaUrls = jsonArray.getJSONObject(0).getString("media_url_https");
            Entity.id_enty = jsonArray.getJSONObject(0).getLong("id");

        }else{
            Entity.mediaUrls = "";
        }

        return Entity;
    }

    public static List<Entities> fromJsonTweetArray(List<Tweet> tweetsFromNetwork) {
        List<Entities> entities = new ArrayList<>();
        for (int i = 0; i < tweetsFromNetwork.size(); i++ ) {
            entities.add(tweetsFromNetwork.get(i).Entity);
        }
        return entities;
    }

}
