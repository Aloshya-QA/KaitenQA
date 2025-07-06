package api.tempMail;

import api.tempMail.dto.request.CreateMailRq;
import api.tempMail.dto.request.TokenRq;
import api.tempMail.dto.response.MessagesRs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static api.tempMail.TempMailClient.*;

@Data
@AllArgsConstructor
@Builder
public class TempMailService {

    private final String email;
    private final String password;
    private final static String PIN_CODE_SUBJECT = "PIN-код для аутентификации в Kaiten";

    public void createMail() {
        CreateMailRq rq = CreateMailRq.builder()
                .address(email)
                .password(password)
                .build();

        createTempMail(rq);
    }

    public String getToken() {
        TokenRq rq = TokenRq.builder()
                .address(email)
                .password(password)
                .build();

        return getAuthToken(rq).token;
    }

    public List<String> getEmailSubjects() throws InterruptedException {
        Thread.sleep(5000);
        List<MessagesRs> messages = getMessages(getToken());
        List<String> subjects = new ArrayList<>();
        for (MessagesRs message : messages) {
            subjects.add(message.subject);
        }
        return subjects;
    }

    private String getMessageId(String subjectText) {
        List<MessagesRs> messages = getMessages(getToken());

        return messages.stream()
                .filter(message -> message.subject.contains(subjectText))
                .max(Comparator.comparing(message -> Instant.parse(message.createdAt)))
                .map(message -> message.id)
                .orElse(null);
    }

    public String getTextLatestMessageBySubject(String subject) {
        return getTextMessage(getMessageId(subject), getToken()).text;
    }

    public String getActivateCompanyUrl() {
        String messageText = getTextLatestMessageBySubject("Активация компании");

        String startWithText = "активировать компанию";
        String endWithText = "Если вы не регистрировали";
        int startIndex = messageText.indexOf(startWithText);
        int endIndex = messageText.indexOf(endWithText);

        return messageText.substring(startIndex + startWithText.length() + 2, endIndex - 3);
    }

    public String getPinCode(){
        String messageText = getTextMessage(getMessageId(PIN_CODE_SUBJECT), getToken()).text;

        String startWithText = "PIN-код";
        String endWithText = "Скопируйте";
        int startIndex = messageText.indexOf(startWithText);
        int endIndex = messageText.indexOf(endWithText);

        return messageText.substring(startIndex + startWithText.length() + 2, endIndex - 2);
    }
}
