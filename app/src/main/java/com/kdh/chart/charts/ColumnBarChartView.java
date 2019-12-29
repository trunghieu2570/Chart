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
import java.util.List;

public class ColumnBarChartView extends View implements ChartView<AdvancedInputRow> {


    private Paint p;
    private Paint paintDrawAxis;
    private int soNhom, soBar;
    private ArrayList<Animator> animators;
    private AnimatorSet animSet;
    private ArrayList<Float> arrPureData = new ArrayList<Float>();
    private ArrayList<Float> arrDrawData = new ArrayList<Float>();
    private int[] cl;
    private String[] groupsName;
    private float max = Float.MIN_VALUE;
    private Point origin;
    private String chartName;
    private String xUnit;
    private String yUnit;
    private int barWidth;

    public ColumnBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.RED);
        p.setStrokeWidth(1.5f);

        paintDrawAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDrawAxis.setStyle(Paint.Style.FILL);
        paintDrawAxis.setStrokeWidth(3f);
        paintDrawAxis.setColor(Color.BLACK);
        animators = new ArrayList<Animator>();
        animSet = new AnimatorSet();
    }


    public float findMax() {
        for (int i = 0; i < arrPureData.size(); i += soBar) {
            if (i % soBar == 0) {
                int x = i / soBar;
                int start = i;
                int end = i + soBar - 1;
                System.out.println("Start là: " + start + " ,End là: " + end);
                for (int j = end; j >= start; j--) {
                    float temp = 0.0f;
                    for (int k = start; k <= j; k++)
                        temp += arrPureData.get(k);
                    if (temp > max)
                        max = temp;
                }
            }
        }
        return max;
    }


    public int findBarwidth(){
        int tem=2*soNhom-1;
        int usageW=750-20;
        barWidth=usageW/tem;
        return barWidth;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (arrDrawData.size() > 0) {
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


            findMax();
            findBarwidth();
            float d = (float) 500.0 / max;
            int key = origin.x;

            for (int i = 0; i < arrDrawData.size(); i++) {
                RectF rect = new RectF();
                if (i % soBar == 0) {
                    int x = i / soBar;
                    int start = i;
                    int end = i + soBar - 1;
                    System.out.println("Start là: " + start + " ,End là: " + end);
                    for (int j = end; j >= start; j--) {
                        if ((i < soBar))
                            rect.left = key + 20;
                        else
                            rect.left = key + 20 + (i/ soBar)*2 * barWidth;
                        rect.top = origin.y - arrDrawData.get(j) * d;
                        rect.right = rect.left + barWidth;
                        rect.bottom = origin.y;
                        p.setColor(cl[j % soBar]);
                        canvas.drawRect(rect, p);
                    }
                }
                showLabelYAxis(canvas);
                showLabelXAxis(canvas);
            }
        }
    }

    public void showLabelYAxis(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float maxValueOfData = (int) max;
        float yAxisValueInterval = 500.0f / 9; //600-100
        float dataInterval = maxValueOfData / 9;
        float valueToBeShown = maxValueOfData;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(20);


        for (int index = 0; index < 9; index++) {
            String string = new DecimalFormat("0.0").format(valueToBeShown);
            Rect bounds = new Rect();
            paint.getTextBounds(string, 0, string.length(), bounds);
            int y = (int) ((origin.y - 500) + yAxisValueInterval * index);
            canvas.drawLine(origin.x - (10 >> 1), y, 100, y, paint);
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
        mypaint.setTextSize(20);
        mypaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        Rect bounds = new Rect();
        for (int i = 0; i < groupsName.length; i++) {
            //mypaint.getTextBounds(groupsName[i], 0, groupsName[i].length(), bounds);
            int y = origin.y + 30;
            int x;
            x=origin.x +30+ i*2 * barWidth;
            canvas.drawText(groupsName[i], x, y, mypaint);
        }
        //draw chart name
        Point pointName = new Point();
        Paint paintName=new Paint();
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

    public void processData() {
        arrDrawData.clear();
        for (int i = 0; i < arrPureData.size(); i++)
            arrDrawData.add(0.0f);
        for (int i = 0; i < arrPureData.size(); i++) {
            if (i % soBar == 0) {
                int x = i / soBar;//nhóm mấy
                int start = i;
                int end = i + soBar - 1;
                System.out.println("Start là: " + start + " ,End là: " + end);
                for (int j = end; j >= start; j--) {
                    int jj = j;
                    float temp = 0.0f;
                    for (int k = start; k <= j; k++)
                        temp += arrPureData.get(k);
                    arrDrawData.set(j, temp);
                }
            }
        }
    }

    public void update(String[] groupsName, int soNhom, int soBar) {
        this.soNhom = soNhom;
        this.soBar = soBar;
        this.groupsName = groupsName;
        animSet.cancel();
        animators.clear();
        arrDrawData.clear();

        processData();
        for (int i = 0; i < arrDrawData.size(); i++) {
            final int iii = i;
            ValueAnimator anim = ValueAnimator.ofFloat(0, arrDrawData.get(iii));
            anim.setDuration(1000);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    arrDrawData.set(iii, (float) valueAnimator.getAnimatedValue());
                    invalidate();
                }
            });
            animators.add(anim);
        }
        animSet.playTogether(animators);
        animSet.start();
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
        arrPureData.clear();
        for (int i = 0; i < timeLines.length; i++) {
            for (int j = 1; j < objects.size(); j++) {
                String[] strs = objects.get(j).getValues().toArray(new String[columns]);
                this.arrPureData.add(Float.parseFloat(strs[i]));
            }
        }
        cl = new int[objects.size() - 1];
        for (int i = 1; i < objects.size(); i++) {
            cl[i - 1] = objects.get(i).getColor();
        }
        this.update(timeLines, timeLines.length, objects.size() - 1);
    }

    @Override
    public void updateData(List<AdvancedInputRow> objects) {

    }
}