package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;
// ...

public class ReplyFragment extends DialogFragment {
    public  static String  TAG = "ReplyFragment";
    public static final int MAXLINES = 140;
    TwitterClient client;


    private EditText mEditText;
    Context context;
    Button btnReply;
    ImageButton ibCancel;
    TextView tvMyName;
    TextView tvMyUserName;
    TextView tvReplyTo;
    TextInputLayout ltTextfield;
    ImageView ivProfil;



    public ReplyFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }


    public static ReplyFragment newInstance(String title) {
        ReplyFragment frag = new ReplyFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reply, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        client = TwitterApp.getRestClient(context);
        btnReply = view.findViewById(R.id.btnReply);
        mEditText = (EditText) view.findViewById(R.id.etReply);
        ibCancel = view.findViewById(R.id.ibCancel);
        tvMyName = view.findViewById(R.id.tvMyName);
        tvMyUserName = view.findViewById(R.id.tvMyUserName);
        tvReplyTo = view.findViewById(R.id.tvReplyTo);
        ltTextfield = view.findViewById(R.id.ltTextfield);
        ivProfil = view.findViewById(R.id.ivProfil);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().getWindow().setLayout(700,1350);

        Bundle bundle=getArguments();
        User user= Parcels.unwrap(bundle.getParcelable("userInfo"));
        Tweet tweet = Parcels.unwrap(bundle.getParcelable("tweet"));

        tvMyName.setText(user.getName());
        tvMyUserName.setText(user.getScreenName());
        tvReplyTo.setText("In reply to " + tweet.user.getName());
        ltTextfield.setHint(tweet.user.getScreenName());

        Glide.with(getContext())
                .load(user.getProfileImageUrl())
                .transform(new RoundedCorners(100))
                .into(ivProfil);



        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username = pref.getString("username", "");
        if (!username.isEmpty()){
            mEditText.setText(username);
        }

        //Click on cancel button
        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref =
                        PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("username", mEditText.getText().toString());
                edit.commit();
                dismiss();
            }
        });


        // Click on Tweet button
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = mEditText.getText().toString();
                if (tweetContent.isEmpty()){
                    Toast.makeText(context, "Sorry, your tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > MAXLINES) {
                    Toast.makeText(context, "Sorry, your tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }


                // Make an API call to Twitter to publish the tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.e(TAG, "onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            ReplyFragment.ReplyFragmentListener listener = ( ReplyFragment.ReplyFragmentListener) getTargetFragment();
                            listener.onFinishReply(tweet);
                            Log.i(TAG,"Published tweet says: " + tweet.body);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);
                    }
                });
                dismiss();

            }
        });

    }

    //    interface listener
    public interface ReplyFragmentListener{
        void onFinishReply(Tweet tweet);
    }

}