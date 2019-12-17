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

import com.kdh.chart.datatypes.SimpleInputRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PieChartView extends View implements ChartView<SimpleInputRow> {
    private static final PorterDuffColorFilter FILTER = new PorterDuffColorFilter(Color.argb(50, 0, 0, 0), PorterDuff.Mode.SRC_OVER);
    ArrayList<Animator> animators;
    AnimatorSet animSet;
    private Paint piePaint = null;
    private Paint textPaint = null;
    private ArrayList<Item> items;
    private RectF bound;
    private float cx;
    private float cy;
    private int radius;
    private Matrix matrix;
    private OnPieItemSelectedListener onPieItemSelectedListener;
    public PieChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        items = new ArrayList<>();
        initPaint();
        initAnimator();
        matrix = new Matrix();
    }

    public void setOnPieItemSelectedListener(OnPieItemSelectedListener onPieItemSelectedListener) {
        this.onPieItemSelectedListener = onPieItemSelectedListener;
    }

    private void initPaint() {
        //piePaint
        piePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        piePaint.setColor(Color.RED);
        piePaint.setStyle(Paint.Style.FILL);
        piePaint.setStrokeWidth(5);
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

/*    public void updateAngleNoAnimation(int[] values) {
        animSet.cancel();
        items.clear();
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
            item.sweepAngle = currentAngle;
            item.percent = value * p;
            items.add(item);

        }
        Collections.reverse(items);
        invalidate();
    }*/
    //@Override
    public void updateData(List<SimpleInputRow> fullList) {
        final List<SimpleInputRow> list = fullList.subList(1,fullList.size());
        animSet.cancel();
        items.clear();
        animators.clear();
        float sum = 0;
        float currentAngle = 0;
        int color;
        for (SimpleInputRow row : list) {
            sum += Float.parseFloat(0 + row.getValue());
        }
        float u = 360f / sum;
        float p = 100f / sum;
        for (SimpleInputRow row : list) {
            color = row.getColor();
            float value = Float.parseFloat(0 + row.getValue());
            final Item item = new Item(currentAngle, color);
            item.percent = value * p;
            currentAngle += value * u;
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




/*    public void updateAngle(int[] values, AnimType animType) {
        switch (animType) {
            case TOGETHER:
                updateData(values);
                break;
            case SEQUENTIALLY:
                updateAngleSequentially(values);
                break;
            default:
                updateAngleNoAnimation(values);
                break;
        }
    }*/

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx = (getX() + w) / 2;
        cy = (getY() + h) / 2;
        radius = w < h ? w / 2 : h / 2;
        radius -= 50;
        bound = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
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
        Log.d("Debug", "arg = " + angle);
        for (Item item : items) {
            if (item.startAngle < angle && item.sweepAngle > angle) {
                item.click = !item.click;
                if (item.click)
                    onPieItemSelectedListener.onSelected(items.size() - items.indexOf(item) - 1);
                else
                    onPieItemSelectedListener.onUnselected();

            } else {
                item.click = false;
            }
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Item item : items) {
            piePaint.setColor(item.color);
            piePaint.setStyle(Paint.Style.FILL);
            canvas.drawArc(bound, -90, item.sweepAngle, true, piePaint);


            if (item.click) {
                piePaint.setColorFilter(FILTER);
                canvas.drawArc(bound, -90 + item.startAngle, item.sweepAngle - item.startAngle, true, piePaint);
                piePaint.setColorFilter(null);
            }


            piePaint.setColor(Color.WHITE);
            piePaint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(bound, -90, item.sweepAngle, true, piePaint);
            float medianAngle = (item.startAngle - 90 + ((item.sweepAngle - item.startAngle) / 2f)) * (float) Math.PI / 180f;
            canvas.drawText(String.format(Locale.ENGLISH, "%.1f", item.percent) + "%", (float) (cx + (radius / 1.5f * Math.cos(medianAngle))), (float) (cy + (radius / 1.5f * Math.sin(medianAngle))), textPaint);

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

    public interface OnPieItemSelectedListener {
        void onSelected(int itemId);

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
