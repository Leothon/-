package com.example.a10483.todayweather.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.a10483.todayweather.Adapter.WeatherdatarecyclerAdapter;
import com.example.a10483.todayweather.R;
import com.example.a10483.todayweather.data.weatherdata;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class showview extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private RecyclerView recyclerView;
    private ArrayList<weatherdata> weatherdataArrayList;
    private WeatherdatarecyclerAdapter weatherdatarecyclerAdapter;
    private final static String weathersurl="www.baidu.com";
    private final static String nowurl="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.showview,container,false);

        recyclerView=(RecyclerView)view.findViewById(R.id.temp_pic_now);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        binddata(weatherdataArrayList);

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(weathersurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson=new Gson();
                //此处将解析数据，添加至数组并最终添加到折线图中。
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonObjectRequest jsonObjectRequest2=new JsonObjectRequest(weathersurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson=new Gson();
                //此处将解析数据，添加至今天的天气实况
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
        requestQueue.add(jsonObjectRequest2);
        return view;
    }



    public void binddata(ArrayList<weatherdata> weatherdata){
        weatherdataArrayList=weatherdata;
        Collections.sort(weatherdata, new Comparator<com.example.a10483.todayweather.data.weatherdata>() {
            @Override
            public int compare(weatherdata lhs, weatherdata rhs) {
                return lhs.getTmp_min()-rhs.getTmp_min();
            }
        });
        int low=weatherdata.get(0).getTmp_min();

        Collections.sort(weatherdata, new Comparator<com.example.a10483.todayweather.data.weatherdata>() {
            @Override
            public int compare(weatherdata lhs, weatherdata rhs) {
                return rhs.getTmp_max()-lhs.getTmp_max();
            }
        });
        int high=weatherdata.get(0).getTmp_max();
        weatherdatarecyclerAdapter=new WeatherdatarecyclerAdapter(getContext(),weatherdataArrayList,low,high);
        recyclerView.setAdapter(weatherdatarecyclerAdapter);

    }


}
