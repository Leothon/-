package com.example.a10483.todayweather.data;

/**
 * Created by 10483 on 2018/1/24.
 */

public class weatherdata {


    private String date;
    private String cond_txt_d;
    private int cond_code_d;
    private String cond_txt_n;
    private int cond_code_n;
    private int tmp_max;
    private int tmp_min;

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }

    public String getCond_txt_d(){
        return cond_txt_d;
    }
    public void setCond_txt_d(String cond_txt_d){
        this.cond_txt_d=cond_txt_d;
    }

    public int getCond_code_d(){
        return cond_code_d;
    }
    public void setCond_code_d(int cond_code_d){
        this.cond_code_d=cond_code_d;
    }

    public String getCond_txt_n(){
        return cond_txt_n;
    }
    public void setCond_txt_n(String cond_txt_n){
        this.cond_txt_n=cond_txt_n;
    }

    public int getCond_code_n(){
        return cond_code_n;
    }
    public void setCond_code_n(int cond_code_n){
        this.cond_code_n=cond_code_n;
    }

    public int getTmp_max(){
        return tmp_max;
    }
    public void setTmp_max(int tmp_max){
        this.tmp_max=tmp_max;
    }

    public int getTmp_min(){
        return tmp_min;
    }
    public void setTmp_min(int tmp_min){
        this.tmp_min=tmp_min;
    }
}
