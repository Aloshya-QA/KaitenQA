package api.tempMail;

import api.tempMail.dto.request.CreateMailRq;
import api.tempMail.dto.request.GetTokenRq;
import api.tempMail.dto.response.GetDomainsRs;
import api.tempMail.dto.response.GetMessagesRs;
import lombok.extern.log4j.Log4j2;

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

    public int getMessageCount() {
        String token = getProperty("mailboxApiToken");
        List<GetMessagesRs> messages = getMessages(token);
        int count = messages.size();

        if (count == 0) {
            setProperty("messageCount", "0");
        } else {
            setProperty("messageCount", String.valueOf(count));
        }
        saveProperties();

        log.info("Total messages in mailbox: {}", count);
        return count;
    }

    public String getMessageId(String subjectText) {
        String token = getProperty("mailboxApiToken");

        int messageCount = Integer.parseInt(getProperty("messageCount"));
        log.info("Message count {}", messageCount);

        int maxAttempts = 10;
        int delaySeconds = 2;

        for (int i = 0; i < maxAttempts; i++) {
            int currentCount = getMessageCount();

            if (currentCount > messageCount) {
                log.info("New messages detected ({} > {}) — searching for subject: {}", currentCount, messageCount, subjectText);

                List<GetMessagesRs> messages = getMessages(token);

                Optional<String> messageId = messages.stream()
                        .filter(m -> m.subject != null && m.subject.toLowerCase().contains(subjectText.toLowerCase()))
                        .max(Comparator.comparing(m -> Instant.parse(m.updatedAt)))
                        .map(m -> m.id);

                if (messageId.isPresent()) {
                    log.info("Found message on attempt {}: {}", i + 1, messageId.get());
                    return messageId.get();
                } else {
                    log.warn("New message(s) found, but none matched subject '{}'", subjectText);
                }
            } else {
                log.debug("No new messages yet. Attempt {}/{}: current = {}, expected > {}", i + 1, maxAttempts, currentCount, messageCount);
            }

            try {
                Thread.sleep(delaySeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.warn("No message with subject '{}' found after {} attempts", subjectText, maxAttempts);
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
