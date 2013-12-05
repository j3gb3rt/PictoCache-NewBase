package edu.gatech.cs4261.wheresdabeef;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs4261.wheresdabeef.domain.Image;
import edu.gatech.cs4261.wheresdabeef.location.LocationApi;
import edu.gatech.cs4261.wheresdabeef.rest.RestApiInterface;
import edu.gatech.cs4261.wheresdabeef.rest.RestApiV3;
import edu.gatech.cs4261.wheresdabeef.rest.RestData;

/**
 * Created by Jonathan on 10/10/13.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Image> mImages;
    private int mPosition;
    public ImageAdapter(Context c, int position, String keyword) {
        mContext = c;
        mImages = new ArrayList<Image>();
        mPosition = position;
        if ((position > 0) && (position < 4)) {
            if (keyword.equals(NavigationDrawerFragment.PREDEFINED_SECTION_NEARBY)) {
                LocationApi.startPollingLocation(mContext);
                RestApiV3 task = new RestApiV3(mContext);
                RestData data = new RestData();
                data.setAction(RestData.RestAction.GET_IMAGES);
                data.addParam(new BasicNameValuePair("ap", String.valueOf(mPosition)));
                data.addParam(new BasicNameValuePair("sd", RestApiInterface.SORT_DESC));
                data.addParam(new BasicNameValuePair("sc", RestApiInterface.SORT_IMG_ID));
                data.addParam(new BasicNameValuePair("l", String.valueOf(8)));
                Location location = LocationApi.stopPollingLocation();
                data.addParam(new BasicNameValuePair("minLat",
                              String.valueOf(location.getLatitude() - 0.0005)));
                data.addParam(new BasicNameValuePair("maxLat",
                              String.valueOf(location.getLatitude() + 0.0005)));
                data.addParam(new BasicNameValuePair("minLon",
                              String.valueOf(location.getLongitude() - 0.0005)));
                data.addParam(new BasicNameValuePair("maxLon",
                              String.valueOf(location.getLongitude() + 0.0005)));
                task.execute(data);
            }
            if (keyword.equals(NavigationDrawerFragment.PREDEFINED_SECTION_POPULAR)) {
                RestApiV3 task = new RestApiV3(mContext);
                RestData data = new RestData();
                data.setAction(RestData.RestAction.GET_POPULAR_KEYWORDS);
                data.addParam(new BasicNameValuePair("ap", String.valueOf(mPosition)));
                data.addParam(new BasicNameValuePair("sd", RestApiInterface.SORT_DESC));
                data.addParam(new BasicNameValuePair("sc", RestApiInterface.SORT_IMG_ID));
                data.addParam(new BasicNameValuePair("l", String.valueOf(8)));
                task.execute(data);
            }
            if (keyword.equals(NavigationDrawerFragment.PREDEFINED_SECTION_NEW)) {
                RestApiV3 task = new RestApiV3(mContext);
                RestData data = new RestData();
                data.setAction(RestData.RestAction.GET_IMAGES);
                data.addParam(new BasicNameValuePair("ap", String.valueOf(mPosition)));
                data.addParam(new BasicNameValuePair("sd", RestApiInterface.SORT_DESC));
                data.addParam(new BasicNameValuePair("sc", RestApiInterface.SORT_IMG_ID));
                data.addParam(new BasicNameValuePair("l", String.valueOf(8)));
                task.execute(data);
            }
        }
        else {
            RestApiV3 task = new RestApiV3(mContext);
            RestData data = new RestData();
            data.setAction(RestData.RestAction.GET_IMAGES);
            data.addParam(new BasicNameValuePair("ap", String.valueOf(mPosition)));
            data.addParam(new BasicNameValuePair("k", keyword));
            task.execute(data);
        }
    }

    public ImageAdapter(Context c) {
        mContext = c;
        mImages = new ArrayList<Image>();
    }

    public void setImages (List<Image> images){
        mImages = (ArrayList)images;
        DownloadImageTask imageTask = new DownloadImageTask();
        imageTask.setAdapterPosition(mPosition);
        imageTask.execute(mImages);
    }

    public void setImage (int position,Uri imageLocation) {
        Image workingImage = mImages.get(position);
        workingImage.setImage(imageLocation);
        mImages.set(position, workingImage);
    }

    public void addImage (Image image) {
        mImages.add(image);
    }

    public int getCount() {
        return mImages.size();
    }

    public Uri getImageUri(int position) {
        return mImages.get(position).getImage();
    }


    public int getImageId(int position) {
//        return mThumbIds[images.get(position)];
        return mImages.get(position).getId();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public void setImageUri(int position, Uri imageLocation) {
        Image temp = mImages.get(position);
        temp.setImage(imageLocation);
        mImages.set(position, temp);

    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmap(Uri imageLocation, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageLocation.getPath(), options);
            //ExifInterface exif = null;
            //try {
            //exif = new ExifInterface(imageLocation.getPath());
            //} catch (IOException e) {
            //e.printStackTrace();
            //}
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(imageLocation.getPath(), options);
        }
        catch(NullPointerException npe) {
            Log.d("PictoCache", "Image is null");
            return null;
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CustomRelativeLayout rootView = (CustomRelativeLayout) inflater.inflate(R.layout.picture_loading, parent, false);

        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        SquareImageView imageView = (SquareImageView) rootView.findViewById(R.id.imageView);

        //if (convertView == null) { // if it's not recycled, initialize some attributes
                //imageView = new SquareImageView(mContext);
                //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //imageView.setPadding(4, 4, 4, 4);
        //}
        //else {
                //imageView = (SquareImageView) convertView;
        //}
        if(mImages.get(position).getImage() != null) {
            progressBar.setVisibility(View.GONE);
            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4, 4, 4, 4);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(decodeSampledBitmap(mImages.get(position).getImage(), 100, 100));
            //imageView.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(),mThumbIds[images.get(position)], 100, 100));
        }
        return rootView;
    }

    // references to our images
//    private Integer[] mThumbIds = {
//            R.drawable.i1, R.drawable.i2,
//            R.drawable.i3, R.drawable.i4,
//            R.drawable.i5, R.drawable.i6,
//            R.drawable.i7, R.drawable.i8,
//            R.drawable.i9, R.drawable.i10,
//            R.drawable.i11, R.drawable.i12
//    };

}