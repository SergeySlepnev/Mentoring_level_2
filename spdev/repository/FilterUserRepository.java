package com.spdev.repository;

import com.spdev.entity.User;
import com.spdev.filter.UserFilter;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllByFilter(UserFilter filter);
}