package factory;

import net.datafaker.Faker;

import java.util.Locale;

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
        return faker
                .internet()
                .password(
                        8,
                        15,
                        true,
                        true,
                        true
                );
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
}
