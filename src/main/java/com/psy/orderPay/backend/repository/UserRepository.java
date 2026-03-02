package com.psy.orderPay.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.psy.orderPay.backend.entity.User;
import com.psy.orderPay.backend.repositoryCustom.UserRepositoryCustom;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);
}