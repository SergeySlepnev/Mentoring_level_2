package com.spdev.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN,
    OWNER,
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
