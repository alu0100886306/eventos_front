package com.my.events;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText startDate,endDate;
    EditText municipality, space, place, title, description;
    Spinner dp_event_types, dp_islands;
    Button find;

    String MY_IP = "192.168.0.161:80";

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDate=(EditText)findViewById(R.id.startDate);
        endDate=(EditText)findViewById(R.id.endDate);
        municipality=(EditText)findViewById(R.id.municipaly);
        space=(EditText)findViewById(R.id.space);
        place=(EditText)findViewById(R.id.place);
        title=(EditText)findViewById(R.id.title);
        description=(EditText)findViewById(R.id.description);

        dp_event_types=(Spinner)findViewById(R.id.eventType);
        dp_islands=(Spinner)findViewById(R.id.island);

        find=(Button)findViewById(R.id.find);

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        String[] ev_types = new String[]{"Todos", "Varios", "Teatro", "Música", "Literatura", "Infantiles", "Exposiciones", "Danza", "Cursos y Conferencias", "Cualquier", "Cine"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ev_types);
        dp_event_types.setAdapter(adapter);

        String[] islands = new String[]{"Todas", "El Hierro", "La Palma", "La Gomera", "Tenerife", "Gran Canaria", "Fuerteventura", "Lanzarote"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, islands);
        dp_islands.setAdapter(adapter2);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://"+ MY_IP +"/mywebservices/buscar_producto.php?";
                if (startDate.getText().toString().length() > 0) url += "startDate=" + startDate.getText().toString() + "&";
                Log.v("Date Start",startDate.getText().toString());
                if (endDate.getText().toString().length() > 0) url += "endDate=" + endDate.getText().toString() + "&";
                Log.v("Date End",endDate.getText().toString());
                if (dp_event_types.getSelectedItem().toString().length() > 0) url += "eventType=" + dp_event_types.getSelectedItem().toString() + "&";
                Log.v("Event Type",dp_event_types.getSelectedItem().toString());
                if (dp_islands.getSelectedItem().toString().length() > 0) url += "island=" + dp_islands.getSelectedItem().toString() + "&";
                Log.v("Island",dp_islands.getSelectedItem().toString());
                if (municipality.getText().toString().length() > 0) url += "municipality=" + municipality.getText().toString() + "&";
                Log.v("Municipality",municipality.getText().toString());
                if (space.getText().toString().length() > 0) url += "space=" + space.getText().toString() + "&";
                Log.v("Space",space.getText().toString());
                if (place.getText().toString().length() > 0) url += "place=" + place.getText().toString() + "&";
                Log.v("Place",place.getText().toString());
                if (title.getText().toString().length() > 0) url += "title=" + title.getText().toString() + "&";
                Log.v("Title",title.getText().toString());
                if (description.getText().toString().length() > 0) url += "description=" + description.getText().toString() + "&";
                Log.v("Description",description.getText().toString());
                url = url.replace(" ", "%20");
                Log.v("URL",url);
                findEvents(url);
            }
        });
    }

    public void openActivity2(ArrayList<ArrayList<String>> response){
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("length", response.size());
        for (int i = 0; i < response.size(); i++) {
            intent.putStringArrayListExtra(Integer.toString(i),response.get(i));
        }
        startActivity(intent);
    }

    private void findEvents(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                //Log.v("RESP_LENGTH", ""+response.length());
                ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
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
                openActivity2(result);
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

    @Override
    public void onClick(View view) {

        int mYear;
        int mMonth;
        int mDay;
        if (view == startDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == endDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            endDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }
}

