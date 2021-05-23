package com.my.events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;

public class Listing extends AppCompatActivity {

    ArrayList<ArrayList<String>> records;
    RecyclerView recycler;

    RequestQueue requestQueue;

    String MY_IP = "192.168.0.161:80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_view);

        recycler = (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Intent intent = getIntent();
        int len = intent.getIntExtra("length",0);
        records = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < len; i++) {
            records.add( intent.getStringArrayListExtra(Integer.toString(i)) );
        }
        //Log.v("Records in Activ2", ""+records.size());
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

        for (int i = 0; i < records.size(); i++) {
            for (int j = 0; j < records.get(i).size(); j++) {
                Log.v("RESP " + i + " " + j, records.get(i).get(j));
            }


        }

        AdapterData adapter = new AdapterData(records, mImageLoader);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("RESP ", records.get(recycler.getChildAdapterPosition(v)).get(4));
                Toast.makeText(getApplicationContext(),records.get(recycler.getChildAdapterPosition(v)).get(4),Toast.LENGTH_SHORT).show();

                plusOne("http://"+ MY_IP +"/mywebservices/event_click.php?id="+ records.get(recycler.getChildAdapterPosition(v)).get(12));
                openActivity3(records.get(recycler.getChildAdapterPosition(v)));
            }
        });
        recycler.setAdapter(adapter);
    }
    public void openActivity3(ArrayList<String> record){
        Intent intent = new Intent(this, FullInfo.class);
        intent.putExtra("record", record);
        startActivity(intent);
    }
    private void plusOne(String URL){
        Log.v("URL", ""+URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.v("RESPONSE", ""+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("ERROR",error.toString());
                Toast.makeText(getApplicationContext(), "CONNECTION ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}