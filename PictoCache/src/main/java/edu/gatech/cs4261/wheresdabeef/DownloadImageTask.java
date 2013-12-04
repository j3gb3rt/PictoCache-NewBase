package edu.gatech.cs4261.wheresdabeef;

import android.net.Uri;
import android.os.AsyncTask;

import java.util.ArrayList;

import edu.gatech.cs4261.wheresdabeef.domain.Image;
import edu.gatech.cs4261.wheresdabeef.rest.RestApiInterface;

/**
 * Created by jonathan on 11/4/13.
 */
public class DownloadImageTask extends AsyncTask<ArrayList<Image>,Integer,Boolean>{
    private ArrayList<Uri> imageLocations;
    private int mAdapterPosition;

    @Override
    protected Boolean doInBackground(ArrayList<Image>... params) {
        ArrayList<Image> images = params[0];
        RestApiInterface rai = new RestApiInterface();
        imageLocations = new ArrayList<Uri>();
        for (int i = 0; i < images.size(); i++) {
            try {
                imageLocations.add(rai.getThumbnailData(images.get(i).getId()));
                publishProgress(i);
            }
            catch (Exception e) {
                imageLocations.add(null);
                e.printStackTrace();
            }

        }

        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int position = values[0];
        ImageAdapter adapter = AdapterHolder.getAdapter(mAdapterPosition);
        adapter.setImageUri(position, imageLocations.get(position));
        adapter.notifyDataSetChanged();
        AdapterHolder.setAdapter(mAdapterPosition, adapter);

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

    }

    public void setAdapterPosition(int position){
        mAdapterPosition = position;
    }
}
