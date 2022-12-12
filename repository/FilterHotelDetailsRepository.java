package com.spdev.repository;

import com.spdev.entity.User;
import com.spdev.filter.UserFilter;

import java.util.List;

public interface FilterHotelDetailsRepository {

    List<User> findByHotel(UserFilter filter);
}