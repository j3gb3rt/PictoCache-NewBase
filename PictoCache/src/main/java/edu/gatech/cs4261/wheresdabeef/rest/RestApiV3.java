package edu.gatech.cs4261.wheresdabeef.rest;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.cs4261.wheresdabeef.AdapterHolder;
import edu.gatech.cs4261.wheresdabeef.domain.Image;

/**
 * Created by HolocronCoder.
 */
public class RestApiV3 extends AsyncTask<RestData, Void, Boolean> {
    private Context context;

    public RestApiV3(final Context c) {
        this.context = c;
    }

    private Image image;
    private int newImageId;
    private int newKeywordId;
    private Uri imageLoc;
    private List<Image> images;
    private Map<String, Integer> popularKeywords;
    private RestData.RestAction action;
    private int adapterPosition;

    @Override
    protected Boolean doInBackground(RestData... restDatas) {
        RestData data = restDatas[0];
        action = data.getAction();
        List<NameValuePair> params = data.getParams();
        Map<String, String> paramsMap = new HashMap<String, String>();
        for (int i = 0; i < params.size(); i++) {
            paramsMap.put(params.get(i).getName(), params.get(i).getValue());
        }
        adapterPosition = Integer.valueOf(paramsMap.get("ap")).intValue();
        try {
            int id;
            switch (action) {
                case GET_IMAGE:
                    id = Integer.valueOf(paramsMap.get("id")).intValue();
                    this.image = RestApiInterface.getImage(id);
                    break;
                case GET_IMAGE_DATA:
                    id = Integer.valueOf(paramsMap.get("id")).intValue();
                    this.imageLoc = RestApiInterface.getImageData(id);
                    break;
                case GET_IMAGES:
                    String sd = null, sc = null, k = null;
                    Integer l = null;
                    Double minLat = null, maxLat = null, minLon = null, maxLon = null;

                    if (paramsMap.containsKey("sd")) {
                        sd = paramsMap.get("sd");
                    }
                    if (paramsMap.containsKey("sc")) {
                        sc = paramsMap.get("sc");
                    }
                    if (paramsMap.containsKey("k")) {
                        k = paramsMap.get("k");
                    }
                    if (paramsMap.containsKey("l")) {
                        l = Integer.valueOf(paramsMap.get("l"));
                    }
                    if (paramsMap.containsKey("minLat")) {
                        minLat = Double.valueOf(paramsMap.get("minLat"));
                    }
                    if (paramsMap.containsKey("maxLat")) {
                        maxLat = Double.valueOf(paramsMap.get("maxLat"));
                    }
                    if (paramsMap.containsKey("minLon")) {
                        minLon = Double.valueOf(paramsMap.get("minLon"));
                    }
                    if (paramsMap.containsKey("maxLon")) {
                        maxLon = Double.valueOf(paramsMap.get("maxLon"));
                    }

                    this.images = RestApiInterface.getImages(sd, sc, l, k, minLat, maxLat, minLon, maxLon);
                    break;
                case GET_POPULAR_KEYWORDS:
                    Integer limit = null;

                    if (paramsMap.containsKey("l")) {
                        limit = Integer.valueOf(paramsMap.get("l"));
                    }

                    this.popularKeywords = RestApiInterface.getPopularKeywords(limit);
                    break;
                case GET_KEYWORDS_FOR_IMAGE:
                    Integer imgID = null;

                    if (paramsMap.containsKey("imgID")) {
                        imgID = Integer.valueOf(paramsMap.get("imgID"));
                    }


                    break;
                case POST_IMAGE:
                    Image i = new Image(-1);
                    i.setLongitude(Double.valueOf(paramsMap.get("lon")));
                    i.setLatitude(Double.valueOf(paramsMap.get("lat")));
                    i.setImage(data.getImage());
                    i.setThumbnail(data.getThumb());
                    this.newImageId = RestApiInterface.saveImage(i);
                    break;
                case POST_KEYWORD:
                    String kw = paramsMap.get("kw");
                    int imgId = Integer.valueOf(paramsMap.get("imgId")).intValue();
                    this.newKeywordId = RestApiInterface.saveKeyword(kw, imgId);
                    break;
            }
        } catch (final IOException e) {
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBool) {
        super.onPostExecute(aBool);
        switch (action) {
            case POST_IMAGE:
                if (aBool) {
                    Toast.makeText(context, "Upload Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
                break;
            case GET_IMAGES:
                if (aBool) {
                    AdapterHolder.getAdapter(adapterPosition).setImages(images);
                    AdapterHolder.getAdapter(adapterPosition).notifyDataSetChanged();
                }
                else {
                    Toast.makeText(context, "Failed to get list of images", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
