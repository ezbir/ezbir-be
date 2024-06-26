package com.ua.ezbir.repository;

import com.ua.ezbir.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Stream<User> streamAllBy();

    Stream<User> streamAllByUsernameStartsWithIgnoreCase(String prefixName);
}
