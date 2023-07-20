package com.finalmelontoken.fmtauthserver.service;

import com.finalmelontoken.fmtauthserver.domain.User;
import com.finalmelontoken.fmtauthserver.exception.BaseException;
import com.finalmelontoken.fmtauthserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public boolean register(User user) {
        User alreadyUser = userRepository.findById(user.getId());
        if (alreadyUser != null) {
            throw new BaseException(HttpStatus.BAD_REQUEST, "already exist user id");
        }
        userRepository.save(user);
        return true;
    }

    public boolean login(User user) {
        return true;
    }
}
