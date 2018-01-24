package com.example.a10483.todayweather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.a10483.todayweather.R;

import java.util.Map;
import java.util.jar.Attributes;

/**
 * Created by 10483 on 2018/1/24.
 */

public class weatherLineView extends View {

    private static final int defaultMinWidth=100;
    private static final int defaultMinHeight=80;
    private int defaultTextSize=16;
    private int defaultTextColor= Color.WHITE;
    private int weatherlinewidth=1;
    private int weatherdotradius=5;
    private int textdotdistance=5;
    private TextPaint textPaint;
    private Paint.FontMetrics fontMetrics;
    private Paint dotpaint;
    private Paint linepaint;
    private int lowesttempdata;
    private int highesttempdata;
    private int lowtemperdata[];
    private int hightemperdata[];

    public weatherLineView(Context context){
        this(context,null);
    }

    public weatherLineView(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
    }

    public weatherLineView(Context context,AttributeSet attributeSet,int defStyleAttr){
        super(context,attributeSet,defStyleAttr);
        init(context,attributeSet,defStyleAttr);
        initpaint();
    }

    public void setLowHighdata(int low[],int high[]){
        lowtemperdata=low;
        hightemperdata=high;
        invalidate();
    }

    public void setLowHighestdata(int low,int high){
        lowesttempdata=low;
        highesttempdata=high;
        invalidate();
    }

    public void initpaint(){
        textPaint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(defaultTextSize);
        textPaint.setColor(defaultTextColor);
        fontMetrics=textPaint.getFontMetrics();

        dotpaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        dotpaint.setStyle(Paint.Style.FILL);
        dotpaint.setColor(defaultTextColor);

        linepaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        linepaint.setStyle(Paint.Style.STROKE);
        linepaint.setStrokeWidth(weatherlinewidth);
        linepaint.setColor(defaultTextColor);

    }


    private void init(Context context,AttributeSet attributeSet,int defstyleAttr){
        TypedArray a=context.obtainStyledAttributes(attributeSet,R.styleable.WeatherLineView,defstyleAttr,0);
        defaultTextSize=(int)a.getDimension(R.styleable.WeatherLineView_temperTextSize,dp2px(context,defaultTextSize));
        defaultTextColor=a.getColor(R.styleable.WeatherLineView_weatextColor,Color.parseColor("#b07b5c"));
        weatherlinewidth=(int)a.getDimension(R.styleable.WeatherLineView_weaLineWidth,dp2px(context,weatherlinewidth));
        weatherdotradius=(int)a.getDimension(R.styleable.WeatherLineView_weadotRadius,dp2px(context,weatherdotradius));
        textdotdistance=(int)a.getDimension(R.styleable.WeatherLineView_textDotDistance,dp2px(context,textdotdistance));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int width=getSize(widthMode,widthSize,0);
        int height=getSize(heightMode,heightSize,1);
        setMeasuredDimension(width,height);
    }

    private int getSize(int mode,int size,int type){
        int result;
        if(mode==MeasureSpec.EXACTLY){
            result=size;
        }else{
            if(type==0){
                result=dp2px(getContext(),defaultMinWidth)+getPaddingLeft()+getPaddingRight();
            }else{
                int textHeight=(int)(fontMetrics.bottom-fontMetrics.top);
                result=dp2px(getContext(),defaultMinWidth)+2*textHeight+getPaddingTop()+getPaddingBottom()+2*textdotdistance;
            }

            if(mode==MeasureSpec.AT_MOST){
                result= Math.min(result,size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(lowtemperdata==null||hightemperdata==null||lowesttempdata==0||highesttempdata==0){
            return;
        }

        canvas.drawColor(Color.YELLOW);
        int textHight=(int)(fontMetrics.bottom-fontMetrics.top);
        int baseHeight=getHeight()-textHight-textdotdistance;

        // 最低温度相关
        // 最低温度中间
        int calowMiddle = baseHeight - cacHeight(lowtemperdata[1]);
        canvas.drawCircle(getWidth() / 2, calowMiddle, weatherdotradius, dotpaint);

        // 画温度文字
        String text = String.valueOf(lowtemperdata[1]) + "°";
        int baseX = (int) (canvas.getWidth() / 2 - textPaint.measureText(text) / 2);
        // mTextFontMetrics.top为负的
        // 需要加上文字高度和文字与圆点之间的空隙
        int baseY = (int) (calowMiddle - fontMetrics.top) + textdotdistance;
        canvas.drawText(text, baseX, baseY, textPaint);

        if (lowtemperdata[0] != 0) {
            // 最低温度左边
            int calowLeft = baseHeight - cacHeight(lowtemperdata[0]);
            canvas.drawLine(0, calowLeft, getWidth() / 2, calowMiddle, linepaint);
        }

        if (lowtemperdata[2] != 0) {
            // 最低温度右边
            int calowRight = baseHeight - cacHeight(lowtemperdata[2]);
            canvas.drawLine(getWidth() / 2, calowMiddle, getWidth(), calowRight, linepaint);
        }

        // 最高温度相关
        // 最高温度中间
        int calHighMiddle = baseHeight - cacHeight(hightemperdata[1]);
        canvas.drawCircle(getWidth() / 2, calHighMiddle, weatherdotradius, dotpaint);

        // 画温度文字
        String text2 = String.valueOf(hightemperdata[1]) + "°";
        int baseX2 = (int) (canvas.getWidth() / 2 - textPaint.measureText(text2) / 2);
        int baseY2 = (int) (calHighMiddle - fontMetrics.bottom) - textdotdistance;
        canvas.drawText(text2, baseX2, baseY2, textPaint);

        if (hightemperdata[0] != 0) {
            // 最高温度左边
            int calHighLeft = baseHeight - cacHeight(hightemperdata[0]);
            canvas.drawLine(0, calHighLeft, getWidth() / 2, calHighMiddle, linepaint);
        }

        if (hightemperdata[2] != 0) {
            // 最高温度右边
            int calHighRight = baseHeight - cacHeight(hightemperdata[2]);
            canvas.drawLine(getWidth() / 2, calHighMiddle, getWidth(), calHighRight, linepaint);
        }
    }

    private int cacHeight(int tem) {
        // 最低，最高温度之差
        int temDistance = highesttempdata - lowesttempdata;
        int textHeight = (int) (fontMetrics.bottom - fontMetrics.top);
        // view的最高和最低之差，需要减去文字高度和文字与圆点之间的空隙
        int viewDistance = getHeight() - 2 * textHeight - 2 * textdotdistance;
        // 今天的温度和最低温度之间的差别
        int currTemDistance = tem - lowesttempdata;
        return currTemDistance * viewDistance / temDistance;
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
