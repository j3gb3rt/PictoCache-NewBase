package edu.gatech.cs4261.wheresdabeef.rest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.cs4261.wheresdabeef.domain.Image;

/**
 * Created by Kyle.
 */
public class RestApiInterface {
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";

    public static final String SORT_IMG_ID = "id";
    public static final String SORT_IMG_LAT = "latitude";
    public static final String SORT_IMG_LON = "longitude";

    public static final String SORT_KW_ID = "id";
    public static final String SORT_KW_IMAGE = "image";

    private static final String BASE_IMG_URL =
            "http://dev.m.gatech.edu/d/gtg310x/api/imagepail/image";
    public static final String BASE_KW_URL =
            "http://dev.m.gatech.edu/d/gtg310x/api/imagepail/keyword";

    public static Uri getThumbnailData(final int id) throws IOException {
        String url = BASE_IMG_URL + "/" + id;

        byte[] image = RestApi.getImage(url, "thumbnail");
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        File f = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
                        + File.separator + "img_" + ((new Date()).getTime() + ".png"));
        bmp.compress(Bitmap.CompressFormat.PNG, 50, new FileOutputStream(f));
        return Uri.fromFile(f);
    }



    public static Uri getImageData(final int id) throws IOException {
        String url = BASE_IMG_URL + "/" + id;

        byte[] image = RestApi.getImage(url, "image");
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        File f = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
                        + File.separator + "img_" + ((new Date()).getTime() + ".png"));
        bmp.compress(Bitmap.CompressFormat.PNG, 50, new FileOutputStream(f));
        return Uri.fromFile(f);
    }

    public static Image getImage(final int id) throws IOException {
        String url = BASE_IMG_URL + "/" + id;
        JSONArray jsonArr = RestApi.get(url, new HashMap<String, String>());

        Image image;
        try {
            JSONObject json = jsonArr.getJSONObject(0);

            image = new Image(json.getInt("id"));
            image.setLatitude(json.getDouble("latitude"));
            image.setLongitude(json.getDouble("longitude"));
        } catch (final JSONException e) {
            return null; // swallow error
        }

        return image;
    }

    /**
     * Retrieves a list of images based upon supplied parameters
     *
     * @param sortDir The initial sort direction of the images
     * @param sortCol The initial column to sort
     * @param limit The max number of records to return        data.setImage(mImage.getImage());
        data.setThumb(mImage.getThumbnail());

     * @param keyword A keyword to search on. Exclusive with Lat&Lon
     * @param minLat A minimum latitude to search on, must include the other 3
     * @param maxLat A maximum latitude to search on, must include the other 3
     * @param minLon A minimum longitude to search on, must include the other 3
     * @param maxLon A maximum longitude to search on, must include the other 3
     * @return An ArrayList of Image objects, potentially empty.
     */
    public static List<Image> getImages(final String sortDir,
                                 final String sortCol,
                                 final Integer limit,
                                 final String keyword,
                                 final Double minLat,
                                 final Double maxLat,
                                 final Double minLon,
                                 final Double maxLon) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("sd", String.valueOf(sortDir));
        params.put("sc", String.valueOf(sortCol));
        params.put("l", String.valueOf(limit));
        params.put("k", String.valueOf(keyword));
        params.put("minLat", String.valueOf(minLat));
        params.put("maxLat", String.valueOf(maxLat));
        params.put("minLon", String.valueOf(minLon));
        params.put("maxLon", String.valueOf(maxLon));
        JSONArray jsonArr = RestApi.get(BASE_IMG_URL, params);

        List<Image> images = new ArrayList<Image>();

        try {
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject json = jsonArr.getJSONObject(i);

                Image image = new Image(json.getInt("id"));
                image.setLatitude(json.getDouble("latitude"));
                image.setLongitude(json.getDouble("longitude"));

                images.add(image);
            }
        } catch (final JSONException e) {
            return null; // swallow error
        }

        return images;
    }

    public static int saveImage(final Image i) throws IOException {
        JSONObject json = RestApi.postImage(BASE_IMG_URL + "/", i);

        int id;
        try {
            id = json.getInt("id");
        } catch (final JSONException e) {
            return -1; // swallow error
        }

        return id;
    }

    public static int saveKeyword(final String kw, final int imgId) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("k", kw);
        params.put("imgId", String.valueOf(imgId));

        JSONObject json = RestApi.post(BASE_KW_URL + "/", params);

        int id;
        try {
            id = json.getInt("id");
        } catch (final JSONException e) {
            return -1; // swallow error
        }

        return id;
    }

    public static Map<String, Integer> getPopularKeywords(final Integer limit) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("l", String.valueOf(limit));

        JSONArray jsonArr = RestApi.get(BASE_KW_URL, params);

        Map<String, Integer> keywords = new HashMap<String, Integer>();

        try {
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject json = jsonArr.getJSONObject(i);
                keywords.put(json.getString("keyword"), json.getInt("total"));
            }
        } catch (final JSONException e) {
            return null; // swallow error
        }

        return keywords;
    }
}
