package com.itzroma.astrocornerapi.security.oauth2;

import com.itzroma.astrocornerapi.exception.OAuth2AuthenticationProcessingException;
import com.itzroma.astrocornerapi.model.entity.User;
import com.itzroma.astrocornerapi.repository.UserRepository;
import com.itzroma.astrocornerapi.security.oauth2.userinformation.OAuth2UserInfo;
import com.itzroma.astrocornerapi.security.oauth2.userinformation.OAuth2UserInfoFactory;
import com.itzroma.astrocornerapi.model.entity.AuthProvider;
import com.itzroma.astrocornerapi.security.userdetails.DefaultUserDetails;
import com.itzroma.astrocornerapi.security.util.CookieUtils;
import com.itzroma.astrocornerapi.service.impl.DefaultUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.itzroma.astrocornerapi.security.oauth2.constant.DefaultOAuth2Constant.OAUTH2_REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultOAuth2UserService extends org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            log.debug("InternalAuthenticationServiceException: processOAuth2User failed for user: "+ oAuth2User.getName());
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user = handleUserAuth(oAuth2UserRequest, oAuth2UserInfo, userOptional);

        return DefaultUserDetails.create(user, oAuth2User.getAttributes());
    }

    private User handleUserAuth(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo, Optional<User> userOptional) {
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()))) {
                log.debug("Not correct oauth2 provider");
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
//            CookieUtils.getCookie(request, OAUTH2_REDIRECT_URI_PARAM_COOKIE_NAME);
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        return user;
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
        user.setProviderId(oAuth2UserInfo.getId());

        user.setFirstName(userNames(oAuth2UserInfo.getName())[0]);
        user.setLastName(userNames(oAuth2UserInfo.getName())[1]);

        user.setEmail(oAuth2UserInfo.getEmail());

        skipEmailVerification(user);
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstName(userNames(oAuth2UserInfo.getName())[0]);
        existingUser.setLastName(userNames(oAuth2UserInfo.getName())[1]);

        skipEmailVerification(existingUser);
        return userRepository.save(existingUser);
    }

    private void skipEmailVerification(User user) {
        user.setEnabled(true);
    }

    private String[] userNames(String fullName) {
        return fullName.trim().split(" ");
    }

}
