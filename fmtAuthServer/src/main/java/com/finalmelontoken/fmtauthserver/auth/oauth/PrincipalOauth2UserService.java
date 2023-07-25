package com.finalmelontoken.fmtauthserver.auth.oauth;

import com.finalmelontoken.fmtauthserver.auth.PrincipalDetails;
import com.finalmelontoken.fmtauthserver.domain.User;
import com.finalmelontoken.fmtauthserver.domain.UserRole;
import com.finalmelontoken.fmtauthserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes : {}" + oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();

        if(provider.equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String nickname = oAuth2UserInfo.getName();
        User user;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);

        UserRole userRole = UserRole.ROLE_USER;
        if(optionalUser.isEmpty()) {
            if (!isEmailValid(email)) userRole = UserRole.ROLE_GUEST;
            user = User.builder()
                    .loginId(loginId)
                    .nickname(nickname)
                    .provider(provider)
                    .providerId(providerId)
                    .role(userRole)
                    .email(email)
                    .point(0L)
                    .build();
            if (isEmailValid(email)) userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

    private boolean isEmailValid(String email) {
        return email.split("@")[1].equals("dgsw.hs.kr");
    }
}