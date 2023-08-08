package com.finalmelontoken.fmtauthserver.repository;

import com.finalmelontoken.fmtauthserver.domain.SecretKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretKeyRepository extends JpaRepository<SecretKey, Long> {
    public SecretKey findByClientKey(String clientKey);

    public boolean existsByClientKey(String clientKey);
}
