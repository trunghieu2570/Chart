package com.kdh.chart.charts;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.kdh.chart.datatypes.AdvancedInputRow;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GroupBarChartView extends View implements ChartView<AdvancedInputRow> {


    private int soNhom, soBar;
    private ArrayList<Animator> animators;
    private AnimatorSet animSet;
    private ArrayList<Float> arr = new ArrayList<Float>();
    private int cl[];
    private ArrayList<String> str = new ArrayList<>();
    private Point origin;
    private Paint p;
    private Paint paintDrawText;
    private Paint paintDrawAxis;
    private int barWidth;
    private String chartName;
    private String xUnit;
    private String yUnit;


    private float max;

    public void init() {
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.RED);
        p.setStrokeWidth(1.5f);

        paintDrawText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDrawText.setStyle(Paint.Style.FILL);
        paintDrawText.setColor(Color.RED);
        paintDrawText.setStrokeWidth(1.5f);
        paintDrawText.setTextSize(20);

        paintDrawAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDrawAxis.setStyle(Paint.Style.FILL);
        paintDrawAxis.setStrokeWidth(3f);
        paintDrawAxis.setColor(Color.BLACK);
        animators = new ArrayList<Animator>();
        animSet = new AnimatorSet();
    }


    public void findBarWidth() {
        int x=soNhom-1+soNhom*soBar;
        int usageW=700-20;
        barWidth=usageW/x;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (arr.size() > 0) {
        //origin
        origin = new Point(100, getHeight() - 200);
        //draw axis
        canvas.drawLine(origin.x, origin.y, origin.x, origin.y - 530, paintDrawAxis);
        canvas.drawLine(origin.x, origin.y, origin.x + 800, origin.y, paintDrawAxis);
        //draw arrow
        canvas.drawLine(origin.x - 10, origin.y - 520, origin.x, origin.y - 530, paintDrawAxis);
        canvas.drawLine(origin.x + 10, origin.y - 520, origin.x, origin.y - 530, paintDrawAxis);
        canvas.drawLine(origin.x + 790, origin.y + 10, origin.x + 800, origin.y, paintDrawAxis);
        canvas.drawLine(origin.x + 790, origin.y - 10, origin.x + 800, origin.y, paintDrawAxis);


            float d = (float) 500.0 / max;
            int key = origin.x;
            findBarWidth();
            for (int i = 0; i < arr.size(); i++) {
                RectF rect = new RectF();
                if (i < soBar) {
                    rect.left = key + 20+i* barWidth;
                    rect.top = (float) (origin.y - arr.get(i) * d);
                    rect.right = rect.left + barWidth;
                    rect.bottom = origin.y;
                }
                if (i >= soBar) {
                    rect.left = key + 20+i * barWidth + barWidth * (i / soBar);
                    rect.top = (float) (origin.y - arr.get(i) * d);
                    rect.right = rect.left + barWidth;
                    rect.bottom = origin.y;
                }


                p.setColor(cl[i % soBar]);
                canvas.drawRect(rect, p);
                canvas.drawText(new DecimalFormat("0.0").format(arr.get(i)), rect.left, rect.top - 10, paintDrawText);
            }

        }
        showLabelYAxis(canvas);
        showLabelXAxis(canvas);
    }

    public void update(int soNhom, int soBar, ArrayList<String> str) {
        this.soNhom = soNhom;
        this.soBar = soBar;
        this.str = str;
        animSet.cancel();
        animators.clear();
        for (int i = 0; i < arr.size(); i++) {
            final int iii = i;
            ValueAnimator anim = ValueAnimator.ofFloat(0.0f, arr.get(iii));
            anim.setDuration(1000);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    arr.set(iii, (float) valueAnimator.getAnimatedValue());
                    invalidate();
                }
            });
            animators.add(anim);
        }
        animSet.playTogether(animators);
        animSet.start();
    }

    public float getMaxValue() {
        float max = Float.MIN_VALUE;
        for (int i = 0; i < arr.size(); i++)
            if (arr.get(i) > max)
                max = arr.get(i);
        return max;
    }

    public void showLabelYAxis(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float maxValueOfData = max;
        float yAxisValueInterval = 500.0f / (soBar * soNhom); //600-100
        float dataInterval = maxValueOfData / (soBar * soNhom);
        float valueToBeShown = maxValueOfData;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(20);


        for (int index = 0; index < (soNhom * soBar); index++) {
            String string = new DecimalFormat("0.0").format(valueToBeShown);

            Rect bounds = new Rect();
            paint.getTextBounds(string, 0, string.length(), bounds);
            int y = (int) ((origin.y - 500) + yAxisValueInterval * index);
            canvas.drawLine(origin.x - (10 >> 1), y, origin.x, y, paint);
            y = y + (bounds.height() >> 1);
            canvas.drawText(string, origin.x - bounds.width() - 10, y, paint);
            valueToBeShown = valueToBeShown - dataInterval;
        }

        //draw unit
        canvas.drawText(yUnit,origin.x,origin.y-550,paint);
    }

    public void showLabelXAxis(Canvas canvas) {
        //draw time
        Paint mypaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mypaint.setTextSize(25);
        mypaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        Rect bounds = new Rect();
        for (int i = 0; i < str.size(); i++) {
            //mypaint.getTextBounds(str.get(i), 0, str.get(i).length(), bounds);
            int y = origin.y + 30;
            int x;
            x=origin.x+30+(soBar+1)*i*barWidth;
            canvas.drawText(str.get(i), x, y, mypaint);
        }
        //draw chart name
        Point pointName = new Point();
        Paint paintName=new Paint(Paint.ANTI_ALIAS_FLAG);
        paintName.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        bounds=new Rect();
        paintName.getTextBounds(chartName, 0, chartName.length(), bounds);
        int temp=(getWidth()-bounds.width())/2;
        pointName.x = origin.x + temp;
        pointName.y = origin.y + 90;
        paintName.setTextSize(25);


        paintName.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(chartName, pointName.x, pointName.y, paintName);
        //draw unit
        canvas.drawText(xUnit,origin.x+802,origin.y,mypaint);
    }

    public GroupBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredHeight = 1000;
        int desiredWidth = 1000;


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    public void updateData(List<AdvancedInputRow> objects, String chartName,String xUnit,String yUnit) {

        this.chartName = chartName;
        this.xUnit=xUnit;
        this.yUnit=yUnit;
        int columns = objects.get(0).getValues().size();
        String[] timeLines = objects.get(0).getValues().toArray(new String[columns]);
        arr.clear();
        for (int i = 0; i < timeLines.length; i++) {
            for (int j = 1; j < objects.size(); j++) {
                String[] strs = objects.get(j).getValues().toArray(new String[columns]);
                this.arr.add(Float.parseFloat(strs[i]));
            }
        }

        max = getMaxValue();
        cl = new int[objects.size() - 1];
        for (int i = 1; i < objects.size(); i++) {
            cl[i - 1] = objects.get(i).getColor();
        }
        ArrayList<String> str = new ArrayList<String>();

        str.clear();
        for (String s : timeLines)
            str.add(s);

        this.update(timeLines.length, objects.size() - 1, str);

        System.out.println("đã vào đây");
    }

    @Override
    public void updateData(List<AdvancedInputRow> objects) {

    }
}
