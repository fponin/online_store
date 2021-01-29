package com.jm.online_store.repository;

import com.jm.online_store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

    User findByFirstName(String username);

    User findUserByEmail(String email);

    boolean existsByEmail(String email);

    User findUserById(Long id);
}
