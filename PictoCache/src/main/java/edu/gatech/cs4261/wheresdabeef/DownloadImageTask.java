package edu.gatech.cs4261.wheresdabeef;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.GridView;

import java.io.IOException;
import java.util.ArrayList;

import edu.gatech.cs4261.wheresdabeef.domain.Image;
import edu.gatech.cs4261.wheresdabeef.rest.RestApiInterface;

/**
 * Created by jonathan on 11/4/13.
 */
public class DownloadImageTask extends AsyncTask<ArrayList<Image>,Integer,Boolean>{
    private Uri imageLocation;
    private int mAdapterPosition;

    @Override
    protected Boolean doInBackground(ArrayList<Image>... params) {
        ArrayList<Image> images = params[0];
        RestApiInterface rai = new RestApiInterface();
        for (int i = 0; i < images.size(); i++) {
            try {
                imageLocation = rai.getImageData(images.get(i).getId());
                publishProgress(i);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Uri imageLoc = imageLocation;
        int position = values[0];
        GridView view = HomeFeed.getGridView();
        ImageAdapter adapter = AdapterHolder.getAdapter(mAdapterPosition);
        adapter.setImageUri(position, imageLoc);
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
