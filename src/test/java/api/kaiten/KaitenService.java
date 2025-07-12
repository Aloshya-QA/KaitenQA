package api.kaiten;

import api.kaiten.dto.request.CreatePasswordRq;
import api.kaiten.dto.response.CreatePasswordRs;

import static api.kaiten.KaitenClient.createPassword;
import static api.kaiten.KaitenClient.getCurrentUserData;

public class KaitenService {


    public String getCurrentUserId(String token, String workspace) {
        return getCurrentUserData(token, workspace).id;
    }

    public CreatePasswordRs setPassword(CreatePasswordRq rq, String userId, String token, String workspace) {
        return createPassword(rq, userId, token, workspace);
    }
}
