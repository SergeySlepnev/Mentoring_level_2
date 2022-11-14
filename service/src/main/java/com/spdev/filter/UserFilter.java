package com.spdev.filter;

import com.spdev.entity.enums.Role;
import com.spdev.entity.enums.Status;
import lombok.Builder;

@Builder
public record UserFilter(Role role,
                         String firstName,
                         String lastName,
                         Status status) {

}
