package com.example.app;

/**
 * Created by ranjiang on 2016/11/20.
 */
import org.scribe.model.*;
import org.scribe.oauth.*;
import org.scribe.builder.*;
import org.json.*;
import java.util.*;
import android.util.*;
import android.graphics.*;
import java.net.*;
import java.io.*;
public class YelpApi {
    private static final String API_HOST = "api.yelp.com";
    private static final String SEARCH_PATH = "/v2/search";
    private static final String CONSUMER_KEY = "qiggFx-F08CxIHPnMAMz4w";
    private static final String CONSUMER_SECRET = "lG92QI4EizAFSfi7h3dmooX1mjI";
    private static final String TOKEN = "bkwwe8hXQntxek9Nky0xhLTkBqAAKXG_";
    private static final String TOKEN_SECRET = "ocsc5Ujj1n-t86KRc2lGVAe3rGY";


    private OAuthService service;
    private Token accessToken;
    public YelpApi() {
        this.service = new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(CONSUMER_KEY)
                .apiSecret(CONSUMER_SECRET).build();
        this.accessToken = new Token(TOKEN, TOKEN_SECRET);
    }
    /**
     * Fire a search request to Yelp API.
     */
    public String searchForBusinessesByLocation(String term, String location, int searchLimit) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + SEARCH_PATH);
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("limit", String.valueOf(searchLimit));
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    /**
     * Download an Image from the given URL, then decodes and returns a Bitmap object.
     */



}
