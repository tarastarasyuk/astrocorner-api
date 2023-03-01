package com.itzroma.astrocornerapi.security.oauth2.userinformation.impl;

import com.itzroma.astrocornerapi.security.oauth2.constant.DefaultOAuth2Constant.FacebookUserInfo;
import com.itzroma.astrocornerapi.security.oauth2.userinformation.OAuth2UserInfo;

import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {

    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get(FacebookUserInfo.ID);
    }

    @Override
    public String getName() {
        return (String) attributes.get(FacebookUserInfo.NAME);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get(FacebookUserInfo.EMAIL);
    }

    @Override
    public String getImageUrl() {
        if (attributes.containsKey("picture")) {
            Map<String, Object> pictureObj = (Map<String, Object>) attributes.get(FacebookUserInfo.PICTURE);
            if (pictureObj.containsKey("data")) {
                Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get("data");
                if (dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
            }
        }
        return null;
    }
}
