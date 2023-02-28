package com.itzroma.astrocornerapi.security.oauth2.userinfo;

import com.itzroma.astrocornerapi.exception.OAuth2AuthenticationProcessingException;
import com.itzroma.astrocornerapi.security.oauth2.userinfo.impl.FacebookOAuth2UserInfo;
import com.itzroma.astrocornerapi.security.oauth2.userinfo.impl.GithubOAuth2UserInfo;
import com.itzroma.astrocornerapi.security.oauth2.userinfo.impl.GoogleOAuth2UserInfo;
import com.itzroma.astrocornerapi.model.entity.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.FACEBOOK.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
