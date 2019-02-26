package com.example.rikki.afcodetest;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private String url = "https://www.abercrombie.com/anf/nativeapp/qa/codetest/codeTest_exploreData.json";
    private ArrayList<CardItem> list = new ArrayList<>();
    private static ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sendRequest(url);

        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void sendRequest(String url) {
        showPDialog();
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG,response.toString());
                for(int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        CardItem item = new CardItem();
                        try {
                            item.setTitle(obj.getString("title"));
                        } catch (JSONException e){
                            item.setTitle("");
                        }
                        try {
                            item.setImageUrl(obj.getString("backgroundImage"));
                        } catch (JSONException e){
                            item.setImageUrl("");
                        }
                        try{
                            item.setPromo(obj.getString("promoMessage"));
                        } catch (JSONException e){
                            item.setPromo("");
                        }
                        try{
                            item.setTopDesc(obj.getString("topDescription"));
                        } catch (JSONException e){
                            item.setTopDesc("");
                        }
                        try{
                            String bottom = obj.getString("bottomDescription");
                            int position=0;
                            for(int j=0; j<bottom.length(); j++){
                                if(bottom.charAt(j)=='<'){
                                    position = j;
                                    break;
                                }
                            }
                            item.setBottomDesc(bottom.substring(0, position));  Log.d("Bottom",bottom.substring(0, position));
                            item.setLink(bottom.substring(position));  Log.d("Link",bottom.substring(position));
                        } catch (JSONException e){
                            item.setBottomDesc("");
                            item.setLink("");
                        }
                        try{
                            JSONArray content = obj.getJSONArray("content");
                            for(int j=0; j<content.length(); j++){
                                JSONObject c = content.getJSONObject(j);
                                item.setContent(j, c.getString("title"), c.getString("target"));
                            }
                        } catch (JSONException e){
                        }
                        list.add(i, item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callAdapter();
                disPDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                disPDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }

    private void callAdapter() {
        adapter.notifyDataSetChanged();
    }

    public static void showPDialog(){
        if (!pDialog.isShowing()){
            pDialog.setContentView(R.layout.progress_loading);
            pDialog.show();
        }
    }
    public static void disPDialog(){
        if (pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        list.clear();
    }
}
