package com.my.events;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolderAdapter> implements View.OnClickListener {

    ArrayList<ArrayList<String>> records;
    ImageLoader mImageLoader;

    private View.OnClickListener listener;

    public AdapterData(ArrayList<ArrayList<String>> records, ImageLoader mImageLoader) {
        this.records = records;
        this.mImageLoader = mImageLoader;
    }

    @NonNull
    @Override
    public ViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);

        view.setOnClickListener(this);

        return new ViewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdapter holder, int position) {
        holder.assignData(records.get(position), mImageLoader);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderAdapter extends RecyclerView.ViewHolder {

        TextView title, event_type, date_start, date_end, island;
        NetworkImageView image;

        public ViewHolderAdapter(@NonNull View itemView) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            event_type = (TextView) itemView.findViewById(R.id.event_type);
            date_start = (TextView) itemView.findViewById(R.id.date_start);
            date_end = (TextView) itemView.findViewById(R.id.date_end);
            island = (TextView) itemView.findViewById(R.id.island);

        }

        public void assignData(ArrayList<String> strings, ImageLoader mImageLoader) {

            image.setImageUrl(strings.get(13), mImageLoader);
            title.setText(strings.get(8));
            event_type.setText(strings.get(3));
            date_start.setText(strings.get(0));
            date_end.setText(strings.get(1));
            island.setText(strings.get(4));
        }
    }
}
