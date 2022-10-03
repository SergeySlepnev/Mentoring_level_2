package com.spdev.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Rating {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final Integer rating;
}
