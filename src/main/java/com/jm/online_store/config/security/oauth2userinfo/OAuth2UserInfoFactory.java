package com.jm.online_store.config.security.oauth2userinfo;

import com.jm.online_store.model.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.FACEBOOK.toString())) {}
        //This part should be changed with proper VK and OK auth info
            return new FacebookOAuth2UserInfo(attributes);
    }
}
