package com.itzroma.astrocornerapi.repository;

import com.itzroma.astrocornerapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE " +
            "WHERE u.email = ?1")
    int enableUser(String email);
}
