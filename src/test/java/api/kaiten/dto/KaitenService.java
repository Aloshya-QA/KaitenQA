package api.kaiten.dto;

import api.kaiten.dto.request.CreatePasswordRq;
import api.kaiten.dto.responce.CreatePasswordRs;
import api.kaiten.dto.responce.GetCurrentUserDataRs;

import static api.kaiten.dto.KaitenClient.createPassword;
import static api.kaiten.dto.KaitenClient.getCurrentUserData;

public class KaitenService {


    public String getCurrentUserId(String token, String workspace) {
        String userId = "";
        GetCurrentUserDataRs rs = getCurrentUserData(token, workspace);
//        for (GetCurrentUserDataRs currentUserData : rs.id) {
//            userId = currentUserData.id;
//        }
        return rs.id;
    }

    public CreatePasswordRs setPassword(CreatePasswordRq rq, String userId, String token, String workspace) {
        return createPassword(rq, userId, token, workspace);
    }
}
