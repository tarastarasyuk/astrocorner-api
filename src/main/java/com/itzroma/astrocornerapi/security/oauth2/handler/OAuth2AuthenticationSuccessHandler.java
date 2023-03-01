package com.itzroma.astrocornerapi.security.oauth2.handler;

import com.itzroma.astrocornerapi.exception.BadRequestException;
import com.itzroma.astrocornerapi.security.config.AppProperties;
import com.itzroma.astrocornerapi.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.itzroma.astrocornerapi.security.service.JwtService;
import com.itzroma.astrocornerapi.security.userdetails.DefaultUserDetails;
import com.itzroma.astrocornerapi.security.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.itzroma.astrocornerapi.security.oauth2.constant.DefaultOAuth2Constant.OAUTH2_JWT_TOKEN_PASS_NAME;
import static com.itzroma.astrocornerapi.security.oauth2.constant.DefaultOAuth2Constant.OAUTH2_REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final AppProperties appProperties;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        // TODO: !IMPORTANT! change it !NOT SECURE!
//        addJwtTokenInSession(request, authentication);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

//    private void addJwtTokenInSession(HttpServletRequest request, Authentication authentication) {
//        String token = jwtService.generateAccessToken((DefaultUserDetails) authentication.getPrincipal());
//        request.getSession().setAttribute(OAUTH2_JWT_TOKEN_PASS_NAME, token);
//    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, OAUTH2_REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            log.debug("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", jwtService.generateAccessToken((DefaultUserDetails) authentication.getPrincipal()))
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> validateHostAndPort(clientRedirectUri, authorizedRedirectUri));
    }

    private static boolean validateHostAndPort(URI clientRedirectUri, String authorizedRedirectUri) {
        URI authorizedURI = URI.create(authorizedRedirectUri);
        return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedURI.getPort() == clientRedirectUri.getPort();
    }

}
