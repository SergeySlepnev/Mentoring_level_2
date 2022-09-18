package com.dmdev.unit;

import com.dmdev.util.LocalDateFormatter;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

@Tag("unit")
@Tag("util")
class LocalDateFormatterTest {

    @Test
    void itShouldValidInputStringAsData() {
        var expectedResult = LocalDateFormatter.isValid("2022-12-12");
        assertThat(expectedResult).isTrue();
    }

    @Test
    void itShouldValidIfInputStringNull() {
        var expectedResult = LocalDateFormatter.isValid(null);
        assertThat(expectedResult).isFalse();
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDataForDataFormatter")
    void itShouldNotValidInputStringAsData(String invalidData) {
        var expectedResult = LocalDateFormatter.isValid(invalidData);
        assertThat(expectedResult).isFalse();
    }

    private static Stream<Arguments> provideInvalidDataForDataFormatter() {
        return Stream.of(
                of("2022/12/12"),
                of("2022_12_12"),
                of("20221212"),
                of("2022.12.12"),
                of("WordInsteadDigits")
        );
    }
}