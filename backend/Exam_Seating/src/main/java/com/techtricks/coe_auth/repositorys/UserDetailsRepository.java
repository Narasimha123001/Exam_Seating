package com.techtricks.coe_auth.repositorys;

import com.techtricks.coe_auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByRegisterNumber(Long regNo);

    Optional<User> findByEmail(String email);
}
