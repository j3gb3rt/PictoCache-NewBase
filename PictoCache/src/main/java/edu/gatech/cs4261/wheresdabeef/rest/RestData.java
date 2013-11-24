package edu.gatech.cs4261.wheresdabeef.rest;

import android.net.Uri;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HolocronCoder on 11/4/13.
 */
public class RestData {

    private enum HttpAction {
        GET,
        POST;
    }

    public enum RestAction {
        /**
         * Expected Parameters:
         *      id
         */
        GET_IMAGE(HttpAction.GET),
        /**
         * Required Parameters:
         *      ap (adapter position)
         * Optional Parameters:
         *      sd (sort direction)
         *      sc (sort column)
         *      l (how many images, max)
         *
         *      - AND -
         *
         *      minLat (minimum latitude)
         *      maxLat (maximum latitude)
         *      minLon (minimum longitude)
         *      maxLon (maximum longitude)
         *
         *      - OR -
         *
         *      k (keyword)
         */
        GET_IMAGES(HttpAction.GET),
        /**
         * Expected Parameters:
         *      id
         *      ap (adapter position)
         *      imgType ("thumbnail" or "image")
         */
        GET_IMAGE_DATA(HttpAction.GET),
        /**
         * Optional Parameters:
         *      l (how many images, max)
         */
        GET_POPULAR_KEYWORDS(HttpAction.GET),
        /**
         * Expected Parameters:
         *      lat (latitude)
         *      lon (longitude)
         *
         * Expected Set Values
         *      image (Uri)
         *      thumb (Uri)
         */
        POST_IMAGE(HttpAction.POST),
        /**
         * Expected Parameters:
         *      kw (Keyword string)
         *      imgId (ID of associated image)
         */
        POST_KEYWORD(HttpAction.POST);

        private HttpAction http;

        private RestAction(HttpAction http) {
            this.http = http;
        }

        public HttpAction getHttp() {
            return http;
        }
    }

    private RestAction action;
    private List<NameValuePair> params;
    private Uri image;
    private Uri thumb;

    public void addParam(final NameValuePair param) {
        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }
        params.add(param);
    }

    public List<NameValuePair> getParams() {
        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }
        return this.params;
    }

    public void setAction(final RestAction action) {
        this.action = action;
    }

    public RestAction getAction() {
        return this.action;
    }

    public void setImage(final Uri img) {
        this.image = img;
    }

    public Uri getImage() {
        return this.image;
    }

    public void setThumb(final Uri thumb) {
        this.thumb = thumb;
    }

    public Uri getThumb() {
        return this.thumb;
    }
}
