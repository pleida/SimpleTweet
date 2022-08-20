package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends  RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweets;

    // pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;

    }

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        TweetsAdapter.ViewHolder viewHolder = new TweetsAdapter.ViewHolder(view,listener);
        return viewHolder;
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with view holder
        holder.bind(tweet);
    }


    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    // Define a viewholder
    public  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvName;
        TextView tvUserName;
        TextView tvDate;
        TextView tvRetweet;
        TextView tvRetweet_green;
        TextView tvHeart;
        TextView tvHeart_red;
        ImageView ivUrl;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvName = itemView.findViewById(R.id.tvName);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);
            tvRetweet_green = itemView.findViewById(R.id.tvRetweet_green);
            tvHeart = itemView.findViewById(R.id.tvHeart);
            tvHeart_red = itemView.findViewById(R.id.tvHeart_red);
            ivUrl = itemView.findViewById(R.id.ivUrl);
          itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  clickListener.onItemClick(itemView, getAdapterPosition());
              }
          });
        }


        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.getName());
            tvUserName.setText(tweet.user.getScreenName());
            tvDate.setText(Tweet.getFormattedTime(tweet.createAt));

            tvRetweet.setText(tweet.getRetweetCount());

            if(!tweet.retweeted){
                tvRetweet.setVisibility(View.VISIBLE);
                tvRetweet_green.setVisibility(View.INVISIBLE);
            }else{
                tvRetweet.setVisibility(View.INVISIBLE);
                tvRetweet_green.setVisibility(View.VISIBLE);
            }



            tvHeart.setText(tweet.getFavoriteCount());

            if(!tweet.favorited){
                tvHeart.setVisibility(View.VISIBLE);
                tvHeart_red.setVisibility(View.INVISIBLE);
            }else{
                tvHeart.setVisibility(View.INVISIBLE);
                tvHeart_red.setVisibility(View.VISIBLE);
            }

            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .transform(new RoundedCorners(50))
                    .into(ivProfileImage);

            if(!tweet.Entity.mediaUrls.isEmpty()){
                ivUrl.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(tweet.Entity.mediaUrls)
                        .transform(new RoundedCorners(50))
                        .into(ivUrl);
            }


            // Add click on Retweet icon
            tvRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweet.retweetCount++;
                    tvRetweet.setVisibility(View.INVISIBLE);
                    tvRetweet_green.setVisibility(View.VISIBLE);
                    tvRetweet_green.setText(tweet.getRetweetCount());
                    tweet.retweeted = true;

                }
            });
            tvRetweet_green.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    tweet.retweetCount--;
                    tvRetweet.setVisibility(View.VISIBLE);
                    tvRetweet_green.setVisibility(View.INVISIBLE);
                    tvRetweet.setText(tweet.getRetweetCount());
                    tweet.retweeted = false;

                }
            });


            // Add click on Heart icon
            tvHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    tweet.favoriteCount++;
                    tvHeart.setVisibility(View.INVISIBLE);
                    tvHeart_red.setVisibility(View.VISIBLE);
                    tvHeart_red.setText(tweet.getFavoriteCount());
                    tweet.favorited = true;
                }
            });
            tvHeart_red.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweet.favoriteCount--;
                    tvHeart.setVisibility(View.VISIBLE);
                    tvHeart_red.setVisibility(View.INVISIBLE);
                    tvHeart.setText(tweet.getFavoriteCount());
                    tweet.favorited = false;

                }
            });


        }
    }

}
