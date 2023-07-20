package com.finalmelontoken.fmtauthserver.repository;

import com.finalmelontoken.fmtauthserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findById(String id);

    public void updateByRefreshToken(String refreshToken);
}