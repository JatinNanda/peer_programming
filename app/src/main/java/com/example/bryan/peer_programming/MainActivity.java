package com.example.bryan.peer_programming;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    TextView text_get;
    EditText text_add;
    Button button_get;
    Button button_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String get_url = "http://ec2-52-55-73-180.compute-1.amazonaws.com/get_users"; //These two are the methods set in the database, the first being the get method
        String add_url = "http://ec2-52-55-73-180.compute-1.amazonaws.com/add_user"; //And the second being the add method
        final TextView text_get = findViewById(R.id.text_get); //We initialize our xml elements here
        final EditText text_add = findViewById(R.id.text_add);
        Button button_get = findViewById(R.id.button_get);
        Button button_add = findViewById(R.id.button_add);
        final RequestQueue queue = Volley.newRequestQueue(this); //the queue system to ensure the add and get method works fine

        final JsonObjectRequest get_request = new JsonObjectRequest(Request.Method.GET,get_url, null, new Response.Listener<JSONObject>() { //The get method, it pulls the database and post the list on the textview
            @Override
            public void onResponse(JSONObject response) {
                String myString = new String();
                JSONArray arr = null;
                try {
                    arr = (JSONArray) response.get("users");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    for (int i = 0; i < arr.length(); i++) {
                        myString += ", " + ((JSONObject) arr.get(i)).get("user");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                text_get.setText(myString);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Get_Wrong");
            }
        });
        final StringRequest post_request = new StringRequest(Request.Method.POST,add_url, new Response.Listener<String>() { //The add method, it puts whatever the user types on the edittext to the database
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Add_Wrong");
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/x-www-form-urlencoded");
                headers.put("user", text_add.getText().toString());
                return headers;
            }
        };



        button_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queue.add(get_request);
            }
        });
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queue.add(post_request);
            }
        });
    }
}