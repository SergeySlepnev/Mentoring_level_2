package com.dmdev.util;

import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class RandomUtils {

    private static final Random RANDOM = new Random();
    private static Integer START_ID = 6;

    private static final List<String> USER_NAMES = List.of(
            "James Hetfield", "Curt Cobain", "Bob Dylan", "Jimi Hendrix", "Mick Jagger",
            "Aric Clapton", "Jim Morrison", "Ozzy Osborne", "Slash", " Jon Bon Jovi"
    );

    private static final List<String> FIRST_NAMES = List.of(
            "Oliver", "Jack", "Harry", "Jacob", "Charley",
            "Thomas", "George", "Oscar", "Maxim", "Will"
    );

    private static final List<String> DATES = List.of(
            "2011-11-18", "1988-12-03", "1958-05-11", "2000-08-04",
            "1999-06-21", "1951-10-25", "1989-11-14", "1967-10-10"
    );

    private static final List<String> EMAILS = List.of(
            "exampleEmail@gmail.com", "randomEmail@gmail.com", "james@gmail.com", "curt@gmail.com",
            "mick@gmail.com", "ozzy@gmail.com", "jimi@gmail.com", "bob@gmail.com"
    );

    private static final List<String> PASS_WORDS = List.of(
            "encryptedWord", "758458", "78754872", "2454645",
            "6455224", "5412_354", "875787D", "564SDF8S"
    );

    private static int randomIndex(List<String> list) {
        return RANDOM.nextInt(list.size());
    }

    public static String randomUserName() {
        return USER_NAMES.get(randomIndex(USER_NAMES));
    }

    public static String randomDate() {
        return DATES.get(randomIndex(DATES));
    }

    public static String randomEmail() {
        return EMAILS.get(randomIndex(EMAILS));
    }

    public static Integer getId() {
        return START_ID++;
    }

    public static String randomPassword() {
        return PASS_WORDS.get(randomIndex(PASS_WORDS));
    }
}