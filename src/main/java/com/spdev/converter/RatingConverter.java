package com.spdev.converter;

import com.spdev.entity.enums.Rating;

import javax.persistence.AttributeConverter;

public class RatingConverter implements AttributeConverter<Rating, Integer> {

    private static final Integer DIFFERENCE = 1;

    @Override
    public Integer convertToDatabaseColumn(Rating attribute) {
        return attribute.ordinal() + DIFFERENCE;
    }

    @Override
    public Rating convertToEntityAttribute(Integer dbData) {
        return switch (dbData) {
            case 1 -> Rating.ONE;
            case 2 -> Rating.TWO;
            case 3 -> Rating.THREE;
            case 4 -> Rating.FOUR;
            case 5 -> Rating.FIVE;
            default -> null;
        };
    }
}