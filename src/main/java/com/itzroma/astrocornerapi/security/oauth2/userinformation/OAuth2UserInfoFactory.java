package com.itzroma.astrocornerapi.security.oauth2.userinformation;

import com.itzroma.astrocornerapi.exception.OAuth2AuthenticationProcessingException;
import com.itzroma.astrocornerapi.security.oauth2.userinformation.impl.FacebookOAuth2UserInfo;
import com.itzroma.astrocornerapi.security.oauth2.userinformation.impl.GithubOAuth2UserInfo;
import com.itzroma.astrocornerapi.security.oauth2.userinformation.impl.GoogleOAuth2UserInfo;
import com.itzroma.astrocornerapi.model.entity.AuthProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.FACEBOOK.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            log.debug("Sorry! Login with " + registrationId + " is not supported yet.");
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
