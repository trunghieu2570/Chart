package com.kdh.chart;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.kdh.chart.datatypes.Chart;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.Project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProjectFileManager {

    public static final String DEFAULT_FOLDER = "ChartProjects";

    public static void saveProject(final Project project) {
        File sdcard = Environment.getExternalStorageDirectory();
        File mainDir = new File(sdcard, DEFAULT_FOLDER);
        Log.d("PRINT", mainDir.toString());
        if (!mainDir.exists()) {
            mainDir.mkdir();
        }
        File projectDir = new File(mainDir, project.getName().replace(' ', '_'));
        if (!projectDir.exists()) {
            projectDir.mkdir();
        }
        File file = new File(projectDir, project.getName().replace(' ', '_') + ".chprj");
        Gson gson = new Gson();
        String content = gson.toJson(project);
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

    public static void saveChart(final Project project, final Chart chart, final ChartLocation location) {
        File sdcard = Environment.getExternalStorageDirectory();
        File mainDir = new File(sdcard, DEFAULT_FOLDER);
        Log.d("PRINT", mainDir.toString());
        if (!mainDir.exists()) {
            mainDir.mkdir();
        }
        File projectDir = new File(mainDir, project.getName().replace(' ', '_'));
        if (!projectDir.exists()) {
            projectDir.mkdir();
        }
        File file = new File(projectDir, location.getLocation());
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
}
