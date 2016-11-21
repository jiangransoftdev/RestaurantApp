package com.example.app;

/**
 * Created by ranjiang on 2016/11/20.
 */
import org.scribe.model.*;
import org.scribe.builder.api.*;
public class TwoStepOAuth extends DefaultApi10a {
    @Override
    public String getAccessTokenEndpoint() { return null; }


    @Override
    public String getAuthorizationUrl(Token unused) {
        return null;
    }


    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }
}
