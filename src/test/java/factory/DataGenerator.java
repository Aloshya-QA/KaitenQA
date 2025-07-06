package factory;

import com.github.javafaker.Faker;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));

    public static String generateEmailLogin() {
        String base = faker.name().username().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String digits = faker.number().digits(4);
        return base + digits;
    }

    public static String generatePassword() {
        String upper = faker.letterify("?").toUpperCase();
        String lower = faker.letterify("?").toLowerCase();
        String digit = faker.number().digit();
        String rest = faker.bothify("???????");

        String combined = upper + lower + digit + rest;

        List<Character> chars = combined.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(chars);

        return chars.stream()
                .map(String::valueOf)
                .collect(Collectors.joining())
                .substring(0, 10); // максимум 10 символов
    }

    public static String generateWorkspaceName() {
        String baseName = faker.company().name().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String randomSuffix = faker.number().digits(4);
        String workspaceName = baseName + randomSuffix;
        if (workspaceName.length() > 28) {
            workspaceName = workspaceName.substring(0, 28);
        }
        return workspaceName;
    }

    public static String generateSubdomain(String companyName) {
        return companyName + ".kaiten.ru";
    }
}