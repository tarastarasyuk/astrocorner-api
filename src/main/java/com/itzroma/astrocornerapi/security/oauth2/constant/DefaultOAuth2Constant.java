package com.itzroma.astrocornerapi.security.oauth2.constant;

public final class DefaultOAuth2Constant {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String OAUTH2_REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final int OAUTH2_COOKIE_EXPIRE_SECONDS = 180;

    public static final String OAUTH2_JWT_TOKEN_PASS_NAME = "OAuth2-JWT-Token";

    public static class GoogleUserInfo {
        public static final String ID = "sub";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PICTURE = "picture";
    }

    public static class FacebookUserInfo {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PICTURE = "picture";
    }

    public static class GithubUserInfo {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PICTURE = "avatar_url";
    }

}
