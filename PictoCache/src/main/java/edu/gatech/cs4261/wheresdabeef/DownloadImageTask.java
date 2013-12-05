package edu.gatech.cs4261.wheresdabeef;

import android.net.Uri;
import android.os.AsyncTask;

import java.util.ArrayList;

import edu.gatech.cs4261.wheresdabeef.rest.RestApiInterface;

/**
 * Created by jonathan on 11/4/13.
 */
public class DownloadImageTask extends AsyncTask<Integer,Integer,Boolean>{
    private Uri imageLocation;
    private ArrayList<String> keywords;
    private String keywordString;
    private int mAdapterPosition;
    private int mImagePosition;

    @Override
    protected Boolean doInBackground(Integer... params) {
        int imgId = params[0];
        RestApiInterface rai = new RestApiInterface();
            try {
                imageLocation = rai.getImageData(imgId);
                keywords = (ArrayList<String>) rai.getKeywordsForImage(imgId);
                keywordString = "";
                for (String keyword : keywords) {
                    keywordString = keywordString + "#" + keyword + " ";
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Single_image.setImageUri(imageLocation);
        Single_image.textView.setText(keywordString);
    }

    public void setAdapterPosition(int position){
        mAdapterPosition = position;
    }

    public void setImagePosition(int position){
        mImagePosition = position;
    }
}
