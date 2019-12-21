package com.kdh.chart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kdh.chart.datatypes.Chart;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.ColumnBarChart;
import com.kdh.chart.datatypes.DonutChart;
import com.kdh.chart.datatypes.GroupBarChart;
import com.kdh.chart.datatypes.LineChart;
import com.kdh.chart.datatypes.PieChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ProjectFileManager {

    public static final String DEFAULT_FOLDER = "ChartProjects";

    public static void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }


    public static File makeDefaultProjectPath(Project project) {
        File sdcard = Environment.getExternalStorageDirectory();
        File mainDir = new File(sdcard, DEFAULT_FOLDER);
        File projectDir = new File(mainDir, project.getName().replace(' ', '_'));
        return new File(projectDir, project.getName().replace(' ', '_') + ".chprj");
    }

    public static void saveProject(ProjectLocation location) {
        File saveFile = new File(location.getLocation());
        saveFile.getParentFile().mkdirs();
        Gson gson = new Gson();
        String content = gson.toJson(location.getProject());
        FileOutputStream fileOutputStream;
        try {
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(saveFile);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteProject(ProjectLocation location) {
        File file = new File(location.getLocation());
        if (!(file).exists()) return;
        try {
            deleteRecursive(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ProjectLocation loadProject(final ProjectLocation projectLocation) {
        File saveFile = new File(projectLocation.getLocation());
        saveFile.getParentFile().mkdirs();
        Gson gson = new Gson();
        Project result = null;
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(projectLocation.getLocation()));
            result = gson.fromJson(reader, Project.class);
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (result == null) return null;
        else
            return new ProjectLocation(projectLocation.getLocation(), result);
    }

    public static ArrayList<ProjectLocation> loadProjects() {
        File sdcard = Environment.getExternalStorageDirectory();
        File mainDir = new File(sdcard, DEFAULT_FOLDER);
        Log.d("PRINT", mainDir.toString());
        if (!mainDir.exists()) {
            mainDir.mkdir();
            return null;
        }
        final ArrayList<ProjectLocation> result = new ArrayList<>();
        final Gson gson = new Gson();
        File[] paths = mainDir.listFiles();
        for (File path : paths) {
            if (path.isDirectory()) {
                File[] projectFiles = path.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        return s.endsWith(".chprj");
                    }
                });
                for (File projectFile : projectFiles) {
                    try {
                        final BufferedReader reader = new BufferedReader(new FileReader(projectFile));
                        final Project tmp = gson.fromJson(reader, Project.class);
                        reader.close();
                        final ProjectLocation location = new ProjectLocation(projectFile.toString(), tmp);
                        result.add(location);
                    } catch (JsonSyntaxException jse) {
                        jse.printStackTrace();
                    } catch (FileNotFoundException fnf) {
                        fnf.printStackTrace();
                    } catch (IOException io) {
                        io.printStackTrace();
                    }

                }
            }
        }
        return result;
    }

    public static ArrayList<Pair<ChartLocation, Chart>> loadCharts(final ProjectLocation projectLocation) {
        final ArrayList<Pair<ChartLocation, Chart>> result = new ArrayList<>();
        final Gson gson = new Gson();
        Project project;
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(projectLocation.getLocation()));
            project = gson.fromJson(reader, Project.class);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
        File projectFolder = new File(projectLocation.getLocation()).getParentFile();
        for (ChartLocation chartLocation : project.getCharts()) {
            final File chartFile = new File(projectFolder, chartLocation.getLocation());
            if (chartFile.exists()) {
                try {
                    final BufferedReader reader = new BufferedReader(new FileReader(chartFile));
                    switch (chartLocation.getType()) {
                        case PIE:
                            final PieChart pieChart = gson.fromJson(reader, PieChart.class);
                            result.add(new Pair<ChartLocation, Chart>(chartLocation, pieChart));
                            break;
                        case DONUT:
                            final DonutChart donutChart = gson.fromJson(reader, DonutChart.class);
                            result.add(new Pair<ChartLocation, Chart>(chartLocation, donutChart));
                            break;
                        case LINE:
                            final LineChart lineChart = gson.fromJson(reader, LineChart.class);
                            result.add(new Pair<ChartLocation, Chart>(chartLocation, lineChart));
                            break;
                        case GROUPED:
                            final GroupBarChart groupChart=gson.fromJson(reader,GroupBarChart.class);
                            result.add(new Pair<ChartLocation, Chart>(chartLocation,groupChart));
                            break;
                        case COLUMN:
                            final ColumnBarChart columnChart=gson.fromJson(reader,ColumnBarChart.class);
                            result.add(new Pair<ChartLocation, Chart>(chartLocation,columnChart));
                        default:
                            break;
                    }
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void saveChart(final ProjectLocation projectLocation, final Chart chart, final ChartLocation chartLocation) {

        File projectFolder = new File(projectLocation.getLocation()).getParentFile();
        File file = new File(projectFolder, chartLocation.getLocation());
        Gson gson = new Gson();
        String content = gson.toJson(chart);
        FileOutputStream fileOutputStream;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static File saveImage(Context context, View view1, View view2) {
        File sdcard = Environment.getExternalStorageDirectory();
        File mainDir = new File(sdcard, DEFAULT_FOLDER);
        String filename = UUID.randomUUID().toString().replace('-', '_');
        File sFile = new File(mainDir, filename.concat(".png"));
        //Bitmap bmp = createBitmapFromView(context, view);
        Bitmap bmp1 = getBitmapFromView(view1);
        Bitmap bmp2 = getBitmapFromView(view2);
        Bitmap bmp = merge2Bitmap(bmp1, bmp2);
        try (FileOutputStream out = new FileOutputStream(sFile)) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sFile;
    }

    private static Bitmap merge2Bitmap(Bitmap bitmap1, Bitmap bitmap2) {
        if (bitmap1 == null) return bitmap2;
        if (bitmap2 == null) return bitmap1;
        int maxWidth = bitmap2.getWidth() > bitmap1.getWidth() ? bitmap2.getWidth() : bitmap1.getWidth();
        int maxHeight = bitmap1.getHeight() + bitmap2.getHeight();
        Bitmap returnedBitmap = Bitmap.createBitmap(maxWidth, maxHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE);
        if (bitmap1.getWidth() > bitmap2.getWidth()) {
            int d = (bitmap1.getWidth() - bitmap2.getWidth()) / 2;
            canvas.drawBitmap(bitmap1, 0f, 0f, null);
            canvas.drawBitmap(bitmap2, d, bitmap1.getHeight(), null);
        } else if (bitmap1.getWidth() < bitmap2.getWidth()) {
            int d = (bitmap2.getWidth() - bitmap1.getWidth()) / 2;
            canvas.drawBitmap(bitmap1, d, 0f, null);
            canvas.drawBitmap(bitmap2, 0f, bitmap1.getHeight(), null);
        } else {
            canvas.drawBitmap(bitmap1, 0f, 0f, null);
            canvas.drawBitmap(bitmap2, 0f, bitmap1.getHeight(), null);
        }

        return returnedBitmap;
    }

    private static Bitmap getBitmapFromView(View view) {
        if (view == null) return null;
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    private static Bitmap createBitmapFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
