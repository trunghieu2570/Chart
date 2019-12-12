package com.kdh.chart.charts;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.kdh.chart.datatypes.SimpleInputRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DonutChartView extends View {

    public class Item {

        private float startAngle;
        private float sweepAngle = 0;
        private int color;
        private float percent;

        Item(float startAngle, int color) {
            this.startAngle = startAngle;
            this.color = color;
        }
    }

    private Paint donutPaint = null;
    private Paint textPaint = null;
    private ArrayList<Item> items;
    ArrayList<Animator> animators;
    AnimatorSet animSet;
    private RectF mainBound;
    private float cx;
    private float cy;
    private int mainRadius;


    public DonutChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        items = new ArrayList<>();
        initPaint();
        initAnimator();
    }

    private void initPaint() {
        //donutPaint
        donutPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        donutPaint.setColor(Color.RED);
        donutPaint.setStyle(Paint.Style.FILL);
        donutPaint.setStrokeWidth(5);
        //textPaint
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50);
        Typeface f = Typeface.create("", Typeface.BOLD);
        textPaint.setTypeface(f);
        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    private void initAnimator() {
        animators = new ArrayList<>();
        animSet = new AnimatorSet();

    }


    public void updateData(ArrayList<SimpleInputRow> inputRows) {
        List<SimpleInputRow> list = inputRows.subList(1, inputRows.size());
        animSet.cancel();
        items.clear();
        animators.clear();
        int sum = 0;
        float currentAngle = 0;
        int color;
        for (SimpleInputRow row : list) {
            sum += Float.parseFloat(0 + row.getValue());
        }
        float u = 360f / sum;
        float p = 100f / sum;for (SimpleInputRow row : list) {
            color = row.getColor();
            final Item item = new Item(currentAngle, color);
            item.percent = Float.parseFloat(0 + row.getValue()) * p;
            currentAngle += Float.parseFloat(0 + row.getValue()) * u;
            //add animator
            ValueAnimator anim = ValueAnimator.ofFloat(0, currentAngle);
            anim.setDuration(1000);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    item.sweepAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            items.add(item);
            animators.add(anim);

        }

        Collections.reverse(items);
        Collections.reverse(animators);

        animSet.playTogether(animators);
        animSet.start();
    }

    /*public void updateAngleSequentially(int[] values) {
        animSet.cancel();
        items.clear();
        animators.clear();
        int sum = 0;
        float currentAngle = 0;
        int color = 0;
        for (int value : values) {
            sum += value;
        }
        float u = 360f / sum;
        float p = 100f / sum;
        Random rnd = new Random();
        for (int value : values) {
            color = getRandomColor();

            final Item item = new Item(currentAngle, color);
            currentAngle += value * u;
            item.percent = value * p;
            //add animator
            ValueAnimator anim = ValueAnimator.ofFloat(0, currentAngle);
            anim.setDuration(3000 / values.length);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    item.sweepAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            items.add(item);
            animators.add(anim);
        }

        Collections.reverse(items);
        Collections.reverse(animators);

        animSet.playSequentially(animators);
        animSet.start();
    }*/

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx = (getX() + w) / 2;
        cy = (getY() + h) / 2;
        mainRadius = w < h ? w / 2 : h / 2;
        mainRadius -= 150;
        mainBound = new RectF(cx - mainRadius, cy - mainRadius, cx + mainRadius, cy + mainRadius);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Item item : items) {
            donutPaint.setColor(item.color);
            donutPaint.setStyle(Paint.Style.STROKE);
            donutPaint.setStrokeCap(Paint.Cap.BUTT);
            donutPaint.setStrokeWidth(200);
            canvas.drawArc(mainBound, -90, item.sweepAngle, false, donutPaint);
            //donutPaint.setColor(Color.WHITE);
            //donutPaint.setStyle(Paint.Style.STROKE);
            //canvas.drawArc(mainBound, -90, item.sweepAngle, true, donutPaint);
            float medianAngle = (item.startAngle - 90 + ((item.sweepAngle - item.startAngle) / 2f)) * (float) Math.PI / 180f;
            canvas.drawText(String.format(Locale.ENGLISH, "%.1f", item.percent) + "%", (float) (cx + (mainRadius / 1f * Math.cos(medianAngle))), (float) (cy + (mainRadius / 1f * Math.sin(medianAngle))), textPaint);
        }
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

}
