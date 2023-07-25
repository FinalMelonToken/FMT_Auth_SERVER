package com.finalmelontoken.fmtauthserver.service;

import com.finalmelontoken.fmtauthserver.domain.User;
import com.finalmelontoken.fmtauthserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElse(null);
    }
}