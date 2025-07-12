package api.tempMail;

import api.tempMail.dto.request.CreateMailRq;
import api.tempMail.dto.request.GetTokenRq;
import api.tempMail.dto.response.GetDomainsRs;
import api.tempMail.dto.response.GetMessagesRs;
import lombok.extern.log4j.Log4j2;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static api.tempMail.TempMailClient.*;
import static utils.PropertyReader.getProperty;

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

    public List<String> getEmailSubjects() throws InterruptedException {
        Thread.sleep(5000);
        List<GetMessagesRs> messages = getMessages(getProperty("mailboxApiToken"));
        List<String> subjects = new ArrayList<>();
        for (GetMessagesRs message : messages) {
            subjects.add(message.subject);
        }
        return subjects;
    }

    private String getMessageId(String subjectText) {
        String threadKey = Thread.currentThread().getName();
        String testStartTime = getProperty("testStartTime_" + threadKey);
        Instant startTime = Instant.parse(testStartTime);

        int maxAttempts = 10;
        int delaySeconds = 2;

        for (int i = 0; i < maxAttempts; i++) {
            List<GetMessagesRs> messages = getMessages(getProperty("mailboxApiToken"));

            Optional<String> messageId = messages.stream()
                    .filter(message -> message.subject != null && message.subject.toLowerCase().contains(subjectText.toLowerCase()))
                    .filter(message -> Instant.parse(message.updatedAt).isAfter(startTime))
                    .max(Comparator.comparing(message -> Instant.parse(message.updatedAt)))
                    .map(message -> message.id);

            if (messageId.isPresent()) {
                log.info("Found message on attempt {}: {}", i + 1, messageId.get());
                return messageId.get();
            }

            log.info("Message not found yet. Attempt {}/{}. Retrying in {} seconds...", i + 1, maxAttempts, delaySeconds);
            try {
                Thread.sleep(delaySeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.warn("Message not found after {} attempts", maxAttempts);
        return null;
    }

    public String getTextLatestMessageBySubject(String subject) {
        return getTextMessage(getMessageId(subject), getProperty("mailboxApiToken")).text;
    }

    public String getActivateCompanyUrl() {
        String messageText = getTextLatestMessageBySubject("Активация компании");

        String startWithText = "активировать компанию";
        String endWithText = "Если вы не регистрировали";
        int startIndex = messageText.indexOf(startWithText);
        int endIndex = messageText.indexOf(endWithText);

        return messageText.substring(startIndex + startWithText.length() + 2, endIndex - 3);
    }

    public String getPinCode() {
        String messageText = getTextMessage(getMessageId(PIN_CODE_SUBJECT), getProperty("mailboxApiToken")).text;

        String startWithText = "PIN-код";
        String endWithText = "Скопируйте";
        int startIndex = messageText.indexOf(startWithText);
        int endIndex = messageText.indexOf(endWithText);

        return messageText.substring(startIndex + startWithText.length() + 2, endIndex - 2);
    }
}
