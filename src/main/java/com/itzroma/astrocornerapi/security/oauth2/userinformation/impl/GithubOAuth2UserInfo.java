package com.itzroma.astrocornerapi.security.oauth2.userinformation.impl;

import com.itzroma.astrocornerapi.security.oauth2.userinformation.OAuth2UserInfo;

import java.util.Map;

import static com.itzroma.astrocornerapi.security.oauth2.constant.DefaultOAuth2Constant.GithubUserInfo;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get(GithubUserInfo.ID)).toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get(GithubUserInfo.NAME);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get(GithubUserInfo.EMAIL);
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get(GithubUserInfo.PICTURE);
    }
}
