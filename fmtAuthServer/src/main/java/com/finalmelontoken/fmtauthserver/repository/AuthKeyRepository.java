package com.finalmelontoken.fmtauthserver.repository;

import com.finalmelontoken.fmtauthserver.domain.AuthKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthKeyRepository extends JpaRepository<AuthKey, Long> {
    public boolean existsAuthKeyByEmail(String email);
    public void deleteByEmail(String email);
    public AuthKey findByEmail(String email);
}
