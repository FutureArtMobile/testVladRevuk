package com.selecto.vladrevuk.test.app.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.selecto.vladrevuk.test.app.R;
import com.selecto.vladrevuk.test.app.ScrollingActivity;
import com.selecto.vladrevuk.test.app.Structures.ContactStructure;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    private ArrayList<ContactStructure> mDataset;
    private Context mContext;
    private RequestOptions options;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card_view;
        public CircleImageView image_user;
        public TextView text_first_user;
        public TextView name_user;
        public ViewHolder(View v) {
            super(v);
            card_view = v.findViewById(R.id.card_view);
            image_user = v.findViewById(R.id.image_user);
            text_first_user = v.findViewById(R.id.text_first_user);
            name_user = v.findViewById(R.id.name_user);
        }
    }

    public MainActivityAdapter(Context myContext, ArrayList<ContactStructure> myDataset) {
        mDataset = myDataset;
        mContext = myContext;

        options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false);
    }

    @Override
    public MainActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main_activity, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void setItemToPostion(ContactStructure watchListBean, int itemPosition) {
        this.mDataset.set(itemPosition, watchListBean);
        notifyItemChanged(itemPosition);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ScrollingActivity.class);
                intent.putExtra(ScrollingActivity.ID, mDataset.get(position).get_id());
                mContext.startActivity(intent);
            }
        });

        if (mDataset.get(position).getImg()!=null) {

            holder.image_user.setVisibility(View.VISIBLE);
            holder.text_first_user.setVisibility(View.GONE);

            final Uri displayPhotoUri = Uri.parse(mDataset.get(position).getImg());

            Glide.with(mContext)
                    .load(displayPhotoUri)
                    .apply(options)
                    .into(holder.image_user);

            holder.text_first_user.setText("");


        } else {
            holder.image_user.setVisibility(View.GONE);
            holder.text_first_user.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(null)
                    .apply(options)
                    .into(holder.image_user);

            holder.text_first_user.setText(String.valueOf(mDataset.get(position).getName().charAt(0)).toUpperCase());
        }
        holder.name_user.setText(mDataset.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}