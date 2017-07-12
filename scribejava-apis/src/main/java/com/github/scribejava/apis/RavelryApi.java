package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.OAuth1SignatureType;
import com.github.scribejava.core.model.OAuth1RequestToken;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RavelryApi extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://www.ravelry.com/oauth/authorize?oauth_token=%s";
    private static final String REQUEST_TOKEN_ENDPOINT = "https://www.ravelry.com/oauth/request_token";
    private static final String ACCESS_TOKEN_ENDPOINT = "https://www.ravelry.com/oauth/access_token";

    public enum Scope {
        FORUM_WRITE("forum-write"),
        MESSAGE_WRITE("message-write"),
        PATTERNSTORE_READ("patternstore-read"),
        DELIVERIES_READ("deliveries-read"),
        LIBRARY_PDF("library-pdf"),
        PROFILE_ONLY("profile-only"),
        CARTS_ONLY("carts-only");

        private String value;

        Scope(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private final List<Scope> scopes;

    protected RavelryApi() {
        scopes = new ArrayList<>();
    }

    protected RavelryApi(List<Scope> scopes) {
        this.scopes = scopes;
    }

    private static class InstanceHolder {
        private static final RavelryApi INSTANCE = new RavelryApi();
    }

    public static RavelryApi instance() {
        return InstanceHolder.INSTANCE;
    }

    public static RavelryApi instance(Scope... scopes) {
        return scopes == null ? instance() : new RavelryApi(Arrays.asList(scopes));
    }

    @Override
    public String getRequestTokenEndpoint() {
        final StringBuilder requestTokenBuilder = new StringBuilder(REQUEST_TOKEN_ENDPOINT);
        if (!scopes.isEmpty()) {
            requestTokenBuilder.append("?scope=");
            String sep = "";
            for (Scope scope : scopes) {
                requestTokenBuilder.append(sep);
                requestTokenBuilder.append(scope.toString());
                sep = "+";
            }
        }
        return requestTokenBuilder.toString();
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

    @Override
    public OAuth1SignatureType getSignatureType() {
        return OAuth1SignatureType.Header;
    }

}
