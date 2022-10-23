package com.spdev.converter;

import javax.persistence.AttributeConverter;
import java.math.BigDecimal;

public class CostConverter implements AttributeConverter<BigDecimal, BigDecimal> {

    //В базе данных cost NUMERIC(10, 2). Сохраняется .00, а возвращается .0. С конвертером работает. 
    @Override
    public BigDecimal convertToDatabaseColumn(BigDecimal attribute) {
        return attribute.setScale(2);
    }

    @Override
    public BigDecimal convertToEntityAttribute(BigDecimal dbData) {
        return dbData.setScale(2);
    }
}