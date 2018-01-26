package com.example.a10483.todayweather.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a10483.todayweather.R;
import com.example.a10483.todayweather.data.weatherdata;
import com.example.a10483.todayweather.view.weatherLineView;

import java.util.List;

/**
 * Created by 10483 on 2018/1/24.
 */

public class WeatherdatarecyclerAdapter extends RecyclerView.Adapter<WeatherdatarecyclerAdapter.WeatherDataViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<weatherdata> mDatas;
    private int mLowestTem;
    private int mHighestTem;

    public WeatherdatarecyclerAdapter(Context context, List<weatherdata> datats, int lowtem, int hightem) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        mLowestTem = lowtem;
        mHighestTem = hightem;
    }

    @Override
    public WeatherDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.weatherchangeview, parent, false);
        WeatherDataViewHolder viewHolder = new WeatherDataViewHolder(view);
        viewHolder.dayText = (TextView) view.findViewById(R.id.id_day_text_tv);
        viewHolder.dayIcon = (ImageView) view.findViewById(R.id.id_day_icon_iv);
        viewHolder.weatherLineView = (weatherLineView) view.findViewById(R.id.wea_line);
        viewHolder.nighticon = (ImageView) view.findViewById(R.id.id_night_icon_iv);
        viewHolder.nightText = (TextView) view.findViewById(R.id.id_night_text_tv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherDataViewHolder holder, int position) {
        // 最低温度设置为15，最高温度设置为30
        Resources resources = mContext.getResources();
        weatherdata weatherdata = mDatas.get(position);
        holder.dayText.setText(weatherdata.getCond_txt_d());
        int iconday = resources.getIdentifier("ic"+weatherdata.getCond_code_d(), "drawable", mContext.getPackageName());
        if (iconday == 0) {
            holder.dayIcon.setImageResource(R.drawable.ic100);
        } else {
            holder.dayIcon.setImageResource(iconday);
        }
        holder.weatherLineView.setLowHighestdata(mLowestTem, mHighestTem);
        int iconight = resources.getIdentifier("ic" + weatherdata.getCond_code_d(), "drawable", mContext.getPackageName());
        if (iconight == 0) {
            holder.nighticon.setImageResource(R.drawable.ic_sun_night);
        } else {
            holder.nighticon.setImageResource(iconight);
        }
        holder.nightText.setText(weatherdata.getCond_txt_n());
        int low[] = new int[3];
        int high[] = new int[3];
        low[1] = weatherdata.getTmp_min();
        high[1] = weatherdata.getTmp_max();
        if (position <= 0) {
            low[0] = 0;
            high[0] = 0;
        } else {
            weatherdata weatherdataLeft = mDatas.get(position - 1);
            low[0] = (weatherdataLeft.getTmp_min() + weatherdata.getTmp_min()) / 2;
            high[0] = (weatherdataLeft.getTmp_max() + weatherdata.getTmp_max()) / 2;
        }
        if (position >= mDatas.size() - 1) {
            low[2] = 0;
            high[2] = 0;
        } else {
            weatherdata weatherdataRight = mDatas.get(position + 1);
            low[2] = (weatherdata.getTmp_min() + weatherdataRight.getTmp_min()) / 2;
            high[2] = (weatherdata.getTmp_max() + weatherdataRight.getTmp_max()) / 2;
        }
        holder.weatherLineView.setLowHighdata(low, high);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class WeatherDataViewHolder extends RecyclerView.ViewHolder {

        TextView dayText;
        ImageView dayIcon;
        weatherLineView weatherLineView;
        ImageView nighticon;
        TextView nightText;

        public WeatherDataViewHolder(View itemView) {
            super(itemView);
        }
    }


}
