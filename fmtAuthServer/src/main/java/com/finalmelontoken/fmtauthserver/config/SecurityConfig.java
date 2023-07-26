package com.finalmelontoken.fmtauthserver.config;

import com.finalmelontoken.fmtauthserver.auth.oauth.PrincipalOauth2UserService;
import com.finalmelontoken.fmtauthserver.domain.UserRole;
import com.finalmelontoken.fmtauthserver.filter.ExceptionHandlerFilter;
import com.finalmelontoken.fmtauthserver.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .anyRequest().permitAll()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                    .oauth2Login()
                        .defaultSuccessUrl("/oauth-result")
                        .failureUrl("/fail")
                        .userInfoEndpoint()
                    .userService(principalOauth2UserService);
        http
                .addFilterBefore(new ExceptionHandlerFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}