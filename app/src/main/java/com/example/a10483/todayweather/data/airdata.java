package com.example.a10483.todayweather.data;

/**
 * Created by 10483 on 2018/1/28.
 */

public class airdata {

    private String aqi;
    private String main;
    private String pm10;
    private String pm25;
    private String qlty;
    private String so2;

    public String getAqi(){
        return aqi;
    }
    public void setAqi(String aqi){
        this.aqi=aqi;
    }

    public String getMain(){
        return main;
    }
    public void setMain(String main){
        this.main=main;
    }

    public String getPm10(){
        return pm10;
    }
    public void setPm10(String pm10){
        this.pm10=pm10;
    }

    public String getPm25(){
        return pm25;
    }
    public void setPm25(String pm25){
        this.pm25=pm25;
    }

    public String getQlty(){
        return qlty;
    }
    public void setQlty(String qlty){
        this.qlty=qlty;
    }

    public String getSo2(){
        return so2;
    }
    public void setSo2(String so2){
        this.so2=so2;
    }

}
