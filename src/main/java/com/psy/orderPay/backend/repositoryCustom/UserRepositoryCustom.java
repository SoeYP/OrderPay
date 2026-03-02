package com.psy.orderPay.backend.repositoryCustom;

import java.util.Optional;

import com.psy.orderPay.backend.dto.SignRequestDto;
import com.psy.orderPay.backend.entity.User;

public interface UserRepositoryCustom {

    Optional<User> save(SignRequestDto dto);
}
