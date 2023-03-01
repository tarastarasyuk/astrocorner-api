package com.itzroma.astrocornerapi.security.oauth2.userinformation.impl;

import com.itzroma.astrocornerapi.security.oauth2.constant.DefaultOAuth2Constant.GoogleUserInfo;
import com.itzroma.astrocornerapi.security.oauth2.userinformation.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get(GoogleUserInfo.ID);
    }

    @Override
    public String getName() {
        return (String) attributes.get(GoogleUserInfo.NAME);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get(GoogleUserInfo.EMAIL);
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get(GoogleUserInfo.PICTURE);
    }
}
