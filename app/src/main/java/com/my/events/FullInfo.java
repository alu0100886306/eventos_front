package com.my.events;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class FullInfo extends AppCompatActivity {

    ArrayList<String> record;
    TextView title, event_type, date_start, date_end, island, duration, time, municipaly, place, description;
    NetworkImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_info_view);

        Intent intent = getIntent();
        record = intent.getStringArrayListExtra("record");
        Log.v("RECORD", record.toString());

        image = (NetworkImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        event_type = (TextView) findViewById(R.id.event_type);
        date_start = (TextView) findViewById(R.id.date_start);
        date_end = (TextView) findViewById(R.id.date_end);
        island = (TextView) findViewById(R.id.island);
        duration = (TextView) findViewById(R.id.duration);
        time = (TextView) findViewById(R.id.time);
        municipaly = (TextView) findViewById(R.id.municipaly);
        place = (TextView) findViewById(R.id.place);
        description = (TextView) findViewById(R.id.description);

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        image.setImageUrl(record.get(13), mImageLoader);
        title.setText(record.get(8));
        event_type.setText(record.get(3));
        date_start.setText(record.get(0));
        date_end.setText(record.get(1));
        island.setText(record.get(4));
        if (!record.get(2).equals("0"))
            duration.setText(record.get(2) + " days");
        if (!record.get(10).equals("--") && !record.get(11).equals("--"))
            time.setText(record.get(10) + ':' + record.get(11));
        municipaly.setText(record.get(5));
        String p = "";
        if (!record.get(6).equals("---") && !record.get(6).equals(""))
            p += record.get(6);
        if (!record.get(7).equals("---") && !record.get(7).equals("")) {
            if (p.length() > 0)
                p += '-';
            p += record.get(7);
        }
        place.setText(p);
        description.setText(record.get(9));
    }

}
