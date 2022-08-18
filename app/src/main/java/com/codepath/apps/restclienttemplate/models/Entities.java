package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Entities {


    public String mediaUrls;

    public Entities(){ }

    public static Entities fromJson(JSONObject jsonObject) throws JSONException {
        Entities Entity = new Entities();

        if (jsonObject.has("media")) {
            JSONArray jsonArray = jsonObject.getJSONArray("media");
            Entity.mediaUrls = jsonArray.getJSONObject(0).getString("media_url_https");
        }else{
            Entity.mediaUrls = "";
        }

        return Entity;
    }
}
