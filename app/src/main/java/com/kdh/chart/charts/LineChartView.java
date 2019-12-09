package com.kdh.chart.charts;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.kdh.chart.datatypes.AdvancedInputRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LineChartView extends View implements ChartView<AdvancedInputRow> {

    private Paint chartPaint = null;
    private Paint textPaint = null;

    private String chartName;
    private String timeLines;
    private String fieldNames;
    private String dataName;
    private String timeName;
    private String dataChart;
    private int[] fieldColors;
    private boolean ishaveData;
    private boolean isReady;

    float rootX;
    float rootY;
    float chartWidth;
    float spaceRow;
    boolean hadLines = false;
    boolean isOnDraw = false;
    int maxValueVertical;
    private ArrayList<Line> lines = null;
    private ArrayList<ShortLine> animLines = null;
    private ArrayList<Animator> animators;
    private AnimatorSet animSet;

/*    @Override
    public void updateData(List<AdvancedInputRow> objects) {
        //count
        int columns = objects.get(0).getValues().size();
        //
        String[] timeLines = objects.get(0).getValues().toArray(new String[columns]);
        String timeLineStr = convertArrayToStringMethod(timeLines);
        String[] fieldNameArr = new String[objects.size() - 1];
        String[] dataChartArr = new String[objects.size() - 1];
        int[] colorArr = new int[objects.size()];
        for (int i = 1; i < objects.size(); i++) {
            fieldNameArr[i-1] = objects.get(i).getLabel();
            String[] strs = objects.get(i).getValues().toArray(new String[columns]);
            String str = convertArrayToStringMethod(strs);
            dataChartArr[i-1] = str;
            colorArr[i-1] = objects.get(i).getColor();
        }
        String fieldNames = convertArrayToStringMethod(fieldNameArr);
        String dataChart = convertArrayToStringMethod(dataChartArr);

        setUpData(
                "Chart",
                timeLineStr,
                fieldNames,
                "Km",
                "Years",
                dataChart,
                colorArr
        );
        isReady = true;
    }*/

    public void updateData(List<AdvancedInputRow> objects, String chartName, String xAxisUnit, String yAxisUnit) {
        //count
        int columns = objects.get(0).getValues().size();
        //
        String[] timeLines = objects.get(0).getValues().toArray(new String[columns]);
        String timeLineStr = convertArrayToStringMethod(timeLines);
        String[] fieldNameArr = new String[objects.size() - 1];
        String[] dataChartArr = new String[objects.size() - 1];
        int[] colorArr = new int[objects.size()];
        for (int i = 1; i < objects.size(); i++) {
            fieldNameArr[i-1] = objects.get(i).getLabel();
            String[] strs = objects.get(i).getValues().toArray(new String[columns]);
            String str = convertArrayToStringMethod(strs);
            dataChartArr[i-1] = str;
            colorArr[i-1] = objects.get(i).getColor();
        }
        String fieldNames = convertArrayToStringMethod(fieldNameArr);
        String dataChart = convertArrayToStringMethod(dataChartArr);

        setUpData(
                chartName,
                timeLineStr,
                fieldNames,
                yAxisUnit,
                xAxisUnit,
                dataChart,
                colorArr
        );
        isReady = true;
        isOnDraw=false;
        this.invalidate();
    }

    private static String convertArrayToStringMethod(String[] strArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strArray.length; i++) {
            stringBuilder.append(strArray[i]);
            if (i < strArray.length - 1)
                stringBuilder.append(',');
        }
        return stringBuilder.toString();
    }


    private class Line {

        private float[] Xs;
        private float[] Ys;
        private int color;
        private int ID;

        Line(float[] Xs, float[] Ys, int color, int ID) {
            this.Xs = Xs;
            this.Ys = Ys;
            this.color = color;
            this.ID = ID;
        }

    }

    private class ShortLine {
        private float x1;
        private float y1;
        private float x2;
        private float y2;
        private int color;
        private int type;//=1: đường, =2: điểm
        private float radious;

        ShortLine(float x1, float y1, int color, int type) {
            this.x1 = x1;
            this.y1 = y1;
            this.color = color;
            this.type = type;
        }

    }

    public LineChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        lines = new ArrayList<Line>();
        animLines = new ArrayList<ShortLine>();
        initPaint();
        initAnimator();
        ishaveData = false;
    }

    private void initAnimator() {
        animators = new ArrayList<Animator>();
        animSet = new AnimatorSet();
    }

    private void initPaint() {
        //chartPaint
        chartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        chartPaint.setColor(Color.GRAY);
        chartPaint.setStyle(Paint.Style.FILL);
        chartPaint.setStrokeWidth(5);
        //textPaint
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredHeight = 600;
        int desiredWidth = desiredHeight * 3 + 200;


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

    private void drawInitialChart(Canvas canvas) {
        //draw name
        String name = "Line Chart: " + chartName;
        int nameSize = name.length() * 30;
        canvas.drawText(name, (rootX + chartWidth / 2) - nameSize / 2, getHeight() - 10, textPaint);
        //draw horizontal
        chartPaint.setColor(0xFF666666);
        chartPaint.setStrokeWidth(8);

        canvas.drawLine(rootX, rootY, rootX + chartWidth + 150, rootY, chartPaint);
        String[] times = timeLines.split(",");
        int numTimes = times.length;
        float spaceField = chartWidth / ((float) (numTimes - 1));
        for (int i = 0; i < numTimes; i++) {
            float locateX = i * spaceField + 200;
            canvas.drawLine(locateX, rootY + 10, locateX, rootY - 10, chartPaint);
            canvas.drawText(times[i], locateX - (times[i].length() * 30) / 2, rootY + 60, textPaint);
        }
        //draw vertical
        canvas.drawLine(rootX, rootY, rootX, rootY - spaceRow * 5 - 70, chartPaint);
        int spaceValue = getMaxValue(dataChart) / 5;
        //float locateY=rootY;
        for (int i = 0; i <= 5; i++) {
            float locateY = rootY - i * spaceRow;
            String valueI = (spaceValue * i) + "";
            canvas.drawText(valueI, rootX - (30 + valueI.length() * 30), locateY + 15, textPaint);
        }
        //draw arrow
        canvas.drawLine(rootX - 30, rootY - spaceRow * 5 - 30, rootX, rootY - spaceRow * 5 - 70, chartPaint);
        canvas.drawLine(rootX + 30, rootY - spaceRow * 5 - 30, rootX, rootY - spaceRow * 5 - 70, chartPaint);
        canvas.drawLine(rootX + chartWidth + 120, rootY - 30, rootX + chartWidth + 150, rootY, chartPaint);
        canvas.drawLine(rootX + chartWidth + 120, rootY + 30, rootX + chartWidth + 150, rootY, chartPaint);
        //draw unit
        canvas.drawText(dataName, rootX - (dataName.length() * 30 / 2), rootY - spaceRow * 5 - 80, textPaint);
        canvas.drawText(timeName, rootX + chartWidth + 170, rootY, textPaint);
    }

    private int getMaxValue(String values) {
        int max = 1;
        String[] valueArr = values.split(",");
        float maxF = 0;
        for (int i = 0; i < valueArr.length; i++) {
            Float tmp = Float.parseFloat(valueArr[i]);
            if (tmp > maxF) maxF = tmp;
        }
        while (max < maxF) {
            max *= 10;
        }
        return max;
    }

    private void drawBackGround(Canvas canvas) {
        float space = getHeight() / 6f;
        float x = 20;
        float startX = 200; //điểm mốc 0 là (150,getHeight-100)
        float startY = getHeight() - (space + x);
        float stopX = space * 6 + 400;
        float stopY = getHeight() - (space + x);
        chartPaint.setColor(0xFF666666);
        chartPaint.setStrokeWidth(8);
        canvas.drawLine(startX, startY, stopX, stopY, chartPaint);

        chartPaint.setStrokeWidth(5);
        float space2 = space * 4f / 5f;
        chartPaint.setColor(0xFFCCCCCC);
        for (int i = 1; i <= 5; i++) {
            startY = startY - space2;
            stopY = startY;
            canvas.drawLine(startX, startY, stopX, stopY, chartPaint);
        }
        //save
        rootX = 200;
        rootY = getHeight() - (getHeight() / 6f + 20);
        chartWidth = (getHeight() / 6f) * 6 + 200;
        spaceRow = (getHeight() / 6f) * 4f / 5f;


    }

    private void setUpData(String chartName, String timeLines, String fieldNames, String dataName, String timeName, String dataChart, int[] colors) {
        this.chartName = chartName;
        this.timeLines = timeLines;
        this.fieldNames = fieldNames;
        this.dataName = dataName;
        this.timeName = timeName;
        this.dataChart = dataChart;
        this.fieldColors = colors;
        maxValueVertical = getMaxValue(dataChart);
        ishaveData = true;


    }

    private void setDataLines() {
        Log.d("khoa", "setdata");
        hadLines = true;
        lines.clear();
        int numTime = timeLines.split(",").length;
        int numField = fieldNames.split(",").length;
        float[] xs = new float[numTime];
        for (int i = 0; i < numTime; i++) {
            xs[i] = rootX + i * (chartWidth / (numTime - 1));
        }

        String[] dataTable = dataChart.split(",");
        int count = 0;
        for (int i = 0; i < numField; i++) {
            int color = fieldColors[i];
            float[] ys = new float[numTime];
            for (int j = 0; j < numTime; j++) {
                ys[j] = Float.parseFloat(dataTable[count]);
                ys[j] = ys[j] * ((getHeight() / 6f) * 4f) / maxValueVertical;
                ys[j] = rootY - ys[j];
                count++;
            }
            Line line = new Line(xs, ys, color, i);
            lines.add(line);
        }
    }

    private int getRandomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    private void drawLine() {
        Log.d("khoa", "drawline");
        isOnDraw = true;
        animSet.cancel();
        animLines.clear();
        animators.clear();
        int color = 0;

        Random rnd = new Random();

        for (Line line : lines) {//mỗi đối tượng của biểu đồ
            //add animator
            for (int i = 1; i < line.Xs.length; i++) {
                //điểm
                final ShortLine shortLine1 = new ShortLine(line.Xs[i - 1], line.Ys[i - 1], line.color, 2);
                shortLine1.x2 = rootX + 1;
                ValueAnimator anim1 = ValueAnimator.ofFloat(0, 10);
                anim1.setDuration(300);
                anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        shortLine1.radious = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                animLines.add(shortLine1);
                animators.add(anim1);

                //viết phương trình đt
                final ShortLine shortLine2 = new ShortLine(line.Xs[i - 1], line.Ys[i - 1], line.color, 1);
                final float a = (line.Ys[i] - line.Ys[i - 1]) / (line.Xs[i] - line.Xs[i - 1]);
                final float b = line.Ys[i] - a * line.Xs[i];
                ValueAnimator anim2 = ValueAnimator.ofFloat(line.Xs[i - 1], line.Xs[i]);
                anim2.setDuration(500);
                anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        shortLine2.x2 = (float) animation.getAnimatedValue();
                        shortLine2.y2 = shortLine2.x2 * a + b;
                        invalidate();
                    }
                });

                animLines.add(shortLine2);
                animators.add(anim2);
            }
            //điểm
            final ShortLine shortLine = new ShortLine(line.Xs[line.Xs.length - 1], line.Ys[line.Xs.length - 1], line.color, 2);
            shortLine.x2 = rootX + 1;
            ValueAnimator anim = ValueAnimator.ofFloat(0, 10);
            anim.setDuration(300);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    shortLine.radious = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animLines.add(shortLine);
            animators.add(anim);

        }

        animSet.playSequentially(animators);
        animSet.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isReady) {
            drawBackGround(canvas);
            //if (!hadLines)
                setDataLines();
            if (ishaveData)
                drawInitialChart(canvas);
            if (!isOnDraw) drawLine();

            for (ShortLine shortLine : animLines) {
                if (shortLine.x1 < rootX || shortLine.x2 < rootX) break;
                chartPaint.setColor(shortLine.color);
                if (shortLine.type == 1) {
                    canvas.drawLine(shortLine.x1, shortLine.y1, shortLine.x2, shortLine.y2, chartPaint);
                } else {
                    canvas.drawCircle(shortLine.x1, shortLine.y1, shortLine.radious, chartPaint);
                }

            }
        }

    }
}
