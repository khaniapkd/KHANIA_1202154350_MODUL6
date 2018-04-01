package com.khania.khania_1202154350_modul6;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;

    public TextView authorView;

    public ImageView starView;

    public TextView numStarsView;

    public TextView bodyView;



    public PostViewHolder(View itemView) {

        super(itemView);



        titleView = itemView.findViewById(R.id.post_title);

        authorView = itemView.findViewById(R.id.post_author);

        starView = itemView.findViewById(R.id.star);

        numStarsView = itemView.findViewById(R.id.post_num_stars);

        bodyView = itemView.findViewById(R.id.post_body);

    }



    public void bindToPost(PostModel post, View.OnClickListener starClickListener) {

        titleView.setText(post.title);

        authorView.setText(post.author);

        numStarsView.setText(String.valueOf(post.starCount));

        bodyView.setText(post.body);



        starView.setOnClickListener(starClickListener);

    }
}
