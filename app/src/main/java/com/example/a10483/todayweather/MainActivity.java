package com.example.a10483.todayweather;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.a10483.todayweather.Adapter.WeatherdatarecyclerAdapter;
import com.example.a10483.todayweather.Adapter.weatherViewpagerAdapter;
import com.example.a10483.todayweather.data.nowWeatherData;
import com.example.a10483.todayweather.data.weatherdata;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ViewPager mainViewpager;
    private ArrayList<View> viewlist=new ArrayList<>();
    private weatherViewpagerAdapter weatherViewpagerAdapter;
    private boolean ispermission;
    private int count=0;
    private TextView wea_now;
    private ImageView tmp_now;
    private TextView win_dir;
    private TextView win_sc;
    private TextView hum_now;
    private TextView pcpn_now;
    private TextView airnum_now;
    private TextView mainpul_now;
    private TextView airqua_now;
    private TextView pm10_now;
    private TextView pm25_now;
    private TextView so2_now;
    private RecyclerView recyclerView;
    private ArrayList<weatherdata> weatherdataArrayList;
    private TextView city_name;
    private ImageView city_manage;
    private ImageView city_add;
    private WeatherdatarecyclerAdapter weatherdatarecyclerAdapter;
    private static final String key="a4edf8532f7e43019279c3ec59cdb2d9";
    private static final String weathernowurl="https://free-api.heweather.com/s6/weather/now";
    private static final String foreurl="https://free-api.heweather.com/s6/weather/forecast";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(200);
        initPermission();
        mainViewpager=(ViewPager)findViewById(R.id.viewpager_main);
        weatherViewpagerAdapter=new weatherViewpagerAdapter(viewlist);
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        View view=layoutInflater.inflate(R.layout.showview,null);
        viewlist.add(view);
        mainViewpager.setCurrentItem(0);
        wea_now=(TextView)view.findViewById(R.id.wea_now);
        tmp_now=(ImageView)view. findViewById(R.id.tmp_now);
        win_dir=(TextView)view.findViewById(R.id.win_dir);
        win_sc=(TextView)view.findViewById(R.id.win_sc);
        hum_now=(TextView)view.findViewById(R.id.hum_now);
        pcpn_now=(TextView)view.findViewById(R.id.pcpn_now);
        airnum_now=(TextView)view.findViewById(R.id.airnum_now);
        mainpul_now=(TextView)view.findViewById(R.id.mainpul_now);
        airqua_now=(TextView)view.findViewById(R.id.airqua_now);
        pm10_now=(TextView)view.findViewById(R.id.pm10_now);
        pm25_now=(TextView)view.findViewById(R.id.pm25_now);
        so2_now=(TextView)view.findViewById(R.id.so2_now);
        recyclerView=(RecyclerView)view.findViewById(R.id.tmp_pic_now);//此处的findviewbyid一定要家view，因为它的id是在添加的view中获取的，而不是本activity的view中。
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        city_add=(ImageView)findViewById(R.id.city_add);
        city_manage=(ImageView)findViewById(R.id.city_manage);
        city_name=(TextView)findViewById(R.id.city_name);


        weatherdataArrayList=new ArrayList<>();
        String city=getLocation();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String foresturl=foreurl+"?key="+key+"&location="+city;
        setfordata(foresturl, requestQueue, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try{

                    Gson gson=new Gson();
                    //此处将解析数据，添加至数组并最终添加到折线图中。
                    String jsonString=response.toString();
                    JsonParser parser=new JsonParser();
                    JsonObject jsonObject=parser.parse(jsonString).getAsJsonObject();
                    JsonArray forestArray=jsonObject.getAsJsonArray("HeWeather6");
                    JsonElement el=forestArray.get(0);
                    JsonObject object=el.getAsJsonObject();
                    JsonArray array=object.getAsJsonArray("daily_forecast");

                    for(int i=0;i<array.size();i++){
                        JsonElement forestel=array.get(i);
                        weatherdata wd=gson.fromJson(forestel,weatherdata.class);
                        weatherdataArrayList.add(wd);
                    }

                    binddata(weatherdataArrayList);
                    Log.d("MainActivity","需要的数组长度"+weatherdataArrayList.size());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        mainViewpager.setAdapter(weatherViewpagerAdapter);
        String nowurl=weathernowurl+"?key="+key+"&location="+city;


        setNowdata(nowurl,requestQueue);

        setWeightlistener();

    }

    public void setWeightlistener(){
        city_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,citymanage.class);
                startActivity(intent);
            }
        });

        city_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,cityadd.class);
                startActivity(intent);
            }
        });
    }

    public void binddata(ArrayList<weatherdata> weatherdatalist){
        //weatherdataArrayList=weatherdata;
        ArrayList<weatherdata> list=weatherdatalist;

        Collections.sort(weatherdatalist, new Comparator<weatherdata>() {
            @Override
            public int compare(weatherdata lhs, weatherdata rhs) {
                return lhs.getTmp_min()-rhs.getTmp_min();
            }
        });

        int low=weatherdatalist.get(0).getTmp_min();

        Collections.sort(weatherdatalist, new Comparator<weatherdata>() {
            @Override
            public int compare(weatherdata lhs, weatherdata rhs) {
                return rhs.getTmp_max()-lhs.getTmp_max();
            }
        });
        int high=weatherdatalist.get(0).getTmp_max();
        weatherdatarecyclerAdapter=new WeatherdatarecyclerAdapter(this,list,low,high);
        recyclerView.setAdapter(weatherdatarecyclerAdapter);

    }
    public void setfordata(String url, RequestQueue requestQueue,final VolleyCallback volleyCallback){

        JsonObjectRequest jsonforest=new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /*try{
                    Gson gson=new Gson();
                    //此处将解析数据，添加至数组并最终添加到折线图中。
                    String jsonString=response.toString();
                    JsonParser parser=new JsonParser();
                    JsonObject jsonObject=parser.parse(jsonString).getAsJsonObject();
                    JsonArray forestArray=jsonObject.getAsJsonArray("HeWeather6");
                    JsonElement el=forestArray.get(0);
                    JsonObject object=el.getAsJsonObject();
                    JsonArray array=object.getAsJsonArray("daily_forecast");

                    for(int i=0;i<array.size();i++){
                        JsonElement forestel=array.get(i);
                        weatherdata wd=gson.fromJson(forestel,weatherdata.class);
                        weatherdataArrayList.add(wd);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }*/
                volleyCallback.onSuccess(response);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MainActivity","网络出错");
            }
        });
        requestQueue.add(jsonforest);
    }
    public void setNowdata(String url, RequestQueue requestQueue){
        JsonObjectRequest jsonnow=new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.d("MainActivity","此处运行");
                    String jsonString=response.toString();
                    JsonParser jsonParser=new JsonParser();
                    JsonObject jsonObject=jsonParser.parse(jsonString).getAsJsonObject();
                    Gson gson=new Gson();
                    JsonArray nowjsonarray=jsonObject.getAsJsonArray("HeWeather6");
                    JsonElement elnow=nowjsonarray.get(0);
                    JsonObject nowobject=elnow.getAsJsonObject();
                    JsonObject now=nowobject.getAsJsonObject("now");
                    JsonObject location=nowobject.getAsJsonObject("basic");
                    String locationString=location.toString();
                    JSONObject locationobject=new JSONObject(locationString);
                    String locationcity=locationobject.getString("location");
                    city_name.setText(locationcity);
                    nowWeatherData nowweatherdata=gson.fromJson(now,nowWeatherData.class);
                    wea_now.setText(nowweatherdata.getCond_txt());
                    int tmp=nowweatherdata.getTmp();
                    int drawableid=transtmp(tmp);

                    tmp_now.setImageResource(drawableid);
                    win_dir.setText(nowweatherdata.getWind_dir());
                    win_sc.setText(nowweatherdata.getWind_sc());
                    hum_now.setText(nowweatherdata.getHum());
                    pcpn_now.setText(nowweatherdata.getPcpn());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonnow);
    }
    public int transtmp(int tmp){
        Context context=getBaseContext();
        if(tmp<0){
            String strid="ic_stat_temp_bz"+Math.abs(tmp);
            int id=getResources().getIdentifier(strid,"drawable",context.getPackageName());//通过文件名获取ID
            return id;
        }else{
            String strid="ic_stat_temp_"+tmp;
            int id=getResources().getIdentifier(strid,"drawable",context.getPackageName());
            return id;
        }
    }
    public String getLocation(){
        String city="洛阳";
        return city;//获取定位
    }

    public void addpage(nowWeatherData nwd){//增加页面，增添数据

        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.showview,null);
        viewlist.add(view);
        weatherViewpagerAdapter.notifyDataSetChanged();
    }
    public void delpage(){
        int position=mainViewpager.getCurrentItem();
        viewlist.remove(position);
        weatherViewpagerAdapter.notifyDataSetChanged();
    }

    private void initPermission() {
        int permission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            //需不需要解释的dialog
            if (shouldRequest()) return;
            //请求权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    private boolean shouldRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            explainDialog();
            return true;
        }
        return false;
    }

    private void explainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("应用需要获取您的定位权限,是否授权？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求权限
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    }
                }).setNegativeButton("取消", null)
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            ispermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;//是否授权，可以根据permission作为标记
        }
    }

    public interface VolleyCallback{
        void onSuccess(JSONObject  jsonObject);
    }
}

