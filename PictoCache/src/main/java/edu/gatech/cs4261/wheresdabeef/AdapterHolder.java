package edu.gatech.cs4261.wheresdabeef;

import android.content.Context;

import java.util.ArrayList;

import edu.gatech.cs4261.wheresdabeef.domain.Image;
import edu.gatech.cs4261.wheresdabeef.rest.RestApiV3;
import edu.gatech.cs4261.wheresdabeef.rest.RestData;

/**
 * Created by jonathan on 11/19/13.
 */
public class AdapterHolder {
    static ArrayList<ImageAdapter> adapters;
    static ImageAdapter searchAdapter;
    private static RestApiV3 imageListTask;
    private static DownloadImagesTask imageDataTask;

    public static ImageAdapter getOrInitAdapter(Context context, int position, String keyword, int numberOfSections) {
        if (position == -1) {
            searchAdapter = new ImageAdapter(context, position, keyword);
            return searchAdapter;
        } else {
            if (adapters == null) {
                adapters = new ArrayList<ImageAdapter>(numberOfSections);
                for (int i = 0; i < numberOfSections; i++) {
                    adapters.add(null);
                }
            }
            if (adapters.get(position) == null) {
                adapters.set(position, new ImageAdapter(context, position, keyword));
            }
            return adapters.get(position);
        }
    }

    public static ImageAdapter getAdapter(int position) {
        if (position == -1) {
            return searchAdapter;
        } else {
            return adapters.get(position);
        }
    }

    public static void setAdapter(int position, ImageAdapter adapter) {
        if (position == -1) {
            searchAdapter = adapter;
        } else {
            adapters.set(position, adapter);
        }
    }

    public static void stopTask() {
        if (imageListTask != null) {
            imageListTask.cancel(true);
        }
    }

    public static void setTask(RestApiV3 task) {
        imageListTask = task;
    }

    public static void executeTask(RestData data) {
        imageListTask.execute(data);
    }

    public static void stopImageTask() {
        if (imageDataTask != null) {
            imageDataTask.cancel(true);
        }
    }

    public static void setImageTask(DownloadImagesTask imageTask, int position) {
        imageDataTask = imageTask;
        imageDataTask.setAdapterPosition(position);
    }

    public static void executeImageTask(ArrayList<Image> images) {
        imageDataTask.execute(images);
    }
}
