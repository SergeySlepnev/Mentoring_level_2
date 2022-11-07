package com.spdev.converter;

import javax.persistence.AttributeConverter;
import java.math.BigDecimal;

public class CostConverter implements AttributeConverter<BigDecimal, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(BigDecimal attribute) {
        return attribute.setScale(2);
    }

    @Override
    public BigDecimal convertToEntityAttribute(BigDecimal dbData) {
        return dbData.setScale(2);
    }
}