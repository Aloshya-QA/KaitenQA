package api.tempMail;

import api.tempMail.dto.request.CreateMailRq;
import api.tempMail.dto.request.GetTokenRq;
import api.tempMail.dto.response.GetDomainsRs;
import api.tempMail.dto.response.GetMessagesRs;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static api.tempMail.TempMailClient.*;
import static utils.PropertyReader.*;

@Log4j2
public class TempMailService {
    String
            email = getProperty("email"),
            mailboxPassword = getProperty("mailboxPassword");

    private final static String PIN_CODE_SUBJECT = "PIN-код для аутентификации в Kaiten";

    public static String getFirstDomain() {
        GetDomainsRs response = getDomains();
        if (response != null && response.domains != null && !response.domains.isEmpty()) {
            return response.domains.get(0).domain;
        } else {
            throw new RuntimeException("No domains returned from API");
        }
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

        List<GetMessagesRs> messages = getMessages(token);

        Optional<String> messageId = messages.stream()
                .filter(m -> m.subject != null && m.subject.toLowerCase().contains(subjectText.toLowerCase()))
                .max(Comparator.comparing(m -> Instant.parse(m.updatedAt))) // самое новое
                .map(m -> m.id);

        if (messageId.isPresent()) {
            log.info("Found latest message with subject '{}': {}", subjectText, messageId.get());
            return messageId.get();
        } else {
            log.warn("No message found with subject: {}", subjectText);
            return null;
        }
    }


    public String getTextMessageBySubject(String subject) {
        return getTextMessage(getMessageId(subject), getProperty("mailboxApiToken")).text;
    }

    public String getActivateCompanyUrl() {
        String messageText = getTextMessageBySubject("Активация компании");

        String startWithText = "активировать компанию";
        String endWithText = "Если вы не регистрировали";
        int startIndex = messageText.indexOf(startWithText);
        int endIndex = messageText.indexOf(endWithText);

        return messageText.substring(startIndex + startWithText.length() + 2, endIndex - 3);
    }

    public String getPin() {
        String unusedPin = getProperty("unusedPin");
        String unusedPinReceivedAt = getProperty("unusedPinReceivedAt");
        String usedPin = getProperty("usedPin");

        if (unusedPin != null && unusedPinReceivedAt != null) {
            Instant receivedAt = Instant.parse(unusedPinReceivedAt);
            Duration duration = Duration.between(receivedAt, Instant.now());

            if (duration.toMinutes() < 5) {
                log.info("Using previously saved unused PIN: {}", unusedPin);
                setProperty("usedPin", unusedPin);
                removeProperty("unusedPin");
                removeProperty("unusedPinReceivedAt");
                saveProperties();
                return unusedPin;
            } else {
                log.info("Saved unused PIN expired");
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
}
