package com.spdev.repository;

import com.spdev.dto.UserFilter;
import com.spdev.entity.User;
import org.springframework.data.domain.Page;

public interface FilterUserRepository {

    Page<User> findAllByFilter(UserFilter filter, Integer pageSize);

    void update(User user);
}
