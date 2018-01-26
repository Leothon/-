package com.example.a10483.todayweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class cityadd extends AppCompatActivity {

    private String[] provinceData;
    private String[] citiesData;
    private String[] areaData;

    private String selectedPro="";
    private String selectedCity="";
    private String selectedArea="";

    private Spinner provinceSp;
    private Spinner citiesSp;
    private Spinner areaSp;

    private EditText cityaddname;

    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> citiesAdapter;
    private ArrayAdapter<String> areaAdapter;

    private boolean ispermission;

    private Map<String,String[]> citiesDataMap=new HashMap<>();
    private Map<String,String[]> areaDataMap=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cityadd);
        checkPermission();
        String Stringjson=InitData();
        setJsonCitisData(Stringjson);

        provinceSp=(Spinner)findViewById(R.id.pri_name_sp);
        citiesSp=(Spinner)findViewById(R.id.city_name_sp);
        areaSp=(Spinner)findViewById(R.id.coun_name_sp);

        cityaddname=(EditText)findViewById(R.id.cityaddname);
        provinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provinceData);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSp.setAdapter(provinceAdapter);

        // 省份选择
        provinceSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPro = "";
                selectedPro = (String) parent.getSelectedItem();
                updateCity(selectedPro);//通过省名选择所有市列表

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 市选择
        citiesSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = "";
                selectedCity = (String) parent.getSelectedItem();
                updateArea(selectedCity);//根据所选市名选择所有区县列表

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 区选择
        areaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedArea = "";
                selectedArea = (String) parent.getSelectedItem();
                cityaddname.setText(selectedPro + selectedCity + selectedArea);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            ispermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;//是否授权，可以根据permission作为标记
        }
    }

    private void updateArea(String city) {

        String[] areas = areaDataMap.get(city);

        if (areas != null) {
            areaSp.setVisibility(View.VISIBLE);
            areaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas);
            areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            areaSp.setAdapter(areaAdapter);
            areaAdapter.notifyDataSetChanged();
            areaSp.setSelection(0);
        } else {
            cityaddname.setText(selectedPro + selectedCity);
            areaSp.setVisibility(View.GONE);
        }

    }

    private void updateCity(String pro) {

        String[] cities = citiesDataMap.get(pro);
        for (int i = 0; i < cities.length; i++) {
            citiesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
            citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            citiesSp.setAdapter(citiesAdapter);
            citiesAdapter.notifyDataSetChanged();
            citiesSp.setSelection(0);
        }

    }


    private void setJsonCitisData(String cityJson) {
        if (!TextUtils.isEmpty(cityJson)) {
            try {

                JSONArray array=new JSONArray(cityJson);
                provinceData = new String[array.length()];
                String mProvinceStr = null;
                for (int i = 0; i < array.length(); i++) {

                    JSONObject mProvinceObject = array.getJSONObject(i);
                    if (mProvinceObject.has("name")) {
                        mProvinceStr = mProvinceObject.getString("name");
                        provinceData[i] = mProvinceStr;
                    } else {
                        provinceData[i] = "unknown province";
                    }


                    String mCityStr = null;
                    JSONArray cityArray = mProvinceObject.getJSONArray("city");
                    citiesData = new String[cityArray.length()];
                    for (int j = 0; j < cityArray.length(); j++) {
                        JSONObject mCityObject = cityArray.getJSONObject(j);
                        if (mCityObject.has("name")) {
                            mCityStr = mCityObject.getString("name");
                            citiesData[j] = mCityStr;
                        } else {
                            citiesData[j] = "unknown city";
                        }
                        JSONArray areaArray = mCityObject.getJSONArray("area");

                        areaData = new String[areaArray.length()];
                        for (int m = 0; m < areaArray.length(); m++) {
                            JSONObject areaObject = areaArray.getJSONObject(m);
                            String mAreastr=areaObject.toString();
                            areaData[m]=mAreastr;
                            //Log.d("cityadd", areaData[m]);
                        }

                        areaDataMap.put(mCityStr, areaData);
                    }

                    citiesDataMap.put(mProvinceStr, citiesData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private String InitData(){
        StringBuffer sb=new StringBuffer();
        AssetManager assetManager=this.getAssets();

        try{
            InputStream is=assetManager.open("city.json");
            byte[] data=new byte[is.available()];
            int len=-1;
            while ((len=is.read(data))!=-1){
                sb.append(new String(data,0,len,"GBK"));
            }
            is.close();
            return sb.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
