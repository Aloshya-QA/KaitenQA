package factory;

import net.datafaker.Faker;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DataFactory {

    private final Faker faker = new Faker(new Locale("en"));

    public String generateEmail() {
        String base = faker.name()
                .username()
                .replaceAll("[^a-zA-Z0-9]", "")
                .toLowerCase();
        String digits = faker.number().digits(4);

        return base + digits + "@";
    }

    public String generatePassword() {
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
                .substring(0, 10);
    }

    public String generateWorkspaceName() {
        String baseName = faker.company()
                .name()
                .replaceAll("[^a-zA-Z0-9]", "")
                .toLowerCase();
        String randomSuffix = faker.number().digits(4);
        String workspaceName = baseName + randomSuffix;
        if (workspaceName.length() > 28) {
            workspaceName = workspaceName.substring(0, 28);
        }

        return workspaceName;
    }

    public String generateColumnName() {
        return faker.company().buzzword() + " " + faker.job().field();
    }

    public String generateSpaceName() {
        return faker.company().name();
    }

    public String generateBoardName() {
        return faker.company().industry();
    }

    public String generateDescription() {
        return faker.lorem().paragraph(5);
    }

    public String generateCardName() {
        return faker.company().catchPhrase();
    }

    public String generatePin() {
        return faker.number().digits(5);
    }

    public String generateUsername() {
        return faker.name().firstName() + faker.number().digits(3);
    }

    public String generateChecklist() {
        return faker.construction().roles();
    }

    public String generateChecklistItem() {
        return faker.construction().heavyEquipment();
    }

    public String generateUrl() {
        return faker.internet().url();
    }

    public String generateUrlTitle() {
        return faker.internet().domainName();
    }

    public String generateTag() {
        return faker.color().name();
    }

    public String generateSecondTag() {
        return faker.animal().name();
    }
}
