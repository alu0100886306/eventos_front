package com.my.events;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class TopEvents extends AppCompatActivity implements View.OnClickListener{
    String MY_IP = "192.168.0.161:80";

    RequestQueue requestQueue;
    ArrayList<ArrayList<String>> result;
    ImageLoader mImageLoader;

    int[]images = {R.id.image,R.id.image2,R.id.image3,R.id.image4,R.id.image5,R.id.image6};
    int[]titles = {R.id.title,R.id.title2,R.id.title3,R.id.title4,R.id.title5,R.id.title6};
    int[]event_types = {R.id.event_type,R.id.event_type2,R.id.event_type3,R.id.event_type4,R.id.event_type5,R.id.event_type6};
    int[]events_list = {R.id.event1,R.id.event2,R.id.event3,R.id.event4,R.id.event5,R.id.event6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_events);
        Log.v("CREATE", "");

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
        findEvents("http://" + MY_IP + "/mywebservices/top_events.php");
        for (int value : events_list) findViewById(value).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i < events_list.length; i++)
            if (view.getId() == events_list[i])
                openActivity3(result.get(i));
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, Searcher.class);
        startActivity(intent);
    }

    public void openActivity3(ArrayList<String> record){
        Intent intent = new Intent(this, FullInfo.class);
        intent.putExtra("record", record);
        startActivity(intent);
    }

    private void set_data(int image, int title, int event_type, int i) {
        ((NetworkImageView)findViewById(image)).setImageUrl(result.get(i).get(13), mImageLoader);
        ((TextView)findViewById(title)).setText(result.get(i).get(8));
        ((TextView)findViewById(event_type)).setText(result.get(i).get(3));
    }

    private void findEvents(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                Log.v("RESP_LENGTH", ""+response.length());
                result = new ArrayList<ArrayList<String>>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        ArrayList<String> aux = new ArrayList<String>();
                        for (int j = 0; j < jsonObject.length()/2; j++) {
                            aux.add(new String(jsonObject.getString(Integer.toString(j)).getBytes("ISO-8859-1"), "UTF-8"));
                        }
                        result.add(aux);

                    } catch (JSONException | UnsupportedEncodingException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                for (int i = 0; i < result.size(); i++) {
                    set_data(images[i],titles[i],event_types[i],i);
                }
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
