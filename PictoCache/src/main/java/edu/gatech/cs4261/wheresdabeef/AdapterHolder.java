package edu.gatech.cs4261.wheresdabeef;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by jonathan on 11/19/13.
 */
public class AdapterHolder {
    static ArrayList<ImageAdapter> adapters;

    public static ImageAdapter getOrInitAdapter(Context context, int position, String keyword, int numberOfSections) {
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

    public static ImageAdapter getAdapter(int position) {
        return adapters.get(position);
    }

    public static void setAdapter(int position, ImageAdapter adapter) {
        adapters.set(position, adapter);
    }

}
