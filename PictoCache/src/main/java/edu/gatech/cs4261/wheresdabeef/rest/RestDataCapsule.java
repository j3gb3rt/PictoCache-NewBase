package edu.gatech.cs4261.wheresdabeef.rest;//package edu.gatech.cs4261.wheresdabeef.rest;
//
//import android.net.Uri;
//
//import org.apache.http.NameValuePair;
//
//import java.util.ArrayList;
//import java.util.List;
//import edu.gatech.cs4261.wheresdabeef.rest.RestApiV2.RestAction;
//
///**
// * Created by jonathan on 11/4/13.
// */
//public class RestDataCapsule {
//    private RestAction action;
//    private List<NameValuePair> params;
//    private Uri image;
//    private Uri thumb;
//
//    public void addParam(final NameValuePair param) {
//        if (params == null) {
//            params = new ArrayList<NameValuePair>();
//        }
//        params.add(param);
//    }
//
//    public List<NameValuePair> getParams() {
//        if (params == null) {
//            params = new ArrayList<NameValuePair>();
//        }
//        return this.params;
//    }
//
//    public void setAction(final RestAction action) {
//        this.action = action;
//    }
//
//    public RestAction getAction() {
//        return this.action;
//    }
//
//    public void setImage(final Uri img) {
//        this.image = img;
//    }
//
//    public Uri getImage() {
//        return this.image;
//    }
//
//    public void setThumb(final Uri thumb) {
//        this.thumb = thumb;
//    }
//
//    public Uri getThumb() {
//        return this.thumb;
//    }
//}