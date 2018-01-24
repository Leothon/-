package com.example.a10483.todayweather.data;

import java.util.ArrayList;

/**
 * Created by 10483 on 2018/1/24.
 */

public class nowWeatherData {
    private String location;
    private int tmp;
    private int cond_code;
    private String cond_txt;
    private String wind_dir;
    private String wind_sc;
    private int hum;
    private int pcpn;

    private ArrayList<weatherdata> weatherdataArrayList;

    public ArrayList<weatherdata> getWeatherdataArrayList(){
        return weatherdataArrayList;
    }
    public void setWeatherdataArrayList(ArrayList<weatherdata> weatherdataArrayList){
        this.weatherdataArrayList=weatherdataArrayList;
    }

    public int getHum(){
        return hum;
    }
    public void setHum(int hum){
        this.hum=hum;
    }

    public int getPcpn(){
        return pcpn;
    }
    public void setPcpn(int pcpn){
        this.pcpn=pcpn;
    }
    public String getWind_sc(){
        return wind_sc;
    }
    public void setWind_sc(String wind_sc){
        this.wind_sc=wind_sc;
    }

    public String getWind_dir(){
        return wind_dir;
    }
    public void setWind_dir(String wind_dir){
        this.wind_dir=wind_dir;
    }

    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location=location;
    }

    public int getTmp(){
        return tmp;
    }
    public void setTmp(int tmp){
        this.tmp=tmp;
    }

    public int getCond_code(){
        return cond_code;
    }
    public void setCond_code(int cond_code){
        this.cond_code=cond_code;
    }

    public String getCond_txt(){
        return cond_txt;
    }
    public void setCond_txt(String cond_txt){
        this.cond_txt=cond_txt;
    }
}
