package com.example.a10483.todayweather;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.a10483.todayweather.Adapter.weatherViewpagerAdapter;
import com.example.a10483.todayweather.data.nowWeatherData;
import com.example.a10483.todayweather.data.weatherdata;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mainViewpager;
    private ArrayList<View> viewlist=new ArrayList<>();
    private weatherViewpagerAdapter weatherViewpagerAdapter;
    private boolean ispermission;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();
        mainViewpager=(ViewPager)findViewById(R.id.viewpager_main);
        weatherViewpagerAdapter=new weatherViewpagerAdapter(viewlist);

        String city=getLocation();
        mainViewpager.setAdapter(weatherViewpagerAdapter);

    }

    public String getLocation(){
        String city="北京";
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
}

