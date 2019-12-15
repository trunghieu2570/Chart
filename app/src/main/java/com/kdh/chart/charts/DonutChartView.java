package com.kdh.chart.charts;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.kdh.chart.datatypes.AdvancedInputRow;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DonutChartView extends View implements ChartView<AdvancedInputRow> {

    public static final float DONUT_WIDTH = 100;
    private static final PorterDuffColorFilter FILTER = new PorterDuffColorFilter(Color.argb(50, 0, 0, 0), PorterDuff.Mode.SRC_OVER);
    ArrayList<Animator> animators;
    AnimatorSet animSet;
    private Paint donutPaint = null;
    private Paint textPaint = null;
    private ArrayList<ArrayList<Item>> series = null;
    private RectF mainBound;
    private float cx;
    private float cy;
    private float mainRadius;
    private RectF bound;
    private float radius;
    private Matrix matrix;

    private OnDonutItemSelectedListener onDonutItemSelectedListener;

    public DonutChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        series = new ArrayList<>();
        initPaint();
        initAnimator();
        matrix = new Matrix();

        //int[][] ints = {{4, 5, 6, 7}, {1, 2, 6, 7}, {1, 5, 6, 1}};
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
        textPaint.setTextSize(30);
        Typeface f = Typeface.create("", Typeface.BOLD);
        textPaint.setTypeface(f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void initAnimator() {
        animators = new ArrayList<>();
        animSet = new AnimatorSet();

    }

    @Override
    public void updateData(List<AdvancedInputRow> fullList) {
        final List<AdvancedInputRow> list = fullList.subList(1, fullList.size());
        int numOfSeries = fullList.get(0).getValues().size();
        int numOfItems = list.size();
        float[][] inputArray = new float[numOfSeries][];
        int[] colors = new int[numOfItems];
        for (int i = 0; i < numOfItems; i++) {
            colors[i] = list.get(i).getColor();
        }
        for (int i = 0; i < numOfSeries; i++) {
            inputArray[i] = new float[numOfItems];
            for (int j = 0; j < numOfItems; j++) {
                inputArray[i][j] = Float.parseFloat(0 + list.get(j).getValues().get(i));
            }
        }
        updateAngleTogether(inputArray, colors);
    }

    public void updateAngleTogether(@NotNull float[][] values, int[] colors) {
        animSet.cancel();
        series.clear();
        for (int i = 0; i < values.length; i++) {
            final ArrayList<Item> items = new ArrayList<>();
            animators.clear();
            series.add(items);
            int sum = 0;
            float currentAngle = 0;
            int color = 0;
            for (int j = 0; j < values[i].length; j++) {
                sum += values[i][j];
            }
            float u = 360f / sum;
            float p = 100f / sum;
            Random rnd = new Random();
            for (int j = 0; j < values[i].length; j++) {
                color = colors[j];
                final Item item = new Item(currentAngle, color);
                item.percent = values[i][j] * p;
                currentAngle += values[i][j] * u;
                //add animator
                ValueAnimator anim = ValueAnimator.ofFloat(0, currentAngle);
                //anim.setDuration(1000/(i+1));
                anim.setDuration(400 * (i + 1));
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
        //invalidate();
    }


    /*public void updateAngleNoAnimation(@NotNull float[][] values, int[] colors) {
        animSet.cancel();
        series.clear();
        for (int i = 0; i < values.length; i++) {
            final ArrayList<Item> items = new ArrayList<>();
            series.add(items);
            int sum = 0;
            float currentAngle = 0;
            int color = 0;
            for (int j = 0; j < values[i].length; j++) {
                sum += values[i][j];
            }
            float u = 360f / sum;
            float p = 100f / sum;
            Random rnd = new Random();
            for (int j = 0; j < values[i].length; j++) {
                color = colors[j];
                final Item item = new Item(currentAngle, color);
                currentAngle += values[i][j] * u;
                item.sweepAngle = currentAngle;
                item.percent = values[i][j] * p;
                items.add(item);

            }
            Collections.reverse(items);
        }
        invalidate();
    }*/

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx = (getX() + w) / 2;
        cy = (getY() + h) / 2;
        mainRadius = w < h ? w / 2f : h / 2f;
        mainRadius -= 50;
        mainBound = new RectF(cx - mainRadius, cy - mainRadius, cx + mainRadius, cy + mainRadius);
        bound = new RectF(mainBound);

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
    public boolean onTouchEvent(MotionEvent event) {
        float base_x = event.getX();
        float base_y = event.getY();
        int width = getWidth();
        int height = getHeight();
        float center_x = width / 2f;
        float center_y = height / 2f;
        float[] pts = {center_x, center_y};
        matrix.mapPoints(pts);
        float x = base_x - pts[0];
        float y = base_y - pts[1];
        float r = (float) Math.hypot(x, y);
        float angle = (float) (180 - (180 * Math.atan2(base_x - center_x, base_y - center_y) / Math.PI));
        Log.d("Debug", "r = " + r + ", arg = " + angle);
        int ring = (int) ((r - DONUT_WIDTH * 1.5) / (DONUT_WIDTH + 5));
        for (ArrayList<Item> items : series) {
            if (series.indexOf(items) == ring) {
                for (Item item : items) {
                    if (item.startAngle < angle && item.sweepAngle > angle) {
                        item.click = !item.click;
                        if (item.click)
                            onDonutItemSelectedListener.onSelected(series.indexOf(items), items.size() - items.indexOf(item) - 1);
                        else
                            onDonutItemSelectedListener.onUnselected();

                    } else {
                        item.click = false;
                    }
                }
            } else {
                if (ring >= series.size())
                    onDonutItemSelectedListener.onUnselected();
                for (Item item : items) {
                    item.click = false;
                }
            }
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        radius = DONUT_WIDTH * 2;
        bound.set(cx - radius, cy - radius, cx + radius, cy + radius);
        for (ArrayList<Item> items : series) {
            for (Item item : items) {
                donutPaint.setColor(Color.WHITE);
                donutPaint.setStyle(Paint.Style.STROKE);
                donutPaint.setStrokeCap(Paint.Cap.BUTT);
                donutPaint.setStrokeWidth(DONUT_WIDTH);
                canvas.drawArc(bound, item.sweepAngle - 90, 1, false, donutPaint);
                donutPaint.setColor(item.color);
                donutPaint.setStyle(Paint.Style.STROKE);
                donutPaint.setStrokeCap(Paint.Cap.BUTT);
                donutPaint.setStrokeWidth(DONUT_WIDTH);
                canvas.drawArc(bound, -90 + 1, item.sweepAngle, false, donutPaint);
                if (item.click) {
                    donutPaint.setColorFilter(FILTER);
                    donutPaint.setStyle(Paint.Style.STROKE);
                    donutPaint.setStrokeCap(Paint.Cap.BUTT);
                    donutPaint.setStrokeWidth(DONUT_WIDTH);
                    canvas.drawArc(bound, -90 + item.startAngle, item.sweepAngle - item.startAngle, false, donutPaint);
                    donutPaint.setColorFilter(null);
                }
                donutPaint.setColor(Color.WHITE);
                donutPaint.setStyle(Paint.Style.STROKE);
                donutPaint.setStrokeCap(Paint.Cap.BUTT);
                donutPaint.setStrokeWidth(DONUT_WIDTH);
                canvas.drawArc(bound, item.sweepAngle - 90, 1, false, donutPaint);
                //donutPaint.setColor(Color.WHITE);
                //donutPaint.setStyle(Paint.Style.STROKE);
                //canvas.drawArc(mainBound, -90, item.sweepAngle, true, donutPaint);
                float medianAngle = (item.startAngle - 90 + ((item.sweepAngle - item.startAngle) / 2f)) * (float) Math.PI / 180f;
                canvas.drawText(String.format(Locale.ENGLISH, "%.1f", item.percent) + "%", (float) (cx + (radius / 1f * Math.cos(medianAngle))), (float) (cy + (radius / 1f * Math.sin(medianAngle))), textPaint);
            }
            radius += donutPaint.getStrokeWidth() + 5f;
            bound.set(cx - radius, cy - radius, cx + radius, cy + radius);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredHeight = 1500;
        int desiredWidth = 1500;


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

    public void setOnDonutItemSelectedListener(OnDonutItemSelectedListener onDonutItemSelectedListener) {
        this.onDonutItemSelectedListener = onDonutItemSelectedListener;
    }

    public interface OnDonutItemSelectedListener {
        void onSelected(int ringId, int itemId);

        void onUnselected();
    }

    public class Item {

        private float startAngle;
        private float sweepAngle = 0;
        private int color;
        private float percent;
        private boolean click = false;

        Item(float startAngle, int color) {
            this.startAngle = startAngle;
            this.color = color;
        }
    }


}
