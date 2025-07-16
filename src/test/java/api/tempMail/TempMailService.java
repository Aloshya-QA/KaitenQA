package api.tempMail;

import api.tempMail.dto.request.CreateMailRq;
import api.tempMail.dto.request.GetTokenRq;
import api.tempMail.dto.response.GetMessagesRs;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;

import static api.tempMail.TempMailClient.*;
import static utils.PropertyReader.*;

@Log4j2
public class TempMailService {
    String
            email = getProperty("email"),
            mailboxPassword = getProperty("mailboxPassword");

    private final static String
            PIN_CODE_SUBJECT = "PIN-код для аутентификации в Kaiten",
            ACTIVATE_SUBJECT = "Активация компании";

//    public static String getFirstDomain() {
//        GetDomainsRs response = getDomains();
//        if (response != null && response.domains != null && !response.domains.isEmpty()) {
//            return response.domains.get(0).domain;
//        } else {
//            throw new RuntimeException("No domains returned from API");
//        }
//    }

    public static String getFirstDomain() {
        String domain = getDomains().domains.get(0).domain;

        if (domain == null || domain.isBlank()) {
            throw new RuntimeException("First domain is empty or null");
        }

        return domain;
    }

    public void createMail() {
        CreateMailRq rq = CreateMailRq.builder()
                .address(email)
                .password(mailboxPassword)
                .build();

        createTempMail(rq);
    }

    public String getToken() {
        GetTokenRq rq = GetTokenRq.builder()
                .address(email)
                .password(mailboxPassword)
                .build();

        return getAuthToken(rq).token;
    }

    public String getMessageId(String subjectText) {
        String token = getProperty("mailboxApiToken");

        int maxAttempts = 10;
        int delaySeconds = 5;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            GetMessagesRs response = getMessages(token);

            Optional<String> messageId = response.messages.stream()
                    .filter(m -> m.subject != null && m.subject.toLowerCase().contains(subjectText.toLowerCase()))
                    .max(Comparator.comparing(m -> Instant.parse(m.updatedAt)))
                    .map(m -> m.id);

            if (messageId.isPresent()) {
                log.info("Found message with subject '{}' on attempt {}: {}", subjectText, attempt, messageId.get());
                return messageId.get();
            } else {
                log.info("No message with subject '{}' found on attempt {}/{}", subjectText, attempt, maxAttempts);
            }

            try {
                Thread.sleep(delaySeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Interrupted while waiting for new message");
                break;
            }
        }

        throw new RuntimeException("Message with subject '" + subjectText + "' was not found after " + maxAttempts + " attempts");
    }



    public String getActivateCompanyUrl() {
        String latestMessage = extractActivateCompanyUrl();
        if (latestMessage == null) {
            latestMessage = waitForNewCompanyUrl();
        }
        return latestMessage;
    }

    public String extractActivateCompanyUrl() {
        String messageText = getTextMessage(
                getMessageId(ACTIVATE_SUBJECT),
                getProperty("mailboxApiToken")
        ).text;
        if (messageText != null) {

            String startWithText = "активировать компанию";
            String endWithText = "Если вы не регистрировали";
            int startIndex = messageText.indexOf(startWithText);
            int endIndex = messageText.indexOf(endWithText);

            return messageText.substring(startIndex + startWithText.length() + 2, endIndex - 3);
        } else {
            return null;
        }
    }

    public String getPin() {
        String unusedPin = getProperty("unusedPin");
        String unusedPinReceivedAt = getProperty("unusedPinReceivedAt");
        String usedPin = getProperty("usedPin");

        if (unusedPin != null && unusedPinReceivedAt != null) {
            Instant receivedAt = Instant.parse(unusedPinReceivedAt);
            Duration duration = Duration.between(receivedAt, Instant.now());

            if (duration.toMinutes() < 5) {
                log.info("Using previously unused PIN: {}", unusedPin);
                setProperty("usedPin", unusedPin);
                removeProperty("unusedPin");
                removeProperty("unusedPinReceivedAt");
                saveProperties();
                return unusedPin;
            } else {
                log.info("Previously unused PIN expired");
            }
        }

        String latestPin = extractPin();

        if (usedPin != null && usedPin.equals(latestPin)) {
            log.info("Latest PIN from email equals used PIN, waiting for new PIN...");
            latestPin = waitForNewPin(usedPin);
        }

        setProperty("usedPin", latestPin);
        saveProperties();

        return latestPin;
    }

    public String extractPin() {
        String token = getProperty("mailboxApiToken");
        String messageId = getMessageId(PIN_CODE_SUBJECT);

        String messageText = getTextMessage(messageId, token).text;

        String startWithText = "PIN-код";
        String endWithText = "Скопируйте";
        int startIndex = messageText.indexOf(startWithText);
        int endIndex = messageText.indexOf(endWithText);

        return messageText.substring(startIndex + startWithText.length() + 2, endIndex - 2);
    }

    private String waitForNewPin(String usedPin) {
        int attempts = 10;
        int delaySeconds = 5;

        for (int i = 0; i < attempts; i++) {
            String newPin = extractPin();

            if (newPin != null && !newPin.equals(usedPin)) {
                log.info("Received new PIN: {}", newPin);
                return newPin;
            }

            log.info("Waiting for new PIN... attempt {}/{}", i + 1, attempts);
            try {
                Thread.sleep(delaySeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.warn("Timed out waiting for new PIN, returning used PIN: {}", usedPin);
        return usedPin;
    }

    private String waitForNewCompanyUrl() {
        int attempts = 10;
        int delaySeconds = 5;

        for (int i = 0; i < attempts; i++) {
            String newCompanyUrl = extractActivateCompanyUrl();

            if (newCompanyUrl != null) {
                log.info("Received new Company url: {}", newCompanyUrl);
                return newCompanyUrl;
            }

            log.info("Waiting for new Company url... attempt {}/{}", i + 1, attempts);
            try {
                Thread.sleep(delaySeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.warn("Timed out waiting for new Company URL");
        return null;
    }
}
