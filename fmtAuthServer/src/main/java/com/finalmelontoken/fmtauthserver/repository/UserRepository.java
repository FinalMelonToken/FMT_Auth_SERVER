package com.finalmelontoken.fmtauthserver.repository;

import com.finalmelontoken.fmtauthserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByLoginId(String loginId);

    public User findByEmail(String email);

    public boolean existsByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.password = ?1")
    public void updateByPassword(String password);
}