package com.finalmelontoken.fmtauthserver.repository;

import com.finalmelontoken.fmtauthserver.domain.AuthKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthKeyRepository extends JpaRepository<AuthKey, Long> {
    public boolean existsAuthKeyByEmail(String email);

    @Modifying
    @Query("DELETE FROM AuthKey u WHERE u.email IN :email")
    public void deleteByEmail(@Param("email") String email);
    public AuthKey findByEmail(String email);

}