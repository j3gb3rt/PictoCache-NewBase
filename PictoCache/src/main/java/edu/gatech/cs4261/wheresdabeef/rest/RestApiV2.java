package edu.gatech.cs4261.wheresdabeef.rest;//package edu.gatech.cs4261.wheresdabeef.rest;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.widget.Toast;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.URLEncodedUtils;
//import org.apache.http.entity.BufferedHttpEntity;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import edu.gatech.cs4261.wheresdabeef.domain.Image;
//
///**
// * Created by Kyle M.
// */
//public class RestApiV2 extends AsyncTask<RestDataCapsule, Void, Boolean> {
//    private static final String BASE_IMG_URL =
//            "http://dev.m.gatech.edu/d/gtg310x/api/imagepail/image";
//    public static final String BASE_KW_URL =
//            "http://dev.m.gatech.edu/d/gtg310x/api/imagepail/keyword";
//
//    private enum HttpAction {
//        GET,
//        POST;
//    }
//
//    public enum RestAction {
//        /**
//         * Expected Parameters:
//         *      id
//         */
//        GET_IMAGE(HttpAction.GET),
//        /**
//         * Optional Parameters:
//         *      l (how many images, max)
//         *
//         *      - AND -
//         *
//         *      minLat (minimum latitude)
//         *      maxLat (maximum latitude)
//         *      minLon (minimum longitude)
//         *      maxLon (maximum longitude)
//         *
//         *      - OR -
//         *
//         *      k (keyword)
//         */
//        GET_IMAGES(HttpAction.GET),
//        /**
//         * Expected Parameters:
//         *      id
//         *      imgType ("thumbnail" or "image")
//         */
//        GET_IMAGE_DATA(HttpAction.GET),
//        /**
//         * Optional Parameters:
//         *      l (how many images, max)
//         */
//        GET_POPULAR_KEYWORDS(HttpAction.GET),
//        /**
//         * Expected Parameters:
//         *      lat (latitude)
//         *      lon (longitude)
//         *
//         * Expected Set Values
//         *      image (Uri)
//         *      thumb (Uri)
//         */
//        POST_IMAGE(HttpAction.POST),
//        /**
//         * Expected Parameters:
//         *      kw (Keyword object)
//         *      imgId (ID of associated image)
//         */
//        POST_KEYWORD(HttpAction.POST);
//
//        private HttpAction http;
//
//        private RestAction(HttpAction http) {
//            this.http = http;
//        }
//
//        public HttpAction getHttp() {
//            return http;
//        }
//    }
//
//    private Image image;
//    private List<Image> images;
//    private Map<String, Integer> popularKeywords;
//    private Integer imageId;
//    private Integer keywordId;
//    private Bitmap imageData;
//    private Bitmap thumbnailData;
//    private Context context;
//
//    public RestApiV2(Context c) {
//        context = c;
//    }
//
//    public Image getReturnedImage() {
//        return this.image;
//    }
//
//    public List<Image> getReturnedImages() {
//        return this.images;
//    }
//
//    public Map<String, Integer> getReturnedPopularKeywords() {
//        return this.popularKeywords;
//    }
//
//    public Integer getReturnedImageId() {
//        return this.imageId;
//    }
//
//    public Integer getReturnedKeywordId() {
//        return this.keywordId;
//    }
//
//    public Bitmap getReturnedImageData() {
//        return this.imageData;
//    }
//
//    public Bitmap getReturnedThumbnailData() {
//        return this.thumbnailData;
//    }
//
//    @Override
//    protected Boolean doInBackground(RestDataCapsule... dataObjs) {
//        RestDataCapsule data = dataObjs[0];
//        RestAction action = data.getAction();
//        List<NameValuePair> params = data.getParams();
//
//        Map<String, NameValuePair> paramsMap = new HashMap<String, NameValuePair>();
//        for (int i = 0; i < params.size(); i++) {
//            paramsMap.put(params.get(i).getName(), params.get(i));
//        }
//
//        String url;
//        int id;
//        switch (action) {
//            case GET_IMAGE:
//                id = Integer.parseInt(paramsMap.get("id").getValue());
//                paramsMap.remove("id");
//                url = BASE_IMG_URL + "/" + id;
//                break;
//            case GET_IMAGES:
//                url = BASE_IMG_URL;
//                break;
//            case GET_IMAGE_DATA:
//                id = Integer.parseInt(paramsMap.get("id").getValue());
//                paramsMap.remove("id");
//                url = BASE_IMG_URL + "/" + id;
//                break;
//            case GET_POPULAR_KEYWORDS:
//                url = BASE_KW_URL;
//                break;
//            case POST_IMAGE:
//                url = BASE_IMG_URL + "/";
//
//                break;
//            case POST_KEYWORD:
//                url = BASE_KW_URL + "/";
//                break;
//            default:
//                return false;
//        }
//
//        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//        paramsList.addAll(paramsMap.values());
//
//        InputStream is;
//        try {
//            if (action.getHttp() == HttpAction.GET) {
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                String paramString = URLEncodedUtils.format(paramsList, "utf-8");
//                url += "?" + paramString;
//                HttpGet get = new HttpGet(url);
//
//                HttpResponse response = httpClient.execute(get);
//                HttpEntity entity = response.getEntity();
//                entity = new BufferedHttpEntity(entity);
//                is = entity.getContent();
//            } else if (action.getHttp() == HttpAction.POST) {
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpPost post = new HttpPost(url);
//
//                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//
//                switch (action) {
//                    case POST_IMAGE:
//                        for (int i = 0; i < paramsList.size(); i++) {
//                            NameValuePair nvp = paramsList.get(i);
//                            builder.addTextBody(nvp.getName(), nvp.getValue());
//                        }
//
//                        Uri imgUri = data.getImage();
//                        builder.addBinaryBody("img", new File(imgUri.getPath()));
//                        Uri tnUri = data.getThumb();
//                        builder.addBinaryBody("tn", new File(tnUri.getPath()));
//
//                        break;
//                    case POST_KEYWORD:
//                        for (int i = 0; i < paramsList.size(); i++) {
//                            NameValuePair nvp = paramsList.get(i);
//                            builder.addTextBody(nvp.getName(), nvp.getValue());
//                        }
//                        break;
//                    default:
//                        return false;
//                }
//
//                post.setEntity(builder.build());
//                HttpResponse response = httpClient.execute(post);
//                HttpEntity entity = response.getEntity();
//                is = entity.getContent();
//            } else {
//                return false;
//            }
//
//            JSONArray jsonArr;
//            JSONObject json;
//            switch (action) {
//                case GET_IMAGE:
//                    jsonArr = getJSONArrayFromInputStream(is);
//                    json = jsonArr.getJSONObject(0);
//                    this.image = new Image(json.getInt("id"));
//                    this.image.setLatitude(json.getDouble("latitude"));
//                    this.image.setLongitude(json.getDouble("longitude"));
//                    break;
//                case GET_IMAGES:
//                    jsonArr = getJSONArrayFromInputStream(is);
//                    this.images = new ArrayList<Image>();
//                    for (int i = 0; i < jsonArr.length(); i++) {
//                        json = jsonArr.getJSONObject(i);
//                        Image image = new Image(json.getInt("id"));
//                        image.setLatitude(json.getDouble("latitude"));
//                        image.setLongitude(json.getDouble("longitude"));
//                        images.add(image);
//                    }
//                    break;
//                case GET_IMAGE_DATA:
//                    if (paramsMap.get("imgType").getValue().equalsIgnoreCase("image")) {
//                        this.imageData = BitmapFactory.decodeStream(is);
//                    } else if (paramsMap.get("imgType").getValue().equalsIgnoreCase("thumbnail")) {
//                        this.thumbnailData = BitmapFactory.decodeStream(is);
//                    } else {
//                        return false;
//                    }
//                    break;
//                case GET_POPULAR_KEYWORDS:
//                    jsonArr = getJSONArrayFromInputStream(is);
//                    popularKeywords = new HashMap<String, Integer>();
//                    for (int i = 0; i < jsonArr.length(); i++) {
//                        json = jsonArr.getJSONObject(i);
//                        String kw = json.getString("keyword");
//                        int total = json.getInt("total");
//                        popularKeywords.put(kw, total);
//                    }
//                    break;
//                case POST_IMAGE:
//                    json = getJSONObjectFromInputStream(is);
//                    this.imageId = json.getInt("id");
//                    break;
//                case POST_KEYWORD:
//                    json = getJSONObjectFromInputStream(is);
//                    this.keywordId = json.getInt("id");
//                    break;
//            }
//        } catch (final IOException e) {
//            return false;
//        } catch (final JSONException e) {
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    protected void onPostExecute(Boolean aBoolean) {
//        super.onPostExecute(aBoolean);
//        if (aBoolean) {
//            Toast.makeText(context, "Upload successful",
//                    Toast.LENGTH_SHORT).show();
//        }
//        else {
//        Toast.makeText(context, "Upload failed",
//                Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private JSONArray getJSONArrayFromInputStream(final InputStream is)
//            throws IOException, JSONException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line + "\n");
//        }
//        is.close();
//        return new JSONArray(sb.toString());
//    }
//
//    private JSONObject getJSONObjectFromInputStream(final InputStream is)
//            throws IOException, JSONException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line + "\n");
//        }
//        is.close();
//        return new JSONObject(sb.toString());
//    }
//}
