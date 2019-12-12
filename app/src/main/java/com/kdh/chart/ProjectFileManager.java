package com.kdh.chart;

import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kdh.chart.datatypes.Chart;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.DonutChart;
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

public class ProjectFileManager {

    public static final String DEFAULT_FOLDER = "ChartProjects";

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

    public static ProjectLocation loadProject(final ProjectLocation projectLocation) {
        File saveFile = new File(projectLocation.getLocation());
        saveFile.getParentFile().mkdirs();
        Gson gson = new Gson();
        Project result = null;
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(projectLocation.getLocation()));
            result = gson.fromJson(reader, Project.class);
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
}
