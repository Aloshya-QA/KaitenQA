package tests.api;

import api.kaiten.dto.request.CreatePasswordRq;
import org.testng.annotations.Test;

public class KaitenApiTest extends BaseApiTest{

    @Test(
            testName = "Создание пароля для аккаунта",
            groups = {"Initialization"}
    )
    public void checkCreatePassword() {
        String userId = kaitenService.getCurrentUserId(token, workspace);

        CreatePasswordRq rq = CreatePasswordRq.builder()
                .password(password)
                .oldPassword(null)
                .build();

        kaitenService.setPassword(rq, userId, token, workspace);
    }


}
